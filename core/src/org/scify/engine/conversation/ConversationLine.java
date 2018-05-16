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
    // we may need to pass a payload object to the line
    // in case we want to retrieve data on click or other events
    protected Object payload;

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

    public Set<String> getPrerequisites() {
        return parseEvents(prerequisites);
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

    public void setId(int id) {
        this.id = id;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public void setOnEnterCurrentOrderTrigger(Set<String> onEnterCurrentOrderTriggerEvents) {
        this.onEnterCurrentOrderTrigger = convert(onEnterCurrentOrderTriggerEvents);
    }

    public void setOnExitCurrentOrderTrigger(Set<String> onExitCurrentOrderTriggerEvents) {
        this.onExitCurrentOrderTrigger = convert(onExitCurrentOrderTriggerEvents);
    }

    public String convert(Set<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String st : strings) {
            sb.append('\'').append(st).append('\'').append(',');
        }
        if (strings.size() != 0) sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
