package org.example.backend.controller;

import org.example.backend.records.CodeRequest;
import org.example.backend.records.ExecutionResult;
import org.example.backend.runner.Orchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CodeExecutionController {

    private static final Logger logger = LoggerFactory.getLogger(CodeExecutionController.class);

    private final Orchestrator orchestrator;

    public CodeExecutionController(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/execute")
    public ResponseEntity<ExecutionResult> executeCode(@RequestBody CodeRequest codeRequest) {
        try {
            ExecutionResult result = orchestrator.processCodeExecution(codeRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error during code execution: ", e);

            ExecutionResult errorResult = new ExecutionResult(
                    "",
                    "An internal server error occurred. Please check server logs.",
                    codeRequest.language(),
                    ""
            );
            return ResponseEntity.status(500).body(errorResult);
        }
    }
}
