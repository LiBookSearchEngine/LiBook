package libook.controller;

import libook.MetadataBuilderMain;
import libook.controller.database.DatabaseDMLHandler;
import libook.controller.http.clients.CleanerAPIClient;
import libook.controller.message.EventConsumer;
import libook.model.MetadataBook;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Controller {
    private final CleanerAPIClient client;
    private final DatabaseDMLHandler dmlHandler;

    public Controller(DatabaseDMLHandler dmlHandler) {
        this.client = new CleanerAPIClient();
        this.dmlHandler = dmlHandler;
    }

    public void run() {
        EventConsumer cleanerEvents = getCleanerMetadataEvents();

        while (true) {
            String book = cleanerEvents.getMessage();
            try {
                Path bookPath = Path.of(book);
                String bookId = bookPath.getFileName().toString().split(".txt")[0];
                MetadataBook metadata = client.getMetadata(bookId);
                dmlHandler.insertMetadata(metadata);
                System.out.println("Inserted book " + book + " to metadata datamart");
            } catch (Exception e) {
                if (book != null)
                    System.out.println("Error found when storing " + book + " metadata");

                cleanerEvents = getCleanerMetadataEvents();
            }
        }
    }

    @NotNull
    private EventConsumer getCleanerMetadataEvents() {
        try {
            Thread.sleep(5000);
            return new EventConsumer(MetadataBuilderMain.SERVER_MQ_PORT,
                    "cleanerMetadataEvents",
                    MetadataBuilderMain.SERVER_API_URL);

        } catch (Exception e) {
            return getCleanerMetadataEvents();
        }
    }
}
