package database.controller.rqlite;

import database.controller.DatabaseRequestMaker;

public class RqliteDMLHandler {
    private final DatabaseRequestMaker requestMaker;

    public RqliteDMLHandler() {
        this.requestMaker = RqliteRequestMaker.getInstance();
    }

    public void createBooksTable() {
        String createTableQuery = "CREATE TABLE book_metadata ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "author TEXT,"
                + "language TEXT,"
                + "date DATE)";
        requestMaker.make("execute", createTableQuery);
    }

    public void insertSampleData() {
        String insertDataQuery = "INSERT INTO book_metadata (author, language, date, title) VALUES "
                + "('Author1', 'English', '2022-01-01', 'Book1'),"
                + "('Author2', 'Spanish', '2022-01-02', 'Book2'),"
                + "('Author3', 'French', '2022-01-03', 'Book3')";
        requestMaker.make("execute", insertDataQuery);
    }
}
