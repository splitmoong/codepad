package org.example.backend.records;

import com.fasterxml.jackson.annotation.JsonProperty;

//this is the Java equivalent of the request body. a record to hold the request data

public record CodeRequest(
        @JsonProperty("language") String language,
        @JsonProperty("code") String code,
        @JsonProperty("input") String input
) {}
