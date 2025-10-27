package com.MajorProject.Client;

import com.MajorProject.common.Domain.AIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "AI-Service", url = "http://localhost:8081/ai")
public interface StreamAI {
//    @PostMapping("/analyze")
//    AIResponse analyzeReport(@RequestBody AIRequest request);

    @PostMapping("/analyze/feedback")
    Map<Long, com.MajorProject.common.Domain.AIResponse> analyzeFeedbacks(@RequestBody Map<Long, List<String>> doctorFeedbackMap);

}
