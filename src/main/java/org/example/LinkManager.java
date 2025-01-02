package org.example;

import java.util.List;

public interface LinkManager {
    void createShortLink(int userId, String longUrl, int lifespan, int clicks);
    ShortLink getLink(String shortUrl);
    List<ShortLink> getUserLinks(int userId);
    void updateLink(ShortLink link, int userId);
    void deleteLink(ShortLink link);
    void deleteUserLinks(int userId);
}