package libook.controller.http.clients;

import libook.Main;
import libook.controller.extractors.MetadataExtractor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import libook.model.Book;
import libook.model.MetadataBook;

import java.io.IOException;

public class CleanerAPIClient {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private final String ip;
    private static String cleanerBaseUrl;

    public CleanerAPIClient(){
        this.ip = Main.SERVER_API_URL;
        baseUrl();
    }

    public void baseUrl(){
        cleanerBaseUrl = ip + ":" + Main.SERVER_CLEANER_PORT + "/datalake";
    }

    public MetadataBook getMetadata(String idBook) throws IOException {
        String url = cleanerBaseUrl + "/metadata/" + idBook;
        String text = executeRequest(url);
        return MetadataExtractor.extractMetadata(text);
    }

    private String executeRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

}
