package com.example.linkservice.service;

import com.example.linkservice.model.Link;
import java.util.Scanner;

public interface LinkService {
    Link addNewLink(String longUrl, String ownerUuid, Scanner scanner);
    void printUserLinks(String ownerUuid);
    void goToLink(String shortUrl);
}
