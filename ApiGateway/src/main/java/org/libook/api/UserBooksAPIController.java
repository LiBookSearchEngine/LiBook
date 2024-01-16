package org.libook.api;

import org.libook.connections.ConnectionHandler;
import org.libook.connections.HTTPConnectionHandler;

import static spark.Spark.get;
import static spark.Spark.post;

public class UserBooksAPIController {
    private final String USER_BOOKS_SERVICE_API;
    private final ConnectionHandler connectionHandler;

    public UserBooksAPIController() {
        this.USER_BOOKS_SERVICE_API = System.getenv("USER_SERVICE_API");
        connectionHandler = new HTTPConnectionHandler();
    }

    public void run() {
        handleUserAPI();
    }

    private void handleUserAPI() {
        postBook();
    }

    private void postBook() {
        get("user/post", (req, res) ->
                connectionHandler.makeUrlRequest(USER_BOOKS_SERVICE_API + "user/login", req, res, "GET"));
    }
}
