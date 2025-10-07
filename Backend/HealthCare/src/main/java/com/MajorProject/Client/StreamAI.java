package com.MajorProject.Client;

import com.MajorProject.Domain.AIRequest;
import com.MajorProject.Domain.AIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AI-Service", url = "http://localhost:8081/ai")
public interface StreamAI {
    @PostMapping("/analyze")
    AIResponse analyzeReport(@RequestBody AIRequest request);
}
