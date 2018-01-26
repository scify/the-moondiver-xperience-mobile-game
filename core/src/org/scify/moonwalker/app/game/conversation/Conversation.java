package org.scify.moonwalker.app.game.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.ArrayList;
import java.util.List;

public class Conversation implements ConversationRules {
    protected List<ConversationState> conversationStates;
    protected int currentConversationIndex = -1;
    private Json json;
    protected ResourceLocator resourceLocator;
    protected String conversationJSONFilePath;

    public Conversation(String filePath) {
        conversationStates = new ArrayList<>();
        resourceLocator = new ResourceLocator();
        conversationJSONFilePath = filePath;
        json = new Json();
        conversationStates = parseConversationStatesFromFile();
        System.out.println(conversationStates.size());
    }

    private List<ConversationState> parseConversationStatesFromFile() {
        return json.fromJson(ArrayList.class, ConversationState.class, Gdx.files.internal(resourceLocator.getFilePath(conversationJSONFilePath)));
    }

    @Override
    public ConversationState getInitialState() {
        return conversationStates.get(0);
    }

    @Override
    public ConversationState getNextState(UserAction userAction) {
        if(userAction != null) {
            if(currentConversationIndex == -1)
                return getInitialState();
            return conversationStates.get(currentConversationIndex++);
        }
        return null;
    }

    @Override
    public boolean isConversationFinished() {
        return false;
    }
}
