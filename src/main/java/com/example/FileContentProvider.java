package com.example;

import com.example.interfaces.IContentProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IContentProvider that reads content from a file.
 */
public class FileContentProvider implements IContentProvider {
    private final String filePath;

    /**
     * Constructor for FileContentProvider.
     *
     * @param filePath The path to the file to read
     */
    public FileContentProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<String> readContent() {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return lines;
    }
}
