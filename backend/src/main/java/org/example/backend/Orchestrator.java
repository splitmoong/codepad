package org.example.backend.runner;

import org.example.backend.records.CodeRequest;
import org.example.backend.records.ExecutionResult;
import org.springframework.stereotype.Service;


@Service
public class Orchestrator {

    private final CodeExecutor codeExecutor;

    public Orchestrator(CodeExecutor codeExecutor) {
        this.codeExecutor = codeExecutor;
    }

    public ExecutionResult processCodeExecution(CodeRequest request) throws Exception {
        return codeExecutor.runCode(request);
    }
}
