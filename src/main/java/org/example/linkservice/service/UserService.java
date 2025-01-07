package com.example.linkservice.service;

import com.example.linkservice.model.User;

public interface UserService {
    User registerUser();
    User loginUser(String uuid);
}
