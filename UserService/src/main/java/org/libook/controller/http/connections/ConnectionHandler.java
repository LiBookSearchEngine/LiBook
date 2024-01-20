package org.libook.controller.http.connections;

import org.libook.model.Book;

public interface ConnectionHandler {
    String makeUrlRequest(String path, Book book);
}
