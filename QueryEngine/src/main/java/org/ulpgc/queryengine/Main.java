package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteRequestMaker;

import java.io.IOException;


public class Main {
    public static String LOCAL_MDB_API = "http://34.16.163.134";

    public static void main(String[] args) throws IOException {
        initializeDatabase();
        new Controller(args[0], Integer.parseInt(args[1]));
    }

    private static void initializeDatabase() {
        new RqliteRequestMaker();
    }
}
