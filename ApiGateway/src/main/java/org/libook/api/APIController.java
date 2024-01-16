package org.libook.api;

import static spark.Spark.port;

public class APIController {
    public APIController() {}

    public void run() {
        port(80);
        new UserAPIController().run();
        new CleanerAPIController().run();
        new UserBooksAPIController();
    }
}
