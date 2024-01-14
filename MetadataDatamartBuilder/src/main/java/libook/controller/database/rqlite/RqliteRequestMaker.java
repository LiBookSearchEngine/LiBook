package libook.controller.database.rqlite;

import libook.controller.database.DatabaseRequestMaker;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RqliteRequestMaker implements DatabaseRequestMaker {
    private static final String RQLITE_URL = "http://localhost:4001";
    private static RqliteRequestMaker instance;

    public RqliteRequestMaker() {
        this.instance = this;
    }

    public static RqliteRequestMaker getInstance() {
        return instance;
    }

    @Override
    public StringBuilder make(String path, String query) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            String apiUrl = RQLITE_URL + "/db/" + path;

            String jsonBody = "[\"" + query + "\"]";
            HttpPost httpPost = getHttpPost(apiUrl, jsonBody);
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println("Query Result: " + result.toString());
                    return result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpPost getHttpPost(String apiUrl, String jsonBody) {
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setEntity(new StringEntity(jsonBody, "UTF-8"));
        httpPost.setHeader("Content-Type", "application/json");
        return httpPost;
    }
}
