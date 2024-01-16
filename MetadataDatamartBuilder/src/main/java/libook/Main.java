package libook;

import libook.controller.Controller;
import libook.controller.database.rqlite.RqliteDMLHandler;
import libook.controller.database.rqlite.RqliteQuery;
import libook.controller.database.rqlite.RqliteRequestMaker;

import javax.jms.JMSException;
import java.io.IOException;

public class Main {
    public static String SERVER_MQ_PORT = "443";
    public static String SERVER_API_URL = "http://34.125.120.252";
    public static String SERVER_CLEANER_PORT = "8080";
    public static String LOCAL_MDB_API = "http://192.168.1.134";

    public static void main(String[] args) throws JMSException, IOException {
        initializeDatabase();

        RqliteQuery rqliteQuery = new RqliteQuery();
        rqliteQuery.selectAll();

        RqliteDMLHandler rqliteDMLHandler = new RqliteDMLHandler();
        rqliteDMLHandler.createBooksTable();

        Controller controller = new Controller(rqliteDMLHandler);
        controller.run();
    }

    private static void initializeDatabase() {
        new RqliteRequestMaker();
    }
}
