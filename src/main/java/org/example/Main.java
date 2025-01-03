package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.awt.Desktop;
import java.net.URI;

public class Main {
    private static final String FILE_PATH = "linkslist.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final LinkManager linkManager = new FileLinkManager(FILE_PATH);
    private static int currentUserId;

    public static void main(String[] args) {
        initUsers();
        while (true) {
            System.out.print("Введите ваш ID пользователя (или 0 для создания нового): ");
            currentUserId = scanner.nextInt();
            scanner.nextLine();
            if (currentUserId == 0) {
                currentUserId = createNewUser();
            }
            menu();
        }
    }

    private static void initUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int createNewUser() {
        List<Integer> userIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                userIds.add(Integer.parseInt(parts[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userIds.isEmpty() ? 1 : Collections.max(userIds) + 1;
    }

    private static void menu() {
        while (true) {
            System.out.println("Ваш ID номер: " + currentUserId);
            System.out.println("Выберите действие:");
            System.out.println("1. Создать короткую ссылку");
            System.out.println("2. Вывести список всех коротких ссылок данного пользователя");
            System.out.println("3. Перейти по короткой ссылке");
            System.out.println("4. Сменить пользователя");
            System.out.println("0. Выход");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: createShortLink(); break;
                case 2: manageUserLinks(); break;
                case 3: followShortLink();; break;
                case 4: currentUserId = 0; return;
                case 0: System.exit(0); break;
                default: System.out.println("Неверный выбор. Попробуйте снова."); break;
            }
        }
    }

    private static void createShortLink() {
        System.out.print("Введите длинную ссылку: ");
        String longUrl = scanner.nextLine();
        System.out.print("Введите срок жизни ссылки в минутах: ");
        int lifespan = scanner.nextInt();
        System.out.print("Введите количество переходов по ссылке: ");
        int clicks = scanner.nextInt();
        scanner.nextLine();
        linkManager.createShortLink(currentUserId, longUrl, lifespan, clicks);
        System.out.println("Короткая ссылка успешно создана!");
    }

    private static void manageUserLinks() {
        List<ShortLink> userLinks = linkManager.getUserLinks(currentUserId);
        if (userLinks.isEmpty()) {
            System.out.println("У вас нет сохраненных ссылок.");
            return;
        }
        while (true) {
            System.out.println("Ваши короткие ссылки:");
            for (int i = 0; i < userLinks.size(); i++) {
                ShortLink link = userLinks.get(i);
                System.out.printf("%d. Короткая ссылка: %s, длинная ссылка: %s, срок жизни: %d минут, оставшиеся переходы: %d%n",
                        i + 1, link.getShortUrl(), link.getLongUrl(), link.getLifespan(), link.getRemainingClicks());
            }
            System.out.println("Выберите номер ссылки для изменения (0 для выхода): ");
            int linkChoice = scanner.nextInt();
            scanner.nextLine();
            if (linkChoice == 0) break;
            if (linkChoice > 0 && linkChoice <= userLinks.size()) {
                ShortLink selectedLink = userLinks.get(linkChoice - 1);
                System.out.println("Выберите действие:");
                System.out.println("1. Изменить срок жизни ссылки");
                System.out.println("2. Изменить количество переходов");
                System.out.println("3. Удалить ссылку");
                int action = scanner.nextInt();
                scanner.nextLine();
                switch (action) {
                    case 1:
                        System.out.print("Введите новый срок жизни в минутах: ");
                        int newLifespan = scanner.nextInt();
                        selectedLink.setLifespan(newLifespan);
                        linkManager.updateLink(selectedLink, currentUserId);
                        System.out.println("Срок жизни ссылки обновлен.");
                        break;
                    case 2:
                        System.out.print("Введите новое количество переходов: ");
                        int newClicks = scanner.nextInt();
                        selectedLink.setRemainingClicks(newClicks);
                        linkManager.updateLink(selectedLink, currentUserId);
                        System.out.println("Количество переходов обновлено.");
                        break;
                    case 3:
                        linkManager.deleteLink(selectedLink);
                        System.out.println("Ссылка удалена.");
                        break;
                    default:
                        System.out.println("Неверный выбор.");
                        break;
                }
            } else {
                System.out.println("Некорректный номер ссылки.");
            }
        }
    }

    private static void followShortLink() {
        System.out.print("Введите короткую ссылку: ");
        String shortUrl = scanner.nextLine();
        ShortLink link = linkManager.getLink(shortUrl);
        if (link != null) {
            if (link.isValid()) {
                link.decrementClicks(); // Уменьшить количество оставшихся переходов
                linkManager.updateLink(link, currentUserId); // Обновить ссылку в менеджере

                System.out.printf("Оставшийся срок жизни ссылки: %d минут.%n", link.getLifespan());
                System.out.printf("Количество оставшихся переходов: %d%n", link.getRemainingClicks());

                try {
                    Desktop.getDesktop().browse(new URI(link.getLongUrl())); // Открыть ссылку в браузере
                } catch (Exception e) {
                    System.out.println("Не удалось открыть ссылку в браузере: " + e.getMessage());
                }
            } else {
                System.out.println("Ссылка больше недействительна (исчерпаны переходы или срок жизни завершен).");
                linkManager.deleteLink(link);
            }
        } else {
            System.out.println("Короткая ссылка не найдена.");
        }
    }
}
