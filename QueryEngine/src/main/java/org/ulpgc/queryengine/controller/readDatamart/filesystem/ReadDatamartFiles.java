package org.ulpgc.queryengine.controller.readDatamart.filesystem;

import org.ulpgc.queryengine.controller.readDatalake.CleanerAPIClient;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ReadDatamartFiles implements DatamartReaderFiles {
    private static String datamartPath;
    private static CleanerAPIClient cleanerAPIClient;

    public ReadDatamartFiles(String datamartPath, CleanerAPIClient client){
        this.datamartPath = datamartPath;
        this.cleanerAPIClient = client;
    }

    @Override
    public List<WordDocuments> getDocumentsWord(String param){
        List<WordDocuments> documents = new ArrayList<>();

        String[] words= param.split("\\+");

        for (String word : words){
            List<String> idDocuments = get_documents(word);
            WordDocuments wordDocuments = new WordDocuments(word, idDocuments);
            documents.add(wordDocuments);

        }
        return documents;
    }

    @Override
    public List<String> get_documents(String word){
        word = word.toLowerCase();
        String filePath = datamartPath + "/" + word;
        List<String> lines;

        try {
            Path path = Paths.get(filePath);
            lines = Files.readAllLines(path);

        } catch (IOException e) {
            e.printStackTrace();
            lines = null;
        }
        return lines;
    }

    @Override
    public List<RecommendBook> getRecommendBook(String phrase) {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(phrase);
        Map<Object, Integer> idCountMap = new HashMap<>();
        Map<Object, String> idTitleMap = new HashMap<>();

        for (WordDocuments wordDocuments : wordDocumentsList) {
            for (Object id : wordDocuments.documentsId()) {
                idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);
                String title = getTitleForId(id);
                idTitleMap.put(id, title);
            }
        }

        int maxCount = 0;
        List<RecommendBook> mostRecommendedBooks = new ArrayList<>();

        return mostRecommendedBooks;
    }

    private static String getTitleForId(Object id) {
        try {
            MetadataBook metadataBook = cleanerAPIClient.getMetadata((String) id);
            return metadataBook.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not found";
        }
    }

    @Override
    public WordFrequency getFrequency(String word){
        List<WordDocuments> wordDocumentsList = getDocumentsWord(word);

        int frequency = 0;
        for (WordDocuments wordDocuments : wordDocumentsList) {
            frequency += wordDocuments.documentsId().size();
        }

        return new WordFrequency(word, frequency);
    }


}
