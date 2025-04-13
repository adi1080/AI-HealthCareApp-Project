package com.MajorProject.Controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@CrossOrigin( origins = "http://localhost:4200")
public class AIController {

    private OllamaChatModel client;

    public AIController(OllamaChatModel client) {
        this.client = client;
    }

    @GetMapping("/message_response")
    public String promptResponse(@RequestParam("query") String query) {
        if (query == null || query.trim().isEmpty()) {
            return "Query parameter is missing or empty";  // Return plain text
        }
        
        String response = client.call(query);  // Process the query with the AI model
        return response;  // Return plain text response , can also give json response using responsebody
    }
}


