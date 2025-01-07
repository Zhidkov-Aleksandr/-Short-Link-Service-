package com.example.linkservice.model;

public class Link {
    private String ownerUuid;
    private String longUrl;
    private String shortUrl;
    private int lifetimeHours;
    private int maxTransitions;
    private int currentTransitions;
    private long creationTimeMillis;
    private boolean invalid;
    public Link(String ownerUuid, String longUrl, String shortUrl, int lifetimeHours, int maxTransitions) {
        this.ownerUuid = ownerUuid;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.lifetimeHours = lifetimeHours;
        this.maxTransitions = maxTransitions;
        this.currentTransitions = 0;
        this.creationTimeMillis = System.currentTimeMillis();
        this.invalid = false;
    }
    public String getOwnerUuid() {
        return ownerUuid;
    }
    public String getLongUrl() {
        return longUrl;
    }
    public String getShortUrl() {
        return shortUrl;
    }
    public int getLifetimeHours() {
        return lifetimeHours;
    }
    public int getMaxTransitions() {
        return maxTransitions;
    }
    public int getCurrentTransitions() {
        return currentTransitions;
    }
    public long getCreationTimeMillis() {
        return creationTimeMillis;
    }
    public boolean isInvalid() {
        return invalid;
    }
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
    public void setCurrentTransitions(int currentTransitions) {
        this.currentTransitions = currentTransitions;
    }
}
