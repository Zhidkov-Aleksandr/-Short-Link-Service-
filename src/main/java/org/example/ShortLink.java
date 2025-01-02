package org.example;

import java.time.LocalDateTime;

public class ShortLink {
    private String shortUrl;
    private String longUrl;
    private LocalDateTime creationTime;
    private int lifespan;
    private int remainingClicks;

    public ShortLink(String shortUrl, String longUrl, LocalDateTime creationTime, int lifespan, int remainingClicks) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.creationTime = creationTime;
        this.lifespan = lifespan;
        this.remainingClicks = remainingClicks;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public int getRemainingClicks() {
        return remainingClicks;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void decrementClicks() {
        remainingClicks--;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public void setRemainingClicks(int remainingClicks) {
        this.remainingClicks = remainingClicks;
    }

    public boolean isValid() {
        return remainingClicks > 0 && LocalDateTime.now().isBefore(creationTime.plusMinutes(lifespan));
    }
}