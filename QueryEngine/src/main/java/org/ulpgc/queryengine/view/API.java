package org.ulpgc.queryengine.view;

import com.google.gson.Gson;
import org.ulpgc.queryengine.controller.readDatalake.CleanerAPIClient;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadCloud;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastStats;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastWords;
import org.ulpgc.queryengine.model.*;

import java.io.IOException;
import java.util.List;

import static spark.Spark.*;
import static spark.Spark.port;

public class API {

    private static ReadHazelcastWords readHazelcastWords;
    private static ReadHazelcastStats readHazelcastStats;
    private static CleanerAPIClient cleanerAPIClient;

    public static void runAPI(DatamartReaderFiles obtainFiles, DatamartCalculateStats obtainStats, int port, CleanerAPIClient client) throws IOException {
        readHazelcastWords = (ReadHazelcastWords) obtainFiles;
        readHazelcastStats = (ReadHazelcastStats) obtainStats;
        ReadCloud.obtain_credentials();
        cleanerAPIClient = client;
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
            List<WordDocuments> documents = readHazelcastWords.getDocumentsWord(word);
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
            List<RecommendBook> book = readHazelcastWords.getRecommendBook(phrase);
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



}
