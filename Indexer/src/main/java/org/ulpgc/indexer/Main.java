package org.ulpgc.indexer;

import org.ulpgc.indexer.controller.Controller;

public class Main {
    public static int SERVER_MQ_PORT = Integer.parseInt(System.getenv("SERVER_MQ_PORT"));
    public static int SERVER_CLEANER_PORT = 80;
    public static String SERVER_API_URL = System.getenv("SERVER_API_URL");
    public static String INDEXER_INDEX = System.getenv("index");
    public static int INDEXED_BOOKS = 0;

    public static void main(String[] args) {
        try {
            Controller.run(SERVER_API_URL, INDEXER_INDEX);
            new API().run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
