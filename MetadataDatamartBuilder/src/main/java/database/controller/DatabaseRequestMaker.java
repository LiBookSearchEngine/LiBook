package database.controller;

public interface DatabaseRequestMaker {
    StringBuilder make(String path, String query);
}
