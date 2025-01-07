package com.example.linkservice;

import com.example.linkservice.model.Link;
import com.example.linkservice.model.User;
import com.example.linkservice.service.LinkService;
import com.example.linkservice.service.LinkServiceImpl;
import com.example.linkservice.service.UserService;
import com.example.linkservice.service.UserServiceImpl;
import java.util.Scanner;

public class Main {
    private static final String CONFIG_FILE_PATH = "config.txt";
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        LinkService linkService = new LinkServiceImpl(CONFIG_FILE_PATH);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("Добро пожаловать в LinkService!");
        while (running) {
            System.out.println("\nГлавное меню:");
            System.out.println("1. Регистрация нового пользователя");
            System.out.println("2. Вход в систему (по UUID)");
            System.out.println("3. Выход");
            System.out.print("Введите номер команды: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    registerUser(userService);
                    break;
                case "2":
                    loginUser(scanner, userService, linkService);
                    break;
                case "3":
                    running = false;
                    System.out.println("Выход из программы...");
                    break;
                default:
                    System.out.println("Неизвестная команда. Повторите ввод.");
            }
        }
        scanner.close();
    }
    private static void registerUser(UserService userService) {
        User newUser = userService.registerUser();
        if (newUser != null) {
            System.out.println("Пользователь зарегистрирован. Ваш UUID: " + newUser.getUuid());
        } else {
            System.out.println("Ошибка регистрации.");
        }
    }
    private static void loginUser(Scanner scanner, UserService userService, LinkService linkService) {
        System.out.print("Введите ваш UUID: ");
        String inputUuid = scanner.nextLine();
        User user = userService.loginUser(inputUuid);
        if (user != null) {
            System.out.println("Авторизация успешна. Ваш UUID: " + user.getUuid());
            userMenu(scanner, user, linkService);
        } else {
            System.out.println("Пользователь не найден.");
        }
    }
    private static void userMenu(Scanner scanner, User user, LinkService linkService) {
        boolean userMenuActive = true;
        while (userMenuActive) {
            System.out.println("\nМеню пользователя (UUID: " + user.getUuid() + "):");
            System.out.println("1. Добавить новую ссылку");
            System.out.println("2. Показать все мои ссылки");
            System.out.println("3. Выйти в главное меню");
            System.out.println("4. Перейти по ссылке");
            System.out.print("Введите номер команды: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createNewLink(scanner, linkService, user);
                    break;
                case "2":
                    linkService.printUserLinks(user.getUuid());
                    break;
                case "3":
                    userMenuActive = false;
                    break;
                case "4":
                    goToLink(scanner, linkService);
                    break;
                default:
                    System.out.println("Неизвестная команда.");
            }
        }
    }
    private static void createNewLink(Scanner scanner, LinkService linkService, User user) {
        System.out.print("Введите исходную (длинную) ссылку: ");
        String longUrl = scanner.nextLine();
        Link link = linkService.addNewLink(longUrl, user.getUuid(), scanner);
        if (link != null) {
            System.out.println("Ссылка добавлена. Короткая ссылка: " + link.getShortUrl());
        } else {
            System.out.println("Ошибка при добавлении ссылки.");
        }
    }
    private static void goToLink(Scanner scanner, LinkService linkService) {
        System.out.print("Введите короткую ссылку: ");
        String shortUrl = scanner.nextLine();
        linkService.goToLink(shortUrl);
    }
}
