package libook.controller.database;

public interface DatabaseRequestMaker {
    StringBuilder make(String path, String query);
}
