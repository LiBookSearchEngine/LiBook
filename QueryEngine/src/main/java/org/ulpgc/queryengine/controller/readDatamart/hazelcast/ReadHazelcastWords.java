package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatalake.CleanerAPIClient;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadGoogleCloudObjects;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReadHazelcastWords implements DatamartReaderFiles {
    private final IMap<String, List<String>> hazelcastMap;
    private final ReadGoogleCloudObjects readGoogleCloudObjects;
    private static CleanerAPIClient cleanerAPIClient;

    public ReadHazelcastWords(HazelcastInstance hazelcastInstance, CleanerAPIClient client){
        this.hazelcastMap = hazelcastInstance.getMap("datamart");
        this.cleanerAPIClient = client;
        this.readGoogleCloudObjects = new ReadGoogleCloudObjects(client);
    }

    @Override
    public List<String> get_documents(String word) {
        word = word.toLowerCase();
        List<String> documents = hazelcastMap.get(word);

        if (documents == null) {
            try {
                documents = readGoogleCloudObjects.get_documents(word);
                hazelcastMap.put(word, documents);
            } catch (ObjectNotFoundException e) {
                return new ArrayList<>();
            }
        }

        return documents;
    }

    @Override
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

    @Override
    public List<RecommendBook> getRecommendBook(String phrase) {
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

        return mostRecommendedBooks.stream()
                .map(id -> new RecommendBook(getTitleForId(id), id))
                .collect(Collectors.toList());
    }

    @Override
    public WordFrequency getFrequency(String word) {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(word);

        int frequency = 0;
        for (WordDocuments wordDocuments : wordDocumentsList) {
            frequency += wordDocuments.documentsId().size();
        }

        return new WordFrequency(word, frequency);
    }

    private static String getTitleForId(String id) {
        try {
            String name = id.substring(0, id.length() - 4);
            MetadataBook metadataBook = cleanerAPIClient.getMetadata(name);
            return metadataBook.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not found";
        }
    }
}
