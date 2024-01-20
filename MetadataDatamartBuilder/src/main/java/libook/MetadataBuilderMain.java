package libook;

import libook.controller.Controller;
import libook.controller.database.rqlite.RqliteDMLHandler;
import libook.controller.database.rqlite.RqliteQuery;
import libook.controller.database.rqlite.RqliteRequestMaker;

import javax.jms.JMSException;
import java.io.IOException;

public class MetadataBuilderMain {
    public static String SERVER_MQ_PORT = System.getenv("SERVER_MQ_PORT");
    public static String SERVER_API_URL = System.getenv("SERVER_API_URL");
    public static String SERVER_CLEANER_PORT = System.getenv("SERVER_CLEANER_PORT");
    public static String LOCAL_MDB_API = System.getenv("LOCAL_MDB_API");

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
