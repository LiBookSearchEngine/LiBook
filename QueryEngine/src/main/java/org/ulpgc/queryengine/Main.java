package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteDMLHandler;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteRequestMaker;
import org.ulpgc.queryengine.model.MetadataBook;

import java.io.IOException;


public class Main {
    public static String LOCAL_MDB_API = "http://localhost";

    public static void main(String[] args) throws IOException {
        initializeDatabase();

        RqliteDMLHandler rqliteDMLHandler = new RqliteDMLHandler();
        rqliteDMLHandler.createBooksTable();
        MetadataBook metadataBook = new MetadataBook("Peces en el mar 2", "Sus", "2024-01-18", "English");
        rqliteDMLHandler.insertMetadata(metadataBook);

        new Controller(args[0], Integer.parseInt(args[1]));
    }

    private static void initializeDatabase() {
        new RqliteRequestMaker();
    }
}
