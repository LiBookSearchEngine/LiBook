import database.controller.rqlite.RqliteQuery;
import database.controller.rqlite.RqliteRequestMaker;

public class Main {
    public static void main(String[] args) {
        initializeDatabase();

        RqliteQuery rqliteQuery = new RqliteQuery();
        rqliteQuery.selectById("2");
    }

    private static void initializeDatabase() {
        new RqliteRequestMaker();
    }
}
