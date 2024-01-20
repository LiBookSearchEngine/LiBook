package libook.controller.extractors;

import libook.model.MetadataBook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataExtractor {

    public static MetadataBook extractMetadata(String id, String text) {
        String title = extractTitle(text);
        String author = extractAuthor(text);
        Date releaseDate = extractReleaseDate(text);
        String language = extractLanguage(text);

        return new MetadataBook(id, title, author, releaseDate, language);
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

    private static Date extractReleaseDate(String text) {
        Pattern pattern = Pattern.compile("Release date: (\\w+ \\d+, \\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String releaseDateString = matcher.group(1);
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                return dateFormat.parse(releaseDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
