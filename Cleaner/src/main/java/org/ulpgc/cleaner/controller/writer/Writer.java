package org.ulpgc.cleaner.controller.writer;

import com.google.gson.Gson;
import org.ulpgc.cleaner.controller.process.ParseFiles;
import org.ulpgc.cleaner.model.WriteEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public class Writer {

    private final Path pathContent;
    private final Path pathEvent;
    private final Path pathMetadata;

    public Writer(Path pathContent, Path pathMetadata, Path pathEvent) {
        this.pathContent = pathContent;
        this.pathEvent = pathEvent;
        this.pathMetadata = pathMetadata;
    }

    public void writeToDatalake(String text, String idBook){
        try {
            FileWriter fileWriter = new FileWriter(pathContent + "/" + idBook);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(text);
            writer.close();
            fileWriter.close();
            System.out.println("Saved the book " + idBook + " at " + pathContent + "/" + idBook);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeMetadataToDatalake(String text, String idBook){
        try {
            FileWriter fileWriter = new FileWriter((pathMetadata +  "/" + getName(idBook) + ".metadata"), true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(text);
            writer.close();
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeEvent(String idBook){
        FileWriter file = null;
        try {
            file = new FileWriter(pathEvent +  "/" + getName(idBook) + ".event" );
            WriteEvent event = new WriteEvent(new Date(), idBook);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write((new Gson()).toJson(event));

            writer.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getName(String idBook) {
        return idBook.split(".txt")[0];
    }
}
