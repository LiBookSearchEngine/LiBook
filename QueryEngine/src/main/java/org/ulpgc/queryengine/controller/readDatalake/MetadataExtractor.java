package org.ulpgc.queryengine.controller.readDatalake;

import org.ulpgc.queryengine.model.MetadataBook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataExtractor {

    public static MetadataBook extractMetadata(String text) {
        String title = extractTitle(text);
        String author = extractAuthor(text);
        String releaseDate = extractReleaseDate(text);
        String language = extractLanguage(text);

        return new MetadataBook(title, author, releaseDate, language);
    }

    private static String extractTitle(String text) {
        Pattern pattern = Pattern.compile("Title: (.+) Author:");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String extractAuthor(String text) {
        Pattern pattern = Pattern.compile("Author: (.+) Release date:");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String extractReleaseDate(String text) {
        Pattern pattern = Pattern.compile("Release date: (\\w+ \\d+, \\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String releaseDateString = matcher.group(1);
            return releaseDateString;
        }
        return null;
    }

    private static String extractLanguage(String text) {
        Pattern pattern = Pattern.compile("Language: (\\w+)\\s");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
