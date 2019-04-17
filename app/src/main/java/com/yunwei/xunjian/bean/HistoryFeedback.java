package com.yunwei.xunjian.bean;

public class HistoryFeedback {
    private Long feedbackDateTime;
    private String feedbackContent;
    private String feedbackPerson;

    public HistoryFeedback(){

    }

    public HistoryFeedback(String feedbackPerson, Long feedbackDateTime, String feedbackContent){
        this.feedbackPerson = feedbackPerson;
        this.feedbackDateTime = feedbackDateTime;
        this.feedbackContent = feedbackContent;
    }


    public Long getFeedbackDateTime() {
        return feedbackDateTime;
    }

    public void setFeedbackDateTime(Long feedbackDateTime) {
        this.feedbackDateTime = feedbackDateTime;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackPerson() {
        return feedbackPerson;
    }

    public void setFeedbackPerson(String feedbackPerson) {
        this.feedbackPerson = feedbackPerson;
    }
}
