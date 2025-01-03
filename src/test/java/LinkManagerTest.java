import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LinkManagerTest {

    private LinkManager linkManager;
    private static final String TEST_FILE_PATH = "test_linkslist.txt"; // Создаем отдельный файл для тестов

    @BeforeEach
    void setUp() {
        linkManager = new FileLinkManager(TEST_FILE_PATH);
        // Убедитесь, что файл пуст в начале каждого теста
        linkManager.clearLinks(); // Можно реализовать метод clearLinks в LinkManager
    }

    @Test
    void testUniqueShortLinksForDifferentUsers() {
        String url = "https://example.com";
        int userId1 = 1;
        int userId2 = 2;

        String shortUrl1 = linkManager.createShortLink(userId1, url, 60, 10);
        String shortUrl2 = linkManager.createShortLink(userId2, url, 60, 10);

        assertNotEquals(shortUrl1, shortUrl2, "Short links should be unique for different users.");
    }

    @Test
    void testLimitExhaustionBlocksLink() {
        int userId = 1;
        String url = "https://limitcheck.com";
        
        linkManager.createShortLink(userId, url, 60, 1); // 1 клик

        // Получаем созданную короткую ссылку
        ShortLink link = linkManager.getLink(url);
        link.decrementClicks(); // Уменьшаем до 0

        assertEquals(0, link.getRemainingClicks(), "Remaining clicks should be 0.");
        assertFalse(link.isValid(), "Link should be invalid after reaching click limit.");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            linkManager.followLink(link.getShortUrl()); // Пытаемся перейти по ссылке
        });
        assertEquals("Ссылка недействительна", exception.getMessage());
    }

    @Test
    void testExpiredLinkDeletion() throws InterruptedException {
        int userId = 1;
        String url = "https://expirationtest.com";
        
        linkManager.createShortLink(userId, url, 1, 10); // 1 минута жизни

        // Ждем более 1 минуты
        TimeUnit.MINUTES.sleep(1);

        // Проверяем, что ссылка «протухла»
        ShortLink link = linkManager.getLink(url);
        assertTrue(link.isExpired(), "Link should be expired.");

        linkManager.deleteLink(link); // Удаляем ссылку

        assertNull(linkManager.getLink(url), "Expired link should be deleted.");
    }

    @Test
    void testNotificationsOnLinkUnavailability() {
        int userId = 1;
        String url = "https://unavailablelink.com";
        
        linkManager.createShortLink(userId, url, 1, 1); // 1 минута жизни и 1 клик

        ShortLink link = linkManager.getLink(url);
        link.decrementClicks(); // Уменьшаем до 0
        
        assertFalse(link.isValid(), "Link should be invalid after reaching click limit.");

        String notification = linkManager.followLink(link.getShortUrl()); // Метод возвращает сообщение
        assertEquals("Ссылка недоступна из-за исчерпания лимита или истечения срока жизни.", notification);
    }
}
