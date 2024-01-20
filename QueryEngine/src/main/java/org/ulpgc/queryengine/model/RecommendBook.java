package org.ulpgc.queryengine.model;

import java.util.Map;

public class RecommendBook {
    private final Map<String, MetadataBook> metadataBook;
    public RecommendBook(Map<String, MetadataBook> metadataBook) {
        this.metadataBook = metadataBook;
    }
    public Map<String, MetadataBook> metadataBook(){
        return metadataBook;
    }
}
