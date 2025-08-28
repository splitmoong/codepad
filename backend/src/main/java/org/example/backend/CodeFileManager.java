package org.example.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * A utility class for managing code files, directly translating the logic
 * from your createCodeFile.js and removeCodeFile.js.
 */
public class CodeFileManager {

    // defining directories for code and output files.
    // gets the system's temporary directory
    private static final Path CODES_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "codes");
    private static final Path OUTPUTS_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "outputs");

    /**
     * Creates a new code file with a unique name and writes content to it.
     * This is the Java equivalent of the 'createCodeFile' function.
     *
     * @param language The file extension (e.g., "java", "js").
     * @param code     The string content to write to the file.
     * @return A CodeFile object containing the details of the created file.
     * @throws IOException if an I/O error occurs during file or directory creation.
     */
    public static CodeFile createCodeFile(String language, String code) throws IOException {
        // ensuring base directories exist
        if (!Files.exists(CODES_DIR)) {
            Files.createDirectories(CODES_DIR);
        }
        if (!Files.exists(OUTPUTS_DIR)) {
            Files.createDirectories(OUTPUTS_DIR);
        }

        // generate a unique id.
        final String jobID = UUID.randomUUID().toString();
        final String fileName = jobID + "." + language;
        final Path filePath = CODES_DIR.resolve(fileName); // Safely join path segments

        // write code to the file
        Files.writeString(filePath, code);

        return new CodeFile(fileName, filePath, jobID);
    }

    /**
     * Removes a code file and its corresponding output file.
     * This is the Java equivalent of the 'removeCodeFile' function.
     *
     * @param uuid      The unique ID of the job.
     * @param lang      The language/extension of the code file.
     * @param outputExt The extension of the output file to be deleted (can be null).
     */
    public static void removeCodeFile(String uuid, String lang, String outputExt) {
        final Path codeFile = CODES_DIR.resolve(uuid + "." + lang);

        try {
            Files.deleteIfExists(codeFile);
        } catch (IOException e) {
            // log the error
            System.err.println("Failed to delete code file: " + codeFile);
            e.printStackTrace();
        }

        if (outputExt != null && !outputExt.trim().isEmpty()) {
            final Path outputFile = OUTPUTS_DIR.resolve(uuid + "." + outputExt);
            try {
                Files.deleteIfExists(outputFile);
            } catch (IOException e) {
                System.err.println("Failed to delete output file: " + outputFile);
                e.printStackTrace();
            }
        }
    }
}
