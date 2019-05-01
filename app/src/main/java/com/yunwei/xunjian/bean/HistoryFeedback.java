package com.yunwei.xunjian.bean;

import java.io.Serializable;

public class HistoryFeedback implements Serializable {
    private String feedbackDateTime;
    private String feedbackContent;
    private String feedbackPerson;
    private String feedbackPicPath;
    private String replyDateTime;
    private String replyPerson;
    private String replyContent;
    private String reply;


    public HistoryFeedback(){

    }

    public HistoryFeedback(String feedbackPerson, String feedbackDateTime, String feedbackContent){
        this.feedbackPerson = feedbackPerson;
        this.feedbackDateTime = feedbackDateTime;
        this.feedbackContent = feedbackContent;
    }

    public HistoryFeedback(String feedbackDateTime, String feedbackContent, String feedbackPerson, String feedbackPicPath, String replyDateTime, String replyPerson, String replyContent, String reply) {
        this.feedbackDateTime = feedbackDateTime;
        this.feedbackContent = feedbackContent;
        this.feedbackPerson = feedbackPerson;
        this.feedbackPicPath = feedbackPicPath;
        this.replyDateTime = replyDateTime;
        this.replyPerson = replyPerson;
        this.replyContent = replyContent;
        this.reply = reply;
    }

    public String getFeedbackDateTime() {
        return feedbackDateTime;
    }

    public void setFeedbackDateTime(String feedbackDateTime) {
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

    public String getFeedbackPicPath() {
        return feedbackPicPath;
    }

    public void setFeedbackPicPath(String feedbackPicPath) {
        this.feedbackPicPath = feedbackPicPath;
    }

    public String getReplyDateTime() {
        return replyDateTime;
    }

    public void setReplyDateTime(String replyDateTime) {
        this.replyDateTime = replyDateTime;
    }

    public String getReplyPerson() {
        return replyPerson;
    }

    public void setReplyPerson(String replyPerson) {
        this.replyPerson = replyPerson;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
