package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.ulpgc.queryengine.controller.readDatalake.CleanerAPIClient;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadGoogleCloudObjects;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteQuery;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReadHazelcastWords {
    private final IMap<String, List<String>> hazelcastMap;
    private final ReadGoogleCloudObjects readGoogleCloudObjects;
    private static CleanerAPIClient cleanerAPIClient;

    public ReadHazelcastWords(HazelcastInstance hazelcastInstance, CleanerAPIClient client){
        this.hazelcastMap = hazelcastInstance.getMap("datamart");
        insertExamples();
        this.cleanerAPIClient = client;
        this.readGoogleCloudObjects = new ReadGoogleCloudObjects(client);
    }

    private void insertExamples(){
        List<String> books = new ArrayList<>();
        books.add("1");
        books.add("2");
        hazelcastMap.put("hand", books);
        List<String> books2 = new ArrayList<>();
        books2.add("1");
        books2.add("61415");
        hazelcastMap.put("job", books2);
        List<String> books3 = new ArrayList<>();
        books3.add("6085");
        books3.add("39773");
        hazelcastMap.put("code", books3);
    }

    public List<String> get_documents(String word) {
        word = word.toLowerCase();
        List<String> documents = hazelcastMap.get(word);

        return documents;
    }

    public List<WordDocuments> getDocumentsWord(String param) {
        List<WordDocuments> documents = new ArrayList<>();

        String[] words= param.split("\\+");

        for (String word : words){
            List<String> idDocuments = get_documents(word);
            WordDocuments wordDocuments = new WordDocuments(word, idDocuments);
            documents.add(wordDocuments);

        }
        return documents;
    }

    public List<Object> getRecommendBook(String phrase) {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(phrase);

        Map<String, Long> idCountMap = wordDocumentsList.stream()
                .flatMap(wordDocuments -> wordDocuments.documentsId().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxCount = idCountMap.values().stream()
                .max(Long::compare)
                .orElse(0L);

        List<String> mostRecommendedBooks = idCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Object> recommendBooks = new ArrayList<>();

        for (String id : mostRecommendedBooks) {
            Map<String, MetadataBook> metadataBookMap = getTitleForId(id);
            recommendBooks.add(metadataBookMap);
        }

        return recommendBooks;
    }

    public List<Map<String, MetadataBook>> getWord(String word){
        List<WordDocuments> wordDocumentsList = getDocumentsWord(word);
        List<String> idBook = wordDocumentsList.get(0).documentsId();
        List<Map<String, MetadataBook>> recommendBooks = new ArrayList<>();
        for(String id: idBook){
            Map<String, MetadataBook> metadataBookMap = getTitleForId(id);
            recommendBooks.add(metadataBookMap);
        }
;
        return recommendBooks;
    }

    public WordFrequency getFrequency(String word) {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(word);

        int frequency = 0;
        for (WordDocuments wordDocuments : wordDocumentsList) {
            frequency += wordDocuments.documentsId().size();
        }

        return new WordFrequency(word, frequency);
    }

    private static Map<String, MetadataBook> getTitleForId(String id) {
        RqliteQuery rqliteQuery = new RqliteQuery();
        return rqliteQuery.selectById(id);
    }
}
