package org.example.backend.records;

import java.util.List;

/**
 * A data-holding record for language-specific compilation and execution commands.
 * This is the Java equivalent of the object returned by the commandMap function.
 *
 * @param compileCodeCommand    The command to compile the code (e.g., "g++"). Can be null if not a compiled language.
 * @param compilationArgs       The arguments for the compilation command.
 * @param executeCodeCommand    The command to execute the code (e.g., "java", "./a.out").
 * @param executionArgs         The arguments for the execution command.
 * @param outputExt             The file extension of the compiled output (e.g., "out", "exe"). Can be null.
 * @param compilerInfoCommand   The command to get the compiler/runtime version info.
 */
public record ExecutionCommands(
        String compileCodeCommand,
        List<String> compilationArgs,
        String executeCodeCommand,
        List<String> executionArgs,
        String outputExt,
        String compilerInfoCommand
) {
}
