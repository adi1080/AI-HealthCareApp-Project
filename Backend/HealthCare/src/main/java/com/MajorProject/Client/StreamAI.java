package com.MajorProject.Client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "AI-Service", url = "http://localhost:8081/ai/")
public class StreamAI {


}
