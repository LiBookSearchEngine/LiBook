package org.ulpgc.indexer;

import org.ulpgc.indexer.controller.Controller;

public class Main {
    public static int SERVER_MQ_PORT = 443;
    public static int SERVER_CLEANER_PORT = 80;

    public static void main(String[] args) {
        try {
            API api = new API();
            api.run();
            Controller.run(args[0], args[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
