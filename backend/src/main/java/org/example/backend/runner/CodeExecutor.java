package org.example.backend.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.example.backend.CodeFile;
import org.example.backend.CodeFileManager;

/**
 * Handles the logic for compiling and running code in various languages.
 * This is the Java equivalent of 'index.js' and 'info.js'.
 */
public class CodeExecutor {

    private static final int TIMEOUT_SECONDS = 30;

    /**
     * Runs a given piece of code in a specified language.
     *
     * @param language The programming language.
     * @param code     The source code to execute.
     * @param input    The standard input to be passed to the code.
     * @return An ExecutionResult containing the output and errors.
     * @throws Exception if any part of the process fails.
     */
    public ExecutionResult runCode(String language, String code, String input) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("No code found to execute.");
        }
        if (!LanguageInstructions.SUPPORTED_LANGUAGES.contains(language)) {
            throw new IllegalArgumentException("Language not supported: " + language);
        }

        CodeFile codeFile = null;
        try {
            codeFile = CodeFileManager.createCodeFile(language, code);
            ExecutionCommands commands = LanguageInstructions.getCommands(language, codeFile.jobID());

            // compile
            if (commands.compileCodeCommand() != null) {
                ProcessResult compileResult = executeProcess(commands.compileCodeCommand(), commands.compilationArgs(), null, TIMEOUT_SECONDS);
                if (!compileResult.error.isEmpty()) {
                    return new ExecutionResult("", compileResult.error, language, getInfo(language));
                }
            }

            // execute
            ProcessResult executeResult = executeProcess(commands.executeCodeCommand(), commands.executionArgs(), input, TIMEOUT_SECONDS);
            String info = getInfo(language);

            return new ExecutionResult(executeResult.output, executeResult.error, language, info);

        } finally {
            // cleanup
            if (codeFile != null) {
                ExecutionCommands commands = LanguageInstructions.getCommands(language, codeFile.jobID());
                CodeFileManager.removeCodeFile(codeFile.jobID(), language, commands.outputExt());
            }
        }
    }

    /**
     * Gets version information for a language's compiler or runtime.
     *
     * @param language The language to check.
     * @return The version information string.
     */
    private String getInfo(String language) {
        try {
            ExecutionCommands commands = LanguageInstructions.getCommands(language, ""); // jobID is not needed for info
            ProcessResult infoResult = executeProcess(commands.compilerInfoCommand(), null, null, 5);
            return infoResult.output.isEmpty() ? infoResult.error : infoResult.output;
        } catch (Exception e) {
            return "Could not retrieve version info.";
        }
    }

    /**
     * A helper method to execute a system process.
     */
    private ProcessResult executeProcess(String command, List<String> args, String input, int timeout) throws IOException, InterruptedException, TimeoutException {
        List<String> commandAndArgs = new ArrayList<>();
        commandAndArgs.add(command);
        if (args != null) {
            commandAndArgs.addAll(args);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commandAndArgs);
        Process process = processBuilder.start();

        // write input to the process if provided
        if (input != null && !input.isEmpty()) {
            try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
                writer.write(input);
            }
        }

        // read stdout and stderr streams
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
        new Thread(outputGobbler).start();
        new Thread(errorGobbler).start();

        boolean finishedInTime = process.waitFor(timeout, TimeUnit.SECONDS);

        if (!finishedInTime) {
            process.destroyForcibly();
            throw new TimeoutException("Process timed out after " + timeout + " seconds.");
        }

        return new ProcessResult(outputGobbler.getContent(), errorGobbler.getContent());
    }

    // Helper record to hold process results
    private record ProcessResult(String output, String error) {}

    // Helper class to consume process streams concurrently
    private static class StreamGobbler implements Runnable {
        private final java.io.InputStream inputStream;
        private final StringBuilder content = new StringBuilder();

        public StreamGobbler(java.io.InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                //handle exception
            }
        }

        public String getContent() {
            return content.toString();
        }
    }
}
