package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;
import org.ulpgc.queryengine.controller.readMetadata.DatabaseDMLHandler;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteDMLHandler;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteQuery;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteRequestMaker;
import org.ulpgc.queryengine.model.MetadataBook;

import java.io.IOException;


public class Main {
    public static String LOCAL_MDB_API = "http://localhost";

    public static void main(String[] args) throws IOException {
        initializeDatabase();
        new Controller(args[0], Integer.parseInt(args[1]));
    }

    private static void initializeDatabase() {
        new RqliteRequestMaker();
        DatabaseDMLHandler databaseDMLHandler = new RqliteDMLHandler();
        databaseDMLHandler.createBooksTable();
        MetadataBook metadataBook = new MetadataBook("The Declaration of Independence of the United States of America by Thomas Jefferson",
                "Ricardo", "01/06/2023", "English");
        databaseDMLHandler.insertMetadata(metadataBook, "1");
        MetadataBook metadataBook2 = new MetadataBook("The United States Bill of Rights by United States",
                "Susana", "12/07/2023", "English");
        databaseDMLHandler.insertMetadata(metadataBook2, "2");
        MetadataBook metadataBook3 = new MetadataBook("Místicas; poesías by María Raquel Adler",
                "Carlos", "01/01/2024", "Spanish");
        databaseDMLHandler.insertMetadata(metadataBook3, "61415");

    }
}
