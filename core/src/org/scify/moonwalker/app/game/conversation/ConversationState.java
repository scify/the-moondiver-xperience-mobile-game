package org.scify.moonwalker.app.game.conversation;

public class ConversationState {
    protected int order;
    protected String speakerId;
    protected String text;
    protected String prerequisites;
    protected String triggerEvent;

    public ConversationState(int order, String speakerId, String text, String prerequisites, String triggerEvent) {
        this.order = order;
        this.speakerId = speakerId;
        this.text = text;
        this.prerequisites = prerequisites;
        this.triggerEvent = triggerEvent;
    }

    public int getOrder() {
        return order;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public String getText() {
        return text;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }
}
