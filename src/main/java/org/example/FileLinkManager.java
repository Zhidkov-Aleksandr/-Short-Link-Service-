package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileLinkManager implements LinkManager {
    private final String filePath;

    public FileLinkManager(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void createShortLink(int userId, String longUrl, int lifespan, int clicks) {
        String shortUrl = "http://linkreducer.ru/" + UUID.randomUUID().toString().substring(0, 5);
        ShortLink newLink = new ShortLink(shortUrl, longUrl, LocalDateTime.now(), lifespan, clicks);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.format("%d, \"%s\", \"%s\", \"%s\", %d, %d%n",
                    userId, shortUrl, longUrl, LocalDateTime.now(), lifespan, clicks));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ShortLink getLink(String shortUrl) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts[1].equals("\"" + shortUrl + "\"")) {
                    return new ShortLink(parts[1].replace("\"", ""),
                            parts[2].replace("\"", ""),
                            LocalDateTime.parse(parts[3].replace("\"", "")),
                            Integer.parseInt(parts[4]),
                            Integer.parseInt(parts[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ShortLink> getUserLinks(int userId) {
        List<ShortLink> links = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (Integer.parseInt(parts[0]) == userId) {
                    links.add(new ShortLink(parts[1].replace("\"", ""),
                            parts[2].replace("\"", ""),
                            LocalDateTime.parse(parts[3].replace("\"", "")),
                            Integer.parseInt(parts[4]),
                            Integer.parseInt(parts[5])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    @Override
    public void updateLink(ShortLink link, int userId) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(link.getShortUrl())) {
                    String newLine = String.format("%d, \"%s\", \"%s\", \"%s\", %d, %d",
                            userId, link.getShortUrl(), link.getLongUrl(),
                            link.getCreationTime(), link.getLifespan(), link.getRemainingClicks());
                    lines.add(newLine);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String l : lines) {
                writer.write(l + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLink(ShortLink link) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(link.getShortUrl())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String l : lines) {
                writer.write(l + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserLinks(int userId) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (Integer.parseInt(parts[0]) != userId) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String l : lines) {
                writer.write(l + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}