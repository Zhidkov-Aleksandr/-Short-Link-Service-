import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkManagerTest {
    
    private final LinkManager linkManager = new FileLinkManager("linkslist.txt");

    @Test
    void testUniqueShortLinksForDifferentUsers() {
        String url = "https://example.com";
        int userId1 = 1;
        int userId2 = 2;

        linkManager.createShortLink(userId1, url, 60, 10);
        ShortLink shortLink1 = linkManager.getLinkByLongUrl(url, userId1);
        
        linkManager.createShortLink(userId2, url, 60, 10);
        ShortLink shortLink2 = linkManager.getLinkByLongUrl(url, userId2);

        assertNotEquals(shortLink1.getShortUrl(), shortLink2.getShortUrl());
    }

@Test
void testLimitExhaustionBlocksLink() {
    int userId = 1;
    String url = "https://limitcheck.com";
    linkManager.createShortLink(userId, url, 60, 1); // 1 клик

    ShortLink link = linkManager.getLink(url);
    link.decrementClicks(); // Уменьшаем до 0

    assertEquals(0, link.getRemainingClicks());
    assertFalse(link.isValid());

    // Проверяем, что переход по ссылке теперь недоступен.
    Exception exception = assertThrows(IllegalStateException.class, () -> {
        linkManager.followLink(link.getShortUrl());
    });

    assertEquals("Ссылка недействительна", exception.getMessage());
}
    @Test
void testNotificationsOnLinkUnavailability() {
    int userId = 1;
    String url = "https://unavailablelink.com";
    linkManager.createShortLink(userId, url, 1, 1); // 1 минута жизни и 1 клик

    ShortLink link = linkManager.getLink(url);
    link.decrementClicks(); // Уменьшаем до 0
    assertFalse(link.isValid());

    String notification = linkManager.followLink(link.getShortUrl()); // Метод возвращает сообщение

    assertEquals("Ссылка недоступна из-за исчерпания лимита или истечения срока жизни.", notification);
}
    
}


