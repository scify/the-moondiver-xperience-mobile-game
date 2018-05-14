package org.scify.engine.conversation;

import java.util.HashSet;
import java.util.Set;

public class ConversationLine {
    protected int id;
    protected int order;
    protected String speakerId;
    protected String text;
    protected String prerequisites;
    protected String onEnterCurrentOrderTrigger;
    protected String onExitCurrentOrderTrigger;
    protected String buttonText;
    protected int nextOrder;

    public void setLineProcessedByRules(boolean lineProcessedByRules) {
        this.lineProcessedByRules = lineProcessedByRules;
    }

    //extra boolean for line events
    protected boolean lineProcessedByRules;

    public boolean isLineProcessedByRules() {
        return lineProcessedByRules;
    }

    public ConversationLine() {
        lineProcessedByRules = false;
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

    public Set<String> getOnEnterCurrentOrderTrigger() {
        return parseEvents(onEnterCurrentOrderTrigger);
    }

    public Set<String> getOnExitCurrentOrderTrigger() {
        return parseEvents(onExitCurrentOrderTrigger);
    }

    protected Set<String> parseEvents(String sEventList) {
        Set<String> sRes = new HashSet<>();
        // Allow empty result
        if (sEventList == null)
            return sRes;

        for (String sCur : sEventList.trim().split(",")) {
            sRes.add(sCur.trim());
        }

        return sRes;
    }

    public int getId() {
        return id;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setText(String text) {
        this.text = text;
    }
}
