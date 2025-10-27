package com.MajorProject.common.Domain;

import java.util.List;

public class AIResponse {
    private String summary;
    private List<String> suggestions;
    private Double healthScore;

    public AIResponse() {}

    public AIResponse(String summary, List<String> suggestions, Double healthScore) {
        this.summary = summary;
        this.suggestions = suggestions;
        this.healthScore = healthScore;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    @Override
    public String toString() {
        return "AIResponse{" +
                "summary='" + summary + '\'' +
                ", suggestions=" + suggestions +
                ", healthScore=" + healthScore +
                '}';
    }
}
