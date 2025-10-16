package com.MajorProject.Util;

import com.MajorProject.Service.FeedbackAnalysisService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FeedbackAnalysisScheduler {

    private final FeedbackAnalysisService feedbackAnalysisService;

    public FeedbackAnalysisScheduler(FeedbackAnalysisService feedbackAnalysisService) {
        this.feedbackAnalysisService = feedbackAnalysisService;
    }

    @Scheduled(cron = "0 0 2 */15 * ?") // every 15 days at 2 AM
    public void periodicFeedbackUpdate() {
        System.out.println("🕑 15-day feedback analysis check started...");
        feedbackAnalysisService.analyzeMissingOrOutdatedFeedbacks();
    }
}

