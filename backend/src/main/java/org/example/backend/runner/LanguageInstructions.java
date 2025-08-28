package org.example.backend.runner;

import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/**
 * Provides language-specific commands for code compilation and execution.
 * This is the Java equivalent of 'instructions.js'.
 */
public class LanguageInstructions {

    private static final String CODES_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "codes").toString();
    private static final String OUTPUTS_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "outputs").toString();

    public static final Set<String> SUPPORTED_LANGUAGES = Set.of("java", "cpp", "py", "c");

    /**
     * Gets the execution and compilation commands for a given language.
     *
     * @param language The programming language.
     * @param jobID    The unique ID for the job, used to construct file paths.
     * @return An ExecutionCommands object with all necessary command details.
     */
    public static ExecutionCommands getCommands(String language, String jobID) {
        switch (language) {
            case "java":
                return new ExecutionCommands(
                        null, null,
                        "java", List.of(Paths.get(CODES_DIR, jobID + ".java").toString()),
                        null,
                        "java --version"
                );
            case "cpp":
                return new ExecutionCommands(
                        "g++", List.of(Paths.get(CODES_DIR, jobID + ".cpp").toString(), "-o", Paths.get(OUTPUTS_DIR, jobID + ".out").toString()),
                        Paths.get(OUTPUTS_DIR, jobID + ".out").toString(), null,
                        "out",
                        "g++ --version"
                );
            case "py":
                return new ExecutionCommands(
                        null, null,
                        "python3", List.of(Paths.get(CODES_DIR, jobID + ".py").toString()),
                        null,
                        "python3 --version"
                );
            case "c":
                return new ExecutionCommands(
                        "gcc", List.of(Paths.get(CODES_DIR, jobID + ".c").toString(), "-o", Paths.get(OUTPUTS_DIR, jobID + ".out").toString()),
                        Paths.get(OUTPUTS_DIR, jobID + ".out").toString(), null,
                        "out",
                        "gcc --version"
                );
            default:
                throw new IllegalArgumentException("Language not supported: " + language);
        }
    }
}
