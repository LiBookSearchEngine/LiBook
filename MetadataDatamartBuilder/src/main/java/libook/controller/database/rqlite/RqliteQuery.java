package libook.controller.database.rqlite;

import libook.controller.database.DatabaseQuery;
import libook.controller.database.DatabaseRequestMaker;

public class RqliteQuery implements DatabaseQuery {
    private final DatabaseRequestMaker requestMaker;

    public RqliteQuery() {
        this.requestMaker = RqliteRequestMaker.getInstance();
    }

    public void selectAll() {
        String insertDataQuery = "SELECT * FROM book_metadata";
        requestMaker.make("query", insertDataQuery);
    }

    public void selectById(String id) {
        String query = String.format(
                "SELECT * FROM book_metadata WHERE id='%s'",
                id
        );

        StringBuilder queryResult = requestMaker.make("query", query);
        System.out.println(queryResult.toString());
    }

    public void selectByAuthor(String name) {
        String query = String.format(
                "SELECT * FROM book_metadata WHERE author='%s'",
                name
        );

        StringBuilder queryResult = requestMaker.make("query", query);
        System.out.println(queryResult.toString());
    }
}
