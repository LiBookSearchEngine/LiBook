package libook.controller.database.rqlite;

import libook.controller.database.DatabaseDMLHandler;
import libook.controller.database.DatabaseRequestMaker;
import libook.model.MetadataBook;

public class RqliteDMLHandler implements DatabaseDMLHandler {
    private final DatabaseRequestMaker requestMaker;
    private MetadataBook book;

    public RqliteDMLHandler() {
        this.requestMaker = RqliteRequestMaker.getInstance();
    }

    @Override
    public void createBooksTable() {
        String createTableQuery = "CREATE TABLE book_metadata ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "author TEXT,"
                + "language TEXT,"
                + "date DATE)";
        requestMaker.make("execute", createTableQuery);
    }

    @Override
    public void insertMetadata(MetadataBook book) {
        this.book = book;
        String insertDataQuery = String.format(
                "INSERT INTO book_metadata (title, author, language, date) VALUES ('%s', '%s', '%s', '%s')",
                book.title(),
                book.author(),
                book.language(),
                book.releaseDate()
        );

        requestMaker.make("execute", insertDataQuery);
    }
}
