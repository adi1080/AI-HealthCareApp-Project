package com.MajorProject.AI.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Component
public class helperForAI {

    private final ObjectMapper objectMapper;

    public helperForAI(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String callOllama(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String requestBody = objectMapper.writeValueAsString(Map.of(
                "model", "mistral",
                "stream", false,
                "messages", List.of(Map.of(
                        "role", "user",
                        "content", prompt
                ))
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/chat"))
                .timeout(Duration.ofSeconds(120))  // per-request timeout
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("Ollama returned status: " + response.statusCode());
        }
    }

    public List<String> chunkText(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }
}
