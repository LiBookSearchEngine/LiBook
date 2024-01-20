package org.ulpgc.indexer.controller.processes.readers;

import com.google.gson.Gson;
import org.ulpgc.indexer.Main;
import org.ulpgc.indexer.controller.message.Consumer;
import org.ulpgc.indexer.controller.message.Publisher;
import org.ulpgc.indexer.controller.message.broker.EventConsumer;
import org.ulpgc.indexer.controller.message.broker.EventPublisher;
import org.ulpgc.indexer.controller.readers.ContentReader;
import org.ulpgc.indexer.model.FileEvent;

import javax.jms.JMSException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public class AsynchronousReaderThread extends Thread {
    private final String contentPath;
    private final String eventPath;
    private final String indexerId;
    private Consumer eventConsumer;
    private final Publisher eventPublisher;
    private final String apiUrl;

    public AsynchronousReaderThread(String contentPath, String eventPath, String apiUrl, String indexerId) {
        this.contentPath = contentPath;
        this.eventPath = eventPath;
        this.apiUrl = apiUrl;
        this.indexerId = indexerId;
        this.eventConsumer = getCleanerConsumer();
        this.eventPublisher = getPublisher();
    }

    public void run() {
        readIncomingContent();
    }

    private void readIncomingContent() {
        while (true) {
            String file = eventConsumer.getMessage();
            try {
                System.out.println("Reading file " + file);
                addToEvents(Path.of(file));
                addToContent(Path.of(file));
                eventPublisher.publish(file);
                System.out.println("File " + file + " read");
                Thread.sleep(5000);
            } catch (Exception e) {
                eventConsumer = getCleanerConsumer();
            }
        }
    }

    private EventConsumer getCleanerConsumer() {
        try {
            Thread.sleep(5000);
            return new EventConsumer(Integer.toString(Main.SERVER_MQ_PORT), "cleanerEvents", apiUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return getCleanerConsumer();
        }
    }

    private EventPublisher getPublisher() {
        try {
            Thread.sleep(5000);
            return new EventPublisher(Integer.toString(Main.SERVER_MQ_PORT),
                    "readEvents" + indexerId,
                    apiUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return getPublisher();
        }
    }

    private void addToEvents(Path filePath) throws IOException {
        FileWriter file = new FileWriter(eventPath + "/" + filePath.getFileName());
        BufferedWriter writer = new BufferedWriter(file);

        FileEvent event = new FileEvent(new Date(), filePath.getFileName());
        writer.write((new Gson()).toJson(event));

        writer.close();
        file.close();
    }

    private void addToContent(Path filePath) throws Exception {
        FileWriter file = new FileWriter(contentPath + "/" + filePath.getFileName());
        BufferedWriter writer = new BufferedWriter(file);

        String fileId = filePath.getFileName().toString().split(".txt")[0];
        String content = ContentReader.readContentFromAPI(apiUrl + ":" + Main.SERVER_CLEANER_PORT + "/datalake/content/" + fileId);
        writer.write(content);

        writer.close();
        file.close();
    }
}
