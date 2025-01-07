package com.example.linkservice.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    private String uuid;
    public User() {
        this.uuid = UUID.randomUUID().toString();
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid);
    }
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
