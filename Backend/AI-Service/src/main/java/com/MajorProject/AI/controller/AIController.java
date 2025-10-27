package com.MajorProject.AI.controller;

import com.MajorProject.AI.Service.AIService;
import com.MajorProject.AI.Util.helperForAI;
import com.MajorProject.common.Domain.AIRequest;
import com.MajorProject.common.Domain.AIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.generate.OllamaStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ai")
public class AIController {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final OllamaAPI ollamaAPI;
    private final helperForAI helperForAI;
    private final ObjectMapper objectMapper;

    @Autowired
    private AIService aiService;

    public AIController(@Value("${spring.ai.ollama.base-url}") String ollamaBaseUrl, helperForAI helperForAI,ObjectMapper objectMapper) {
        this.ollamaAPI = new OllamaAPI(ollamaBaseUrl);
        this.ollamaAPI.setRequestTimeoutSeconds(300000);
        this.helperForAI = helperForAI;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/message_stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamResponse(@RequestParam("query") String query) {
//        System.out.println("Received query: " + query);

        SseEmitter emitter = new SseEmitter(0L);
        ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

        heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (IOException e) {
                System.out.println("Heartbeat error: " + e.getMessage());
                emitter.completeWithError(e);
                heartbeatExecutor.shutdown();
            }
        }, 15, 15, TimeUnit.SECONDS);

        Runnable shutdown = () -> {
            heartbeatExecutor.shutdown();
            emitter.complete();
        };

        emitter.onError(e -> {
            System.out.println("Emitter error: " + e.getMessage());
            shutdown.run();
        });

        executor.execute(() -> {
            try {
                String prompt = "You are a helpful health AI: " + query;

                OllamaChatRequest chatRequest = OllamaChatRequestBuilder.getInstance("mistral")
                        .withMessage(OllamaChatMessageRole.USER, prompt)
                        .build();

                OllamaStreamHandler streamHandler = token -> {
                    try {
                        emitter.send(SseEmitter.event().data(token).name("message"));
                        System.out.print(token); // Print tokens live
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        heartbeatExecutor.shutdown();
                    }
                };
                ollamaAPI.chat(chatRequest, streamHandler);
                shutdown.run();
            } catch (Exception e) {
                System.out.println("Streaming exception: " + e.getMessage());
                emitter.completeWithError(e);
                heartbeatExecutor.shutdown();
            }
        });
        return emitter;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AIResponse> analyze(@RequestBody AIRequest request) {
        try {
            if (request.getReportContent() == null || request.getReportContent().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new AIResponse("Report content is empty", Collections.emptyList(), null));
            }

            List<String> chunks = helperForAI.chunkText(request.getReportContent(), 2000);
            List<AIResponse> responses = new ArrayList<>();

            for (String chunk : chunks) {
                try {
                    String prompt = """
                        Analyze the following patient medical report chunk and respond in **valid JSON** with:
                        - summary: A 5-7 line summary of the patient's current health.
                        - suggestions: List of practical steps to improve health, all strings quoted.
                        - healthScore: Number 0-100.
                        
                        Report chunk:
                        """ + chunk;

                    String fullJson = helperForAI.callOllama(prompt);

                    Map<String, Object> map = objectMapper.readValue(fullJson, Map.class);
                    Map<String, Object> message = (Map<String, Object>) map.get("message");
                    String innerContent = (String) message.get("content");

                    // Safe parsing of AI JSON
                    AIResponse aiResponse = helperForAI.parseAIResponseSafely(innerContent);
                    responses.add(aiResponse);

                } catch (HttpTimeoutException timeoutEx) {
                    responses.add(new AIResponse(
                            "Timeout while processing this chunk.",
                            List.of("Try analyzing a smaller portion or increase timeout."),
                            null
                    ));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    responses.add(new AIResponse(
                            "Error analyzing this chunk.",
                            List.of("Check the input or system logs."),
                            null
                    ));
                }
            }

            // Aggregate final response
            String fullSummary = responses.stream()
                    .map(AIResponse::getSummary)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n\n"));

            List<String> allSuggestions = responses.stream()
                    .map(AIResponse::getSuggestions)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            Double avgScore = responses.stream()
                    .map(AIResponse::getHealthScore)
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);

            AIResponse finalResponse = new AIResponse(fullSummary, allSuggestions, avgScore);
            System.out.println("sending final response : " + finalResponse);
            return ResponseEntity.ok(finalResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new AIResponse("Internal server error during analysis", Collections.emptyList(), null));
        }
    }

    @PostMapping("/analyze/feedback")
    public Map<Long, AIResponse> analyzeFeedbacks(@RequestBody Map<Long, List<String>> doctorFeedbackMap) {
        Map<Long, AIResponse> results = new HashMap<>();

        for (Map.Entry<Long, List<String>> entry : doctorFeedbackMap.entrySet()) {
            Long doctorId = entry.getKey();
            List<String> feedbacks = entry.getValue();

            String joinedFeedback = String.join(". ", feedbacks);

            AIResponse analysis = aiService.analyzeFeedback(joinedFeedback);
            System.out.println("analysis : "+ analysis);
            results.put(doctorId, analysis);
        }

        return results;
    }
}