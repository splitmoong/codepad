package org.example.backend.records;

import java.nio.file.Path;

/**
 * A data-holding record to store information about a created code file.
 * This is the Java equivalent of the JavaScript object returned by createCodeFile.
 *
 * @param fileName The generated name of the file (e.g., "uuid.java").
 * @param filePath The full, absolute path to the created file.
 * @param jobID    The unique identifier (UUID) for this file creation job.
 */
public record CodeFile(String fileName, Path filePath, String jobID) {
}
