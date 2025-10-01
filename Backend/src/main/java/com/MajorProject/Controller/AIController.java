package com.MajorProject.Controller;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.generate.OllamaStreamHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final OllamaAPI ollamaAPI;

    public AIController(@Value("${spring.ai.ollama.base-url}") String ollamaBaseUrl) {
        this.ollamaAPI = new OllamaAPI(ollamaBaseUrl);
    }

    @GetMapping(value = "/message_stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamResponse(@RequestParam("query") String query) {

//        System.out.println("Received query: " + query);

        SseEmitter emitter = new SseEmitter(0L);
        ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

        heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
//                System.out.println("Sent heartbeat");
            } catch (IOException e) {
                System.out.println("Heartbeat error: " + e.getMessage());
                emitter.completeWithError(e);
                heartbeatExecutor.shutdown();
            }
        }, 15, 15, TimeUnit.SECONDS);

        Runnable shutdown = () -> {
//            System.out.println("Shutting down heartbeat and completing emitter.");
            heartbeatExecutor.shutdown();
            emitter.complete();
        };

//        emitter.onCompletion(() -> System.out.println("Emitter completed"));
//        emitter.onTimeout(() -> System.out.println("Emitter timeout"));
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
//                        System.out.println("Streaming error: " + e.getMessage());
                        emitter.completeWithError(e);
                        heartbeatExecutor.shutdown();
                    }
                };

//                System.out.println("Calling Ollama API...");
                ollamaAPI.chat(chatRequest, streamHandler);

//                System.out.println("\nFinished streaming.");
                shutdown.run();

            } catch (Exception e) {
                System.out.println("Streaming exception: " + e.getMessage());
                emitter.completeWithError(e);
                heartbeatExecutor.shutdown();
            }
        });
        return emitter;
    }
}
