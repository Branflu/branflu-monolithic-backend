package com.example.branflu.utils;

public class InstagramUtils {
    public static String extractUsernameFromUrl(String url) {
        if (url == null || !url.contains("instagram.com")) return null;
        String[] parts = url.split("instagram.com/");
        if (parts.length < 2) return null;
        String usernamePart = parts[1];
        if (usernamePart.contains("?")) {
            return usernamePart.substring(0, usernamePart.indexOf("?"));
        }
        return usernamePart;
    }
}
