package com.example.linkservice.service;

import com.example.linkservice.model.User;
import com.example.linkservice.util.JsonStorage;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User registerUser() {
        User user = new User();
        JsonStorage storage = JsonStorage.load();
        storage.getUsers().add(user);
        JsonStorage.save(storage);
        return user;
    }
    @Override
    public User loginUser(String uuid) {
        JsonStorage storage = JsonStorage.load();
        List<User> users = storage.getUsers();
        for (User u : users) {
            if (u.getUuid().equals(uuid)) {
                return u;
            }
        }
        return null;
    }
}
