package org.example.backend.records;

/**
 * A data-holding record for the result of a code execution.
 *
 * @param output   The standard output from the executed code.
 * @param error    The standard error from the executed code or compilation.
 * @param language The language of the executed code.
 * @param info     Version information for the compiler or runtime.
 */
public record ExecutionResult(String output, String error, String language, String info) {
}
