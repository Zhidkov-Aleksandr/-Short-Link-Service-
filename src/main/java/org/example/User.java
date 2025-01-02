package org.example;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private List<ShortLink> shortLinks;

    public User(int id) {
        this.id = id;
        this.shortLinks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<ShortLink> getShortLinks() {
        return shortLinks;
    }

    public void addShortLink(ShortLink link) {
        shortLinks.add(link);
    }
}