package org.ulpgc.queryengine.controller.readMetadata;

public interface DatabaseRequestMaker {
    StringBuilder make(String path, String query);
}
