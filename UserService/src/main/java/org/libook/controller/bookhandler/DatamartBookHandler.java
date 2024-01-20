package org.libook.controller.bookhandler;

import org.libook.model.Book;
import org.libook.model.BookInfo;
import org.libook.model.User;

import java.util.List;

public interface DatamartBookHandler {
    void addBookToUser(User user, Book book);
    List<BookInfo> getUserBooks(User user);
}
