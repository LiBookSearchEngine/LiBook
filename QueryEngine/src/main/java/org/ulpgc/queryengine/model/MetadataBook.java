package org.ulpgc.queryengine.model;

public class MetadataBook {
    private final String title;
    private final String author;
    private final String releaseDate;
    private final String language;

    public MetadataBook(String title, String author, String releaseDate, String language) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.language = language;
    }

    public String title() {
        return title;
    }

    public String author() {
        return author;
    }

    public String releaseDate() {
        return releaseDate;
    }

    public String language() {
        return language;
    }

}
