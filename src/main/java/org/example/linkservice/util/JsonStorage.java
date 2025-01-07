package com.example.linkservice.util;

import com.example.linkservice.model.Link;
import com.example.linkservice.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {
    private List<User> users;
    private List<Link> links;
    private static final String FILE_PATH = "linkslist.json";
    public JsonStorage() {
        this.users = new ArrayList<>();
        this.links = new ArrayList<>();
    }
    public List<User> getUsers() {
        return users;
    }
    public List<Link> getLinks() {
        return links;
    }
    public static JsonStorage load() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new JsonStorage();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            Gson gson = new Gson();
            JsonStorage storage = gson.fromJson(br, JsonStorage.class);
            if (storage == null) return new JsonStorage();
            return storage;
        } catch (Exception e) {
            return new JsonStorage();
        }
    }
    public static void save(JsonStorage storage) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            bw.write(gson.toJson(storage));
        } catch (Exception e) {}
    }
}
