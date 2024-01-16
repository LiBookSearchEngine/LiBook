package org.ulpgc.queryengine.controller.readMetadata.readrqlite;

import org.ulpgc.queryengine.controller.readMetadata.DatabaseQuery;
import org.ulpgc.queryengine.controller.readMetadata.DatabaseRequestMaker;
import org.ulpgc.queryengine.model.MetadataBook;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RqliteQuery implements DatabaseQuery {
    private final DatabaseRequestMaker requestMaker;

    public RqliteQuery() {
        this.requestMaker = RqliteRequestMaker.getInstance();
    }


    public Map<String, MetadataBook> selectByLanguage(String language) {
        String query = String.format(
                "SELECT * FROM book_metadata WHERE language='%s'",
                language
        );

        StringBuilder queryResult = requestMaker.make("query", query);
        List<List<String>> values = findValues(queryResult.toString());
        return obtainInformation(values);

    }

    public Map<String, MetadataBook> selectByAuthor(String name) {
        String query = String.format(
                "SELECT * FROM book_metadata WHERE author='%s'",
                name
        );

        StringBuilder queryResult = this.requestMaker.make("query", query);
        List<List<String>> values = findValues(queryResult.toString());
        return obtainInformation(values);
    }

    private static List<List<String>> findValues(String query){
        List<List<String>> values = new ArrayList<>();

        Pattern pattern = Pattern.compile("\"values\":\\[([\\s\\S]*?)\\]}");
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String valuesArray = matcher.group(1);
            Pattern rowPattern = Pattern.compile("\\[([^\\]]*?)\\]");
            Matcher rowMatcher = rowPattern.matcher(valuesArray);

            while (rowMatcher.find()) {
                String row = rowMatcher.group(1);
                String[] rowValues = row.split(",");
                List<String> rowList = new ArrayList<>();

                for (String value : rowValues) {
                    rowList.add(value.trim());
                }

                values.add(rowList);
            }
        }
        return values;
    }

    private static Map<String, MetadataBook> obtainInformation(List<List<String>> values){
        Map<String, MetadataBook> metadataMap = new HashMap<>();

        for (List<String> row : values) {
            String id = row.get(0);
            String title = row.get(1).replace("\"", "");
            String author = row.get(2).replace("\"", "");
            String language = row.get(3).replace("\"", "");
            String releaseDate =  row.get(4).replace("\"", "");

            MetadataBook book = new MetadataBook(title, author, releaseDate, language);

            metadataMap.put(id, book);
        }

        return metadataMap;
    }
}
