package com.example.linkservice.service;

import com.example.linkservice.ConfigReader;
import com.example.linkservice.model.Link;
import com.example.linkservice.util.JsonStorage;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class LinkServiceImpl implements LinkService {
    private final ConfigReader configReader;
    public LinkServiceImpl(String configFilePath) {
        this.configReader = new ConfigReader(configFilePath);
    }
    @Override
    public Link addNewLink(String longUrl, String ownerUuid, Scanner scanner) {
        if (longUrl == null || longUrl.trim().isEmpty()) return null;
        int maxLifetime = configReader.getMaxLifetimeHours();
        int maxTransitions = configReader.getMaxTransitions();
        System.out.print("Введите срок жизни ссылки (максимум " + maxLifetime + " часов): ");
        int userLifetime;
        try {
            userLifetime = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            userLifetime = maxLifetime;
        }
        if (userLifetime > maxLifetime) userLifetime = maxLifetime;
        System.out.print("Введите количество переходов (максимум " + maxTransitions + "): ");
        int userTransitions;
        try {
            userTransitions = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            userTransitions = maxTransitions;
        }
        if (userTransitions > maxTransitions) userTransitions = maxTransitions;
        String shortUrl = "short.ly/" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Link newLink = new Link(ownerUuid, longUrl, shortUrl, userLifetime, userTransitions);
        JsonStorage storage = JsonStorage.load();
        storage.getLinks().add(newLink);
        JsonStorage.save(storage);
        return newLink;
    }
    @Override
    public void printUserLinks(String ownerUuid) {
        JsonStorage storage = JsonStorage.load();
        List<Link> links = storage.getLinks();
        boolean found = false;
        for (Link link : links) {
            if (link.getOwnerUuid().equals(ownerUuid)) {
                found = true;
                System.out.println("Длинная ссылка: " + link.getLongUrl());
                System.out.println("Короткая ссылка: " + link.getShortUrl());
                System.out.println("Дата создания (мс): " + link.getCreationTimeMillis());
                System.out.println("Срок жизни (часов): " + link.getLifetimeHours());
                System.out.println("Максимум переходов: " + link.getMaxTransitions());
                System.out.println("Совершено переходов: " + link.getCurrentTransitions());
                System.out.println("Недействительна: " + link.isInvalid());
                System.out.println("------------------------");
            }
        }
        if (!found) System.out.println("Нет добавленных ссылок.");
    }
    @Override
    public void goToLink(String shortUrl) {
        JsonStorage storage = JsonStorage.load();
        List<Link> links = storage.getLinks();
        boolean found = false;
        for (Link link : links) {
            if (link.getShortUrl().equals(shortUrl)) {
                found = true;
                if (link.isInvalid()) {
                    System.out.println("Ссылка недействительна");
                    break;
                }
                long now = System.currentTimeMillis();
                long lifeMillis = link.getLifetimeHours() * 3600000L;
                if (now - link.getCreationTimeMillis() > lifeMillis) {
                    System.out.println("Время жизни ссылки истекло");
                    link.setInvalid(true);
                    break;
                }
                if (link.getCurrentTransitions() >= link.getMaxTransitions()) {
                    System.out.println("Переходы по ссылке исчерпаны");
                    link.setInvalid(true);
                    break;
                }
                try {
                    Desktop.getDesktop().browse(new URI(link.getLongUrl()));
                } catch (Exception e) {
                    System.out.println("Невозможно открыть ссылку");
                }
                link.setCurrentTransitions(link.getCurrentTransitions() + 1);
                if (link.getCurrentTransitions() >= link.getMaxTransitions()) {
                    link.setInvalid(true);
                }
                int remaining = link.getMaxTransitions() - link.getCurrentTransitions();
                System.out.println("Осталось переходов: " + remaining);
                long usedTime = now - link.getCreationTimeMillis();
                long remainingTime = lifeMillis - usedTime;
                double rh = remainingTime / 3600000.0;
                System.out.printf("Оставшееся время жизни ссылки: %.2f часов\n", rh);
                break;
            }
        }
        if (!found) System.out.println("Ссылка не найдена");
        JsonStorage.save(storage);
    }
}
