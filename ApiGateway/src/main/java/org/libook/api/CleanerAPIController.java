package org.libook.api;

import org.libook.connections.ConnectionHandler;
import org.libook.connections.HTTPConnectionHandler;

import static spark.Spark.get;

public class CleanerAPIController {
    private final String CLEANER_SERVICE_API;
    private final ConnectionHandler connectionHandler;

    public CleanerAPIController() {
        this.CLEANER_SERVICE_API = System.getenv("CLEANER_SERVICE_API");
        connectionHandler = new HTTPConnectionHandler();
    }

    public void run() {
        handleUserAPI();
    }

    private void handleUserAPI() {
        getMetadata();
        getContent();
        getRawBooks();
        getContentDocuments();
    }

    private void getMetadata() {
        get("datalake/metadata/:idBook", (req, res) ->
                connectionHandler.makeUrlRequest(CLEANER_SERVICE_API + "datalake/metadata/" + req.params("idBook"), req, res, "GET"));
    }

    private void getContentDocuments() {
        get("datalake/content", (req, res) ->
                connectionHandler.makeUrlRequest(CLEANER_SERVICE_API + "datalake/content", req, res, "GET"));
    }

    private void getContent() {
        get("datalake/content/:idBook", (req, res) ->
                connectionHandler.makeUrlRequest(CLEANER_SERVICE_API + "datalake/content/" + req.params("idBook"), req, res, "GET"));
    }

    private void getRawBooks() {
        get("datalake/books/:idBook", (req, res) ->
                connectionHandler.makeUrlRequest(CLEANER_SERVICE_API + "datalake/books/" + req.params("idBook"), req, res, "GET"));
    }
}
