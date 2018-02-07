package org.scify.engine.conversation;

public class ConversationLine {
    protected int id;
    protected int order;
    protected String speakerId;
    protected String text;
    protected String prerequisites;
    protected String triggerEvent;
    protected int nextOrder;

    public ConversationLine() {
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

    public int getId() {
        return id;
    }
}
