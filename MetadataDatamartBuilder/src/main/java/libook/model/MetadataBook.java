package libook.model;

import java.util.Date;

public class MetadataBook {
    private final String id;
    private final String title;
    private final String author;
    private final Date releaseDate;
    private final String language;

    public MetadataBook(String id, String title, String author, Date releaseDate, String language) {
        this.id = id;
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

    public Date releaseDate() {
        return releaseDate;
    }

    public String language() {
        return language;
    }

    public String id() {
        return id;
    }
}
