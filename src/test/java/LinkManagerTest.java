package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkManagerTest {
    private static final String TEST_FILE_PATH = "test_linkslist.txt";
    private LinkManager linkManager;

    @BeforeEach
    void setUp() throws IOException {
        linkManager = new FileLinkManager(TEST_FILE_PATH);
        new File(TEST_FILE_PATH).createNewFile();
    }

    @Test
    void testCreateShortLink() {
        linkManager.createShortLink(1, "http://example.com", 60, 5);
        ShortLink retrievedLink = linkManager.getLink("http://linkreducer.ru/" + TEST_FILE_PATH.substring(13, 18)); // Adjust according to UUID
        assertNotNull(retrievedLink);
        assertEquals("http://example.com", retrievedLink.getLongUrl());
    }

    @Test
    void testGetUserLinks() {
        linkManager.createShortLink(1, "http://example1.com", 60, 5);
        linkManager.createShortLink(1, "http://example2.com", 60, 5);
        List<ShortLink> userLinks = linkManager.getUserLinks(1);
        assertEquals(2, userLinks.size());
    }

    @Test
    void testUpdateLink() {
        linkManager.createShortLink(1, "http://example.com", 60, 5);
        ShortLink link = linkManager.getLink("http://linkreducer.ru/" + TEST_FILE_PATH.substring(13, 18)); // Adjust according to UUID
        link.setRemainingClicks(3);
        linkManager.updateLink(link, 1);
        ShortLink updatedLink = linkManager.getLink("http://linkreducer.ru/" + TEST_FILE_PATH.substring(13, 18));
        assertEquals(3, updatedLink.getRemainingClicks());
    }

    @Test
    void testDeleteLink() {
        linkManager.createShortLink(1, "http://example.com", 60, 5);
        ShortLink link = linkManager.getLink("http://linkreducer.ru/" + TEST_FILE_PATH.substring(13, 18)); // Adjust according to UUID
        linkManager.deleteLink(link);
        assertNull(linkManager.getLink(link.getShortUrl()));
    }
}