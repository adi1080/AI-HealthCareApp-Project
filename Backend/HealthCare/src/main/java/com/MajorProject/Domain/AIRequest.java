package com.MajorProject.Domain;

public class AIRequest {
    private String reportContent;

    public AIRequest(String reportContent) {
        this.reportContent = reportContent;
    }

    public AIRequest() {

    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }
}

