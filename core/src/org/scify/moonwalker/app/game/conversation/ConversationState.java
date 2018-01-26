package org.scify.moonwalker.app.game.conversation;

public class ConversationState {
    protected int order;
    protected String speakerId;
    protected String text;
    protected String prerequisites;
    protected String triggerEvent;
    protected int nextOrder;

    public ConversationState() {
    }

    public int getNextOrder() {
        return nextOrder;
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
