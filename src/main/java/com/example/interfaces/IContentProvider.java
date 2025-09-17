package com.example.interfaces;

import java.util.List;

/**
 * Interface for providing content from a source.
 */
public interface IContentProvider {
    /**
     * Reads content from a source.
     *
     * @return List of strings representing the content lines
     */
    List<String> readContent();
}



