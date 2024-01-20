package org.ulpgc.queryengine.view;

import com.google.gson.Gson;
import org.ulpgc.queryengine.controller.readDatalake.CleanerAPIClient;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadCloud;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastStats;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastWords;
import org.ulpgc.queryengine.controller.readMetadata.readrqlite.RqliteQuery;
import org.ulpgc.queryengine.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.port;

public class API {

    private static ReadHazelcastWords readHazelcastWords;
    private static ReadHazelcastStats readHazelcastStats;
    private static CleanerAPIClient cleanerAPIClient;
    private static RqliteQuery rqliteQuery;

    public static void runAPI(ReadHazelcastWords obtainFiles, ReadHazelcastStats obtainStats, int port, CleanerAPIClient client) throws IOException {
        readHazelcastWords = obtainFiles;
        readHazelcastStats = obtainStats;
        ReadCloud.obtain_credentials();
        cleanerAPIClient = client;
        rqliteQuery = new RqliteQuery();
        port(port);
        getTotalWords();
        getLen();
        getWordDocuments();
        getPhrase();
        getRecommendBook();
        getFrequencyWord();
        getMetadata();
        getRawBook();
        getContent();
        getByAuthor();
        getByLanguage();
    }

    public static void getTotalWords(){
        get("stats/total", (req, res) -> readHazelcastStats.totalWords());
    }

    public static void getLen(){
        get("stats/length/:number", (req, res) -> {
            String number = req.params("number");
            return readHazelcastStats.wordLength(number);
        });
    }

    private static void getWordDocuments() {
        get("/datamart/:word", (req, res) -> {
            String word = req.params("word");
            List<Object> documents = readHazelcastWords.getWord(word);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getPhrase() {
        get("datamart-search/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<WordDocuments> documents = readHazelcastWords.getDocumentsWord(phrase);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getRecommendBook(){
        get("datamart-recommend/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<Object> book = readHazelcastWords.getRecommendBook(phrase);
            return (new Gson()).toJson(book);
        });
    }

    private static void getFrequencyWord(){
        get("/stats/datamart-frequency/:word", (req, res) -> {
            String word = req.params("word");
            WordFrequency frequency = readHazelcastWords.getFrequency(word);
            return (new Gson()).toJson(frequency);
        });
    }

    private static void getMetadata() {
        get("/datalake/metadata/:idBook", (req, res) -> {
            String idBook = req.params(":idBook");
            return (new Gson()).toJson(cleanerAPIClient.getMetadata(idBook));
        });
    }

    private static void getContent() {
        get("/datalake/content/:idBook", (req, res) -> {
            String idBook = req.params(":idBook");
            Book book = cleanerAPIClient.getContent(idBook);
            return new Gson().toJson(book);
        });
    }

    private static void getRawBook() {
        get("/datalake/books/:idBook", (req, res) -> {
            String idBook = req.params(":idBook");
            Book book = cleanerAPIClient.getRawBook(idBook);
            return new Gson().toJson(book);
        });
    }

    private static void getByAuthor(){
        get("/metadata/author/:author", (req, res) -> {
            String author = req.params(":author");
            Map<String, MetadataBook> books = rqliteQuery.selectByAuthor(author);
            return new Gson().toJson(books);
        });
    }

    private static void getByLanguage(){
        get("/metadata/language/:language", (req, res) -> {
            String author = req.params(":language");
            Map<String, MetadataBook> books = rqliteQuery.selectByLanguage(author);
            return new Gson().toJson(books);
        });
    }


}
