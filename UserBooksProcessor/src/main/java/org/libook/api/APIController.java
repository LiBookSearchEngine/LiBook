package org.libook.api;

import org.libook.fileHandlers.FileHandler;
import org.libook.fileHandlers.LocalFileHandler;
import org.libook.fillers.DocumentFiller;
import org.libook.model.Book;

import static spark.Spark.*;

public class APIController {
    private final FileHandler fileHandler;
    private final DocumentFiller documentFiller;

    public APIController() throws Exception {
        fileHandler = new LocalFileHandler();
        documentFiller = new DocumentFiller();
    }

    public void run() {
        port(80);
        postDocument();
    }

    private void postDocument() {
        post("/post", (req, res) -> {
            String name = req.queryParams("name");
            String author = req.queryParams("author");
            String language = req.queryParams("language");
            String date = req.queryParams("date");
            String content = req.body();

            content = documentFiller.fillDocument(new Book(name, author, language, date, content));
            fileHandler.saveDocument(content, name);

            return "Added and enqueued book " + name;
        });
    }
}
