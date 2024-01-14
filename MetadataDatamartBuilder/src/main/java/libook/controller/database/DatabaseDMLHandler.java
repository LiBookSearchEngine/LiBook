package libook.controller.database;

import libook.model.MetadataBook;

public interface DatabaseDMLHandler {
    void createBooksTable();
    void insertMetadata(MetadataBook book);
}
