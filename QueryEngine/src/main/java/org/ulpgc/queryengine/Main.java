package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;
import org.ulpgc.queryengine.controller.readMetadata.DatabaseDMLHandler;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteDMLHandler;
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
        MetadataBook metadataBook1 = new MetadataBook("Clean code: A Handbook of Agile Software Craftsmanship",
                "José Juan", "01/08/2008", "English");
        databaseDMLHandler.insertMetadata(metadataBook1, "6085");
        MetadataBook metadataBook4 = new MetadataBook("Design Patterns: Elements of Reusable Object-Oriented Software (Addison Wesley professional computing series)",
                "José Évora", "31/10/1994", "English");
        databaseDMLHandler.insertMetadata(metadataBook4, "39773");

    }
}
