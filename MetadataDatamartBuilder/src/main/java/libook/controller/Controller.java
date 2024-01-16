package libook.controller;

import libook.Main;
import libook.controller.database.DatabaseDMLHandler;
import libook.controller.http.clients.CleanerAPIClient;
import libook.controller.message.EventConsumer;
import libook.model.MetadataBook;

import javax.jms.JMSException;
import java.io.IOException;
import java.nio.file.Path;

public class Controller {
    private final CleanerAPIClient client;
    private final DatabaseDMLHandler dmlHandler;

    public Controller(DatabaseDMLHandler dmlHandler) {
        this.client = new CleanerAPIClient();
        this.dmlHandler = dmlHandler;
    }

    public void run() throws JMSException, IOException {
        EventConsumer cleanerEvents = new EventConsumer(Main.SERVER_MQ_PORT,
                "cleanerMetadataEvents",
                Main.SERVER_API_URL);

        while (true) {
            String book = cleanerEvents.getMessage();
            System.out.println(book);
            Path bookPath = Path.of(book);
            String bookId = bookPath.getFileName().toString().split(".txt")[0];
            MetadataBook metadata = client.getMetadata(bookId);
            dmlHandler.insertMetadata(metadata);
        }
    }
}
