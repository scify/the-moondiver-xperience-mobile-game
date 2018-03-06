package org.scify.moonwalker.app.game.rules;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.Player;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class SinglePlayerRules extends MoonWalkerRules {

    protected Player pPlayer;

    public SinglePlayerRules() {
        super();
    }

    @Override
    public GameState getInitialState() {
        List<GameEvent> eventQueue = Collections.synchronizedList(new LinkedList<GameEvent>());
        if(initialGameState == null)
            initialGameState = new MoonWalkerGameState(eventQueue, pPlayer, physics.world);
        return initialGameState;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

    /**
     * Avatar information is stored in the game state that was delivered
     * by the Avatar selection episode.
     * So we need to search for the appropriate {@link GameEvent} in the previous GameState
     * and set the avatar according to the game event's value (boy or girl).
     */
    protected void addPlayerAvatar(GameState gsCurrent) {
        if(initialGameState != null) {
            GameEvent avatarSelectionEvent = initialGameState.getGameEventsWithType("AVATAR_SELECTED");
            if (avatarSelectionEvent != null) {
                String avatarIdentifier = (String) avatarSelectionEvent.parameters;
                createPlayerAvatar(avatarIdentifier, gsCurrent);
                // transfer the avatar selection to current game state also
                // to be used by other episodes
                // TODO Ask ggianna
                gsCurrent.addGameEvent(new GameEvent("AVATAR_SELECTED", avatarIdentifier));
            }
        }
    }

    private void createPlayerAvatar(String avatarIdentifier, GameState gsCurrent) {
        pPlayer = new Player(appInfo.getScreenWidth() / 2f,
                appInfo.getScreenHeight() / 2f - 80,  appInfo.getScreenWidth() * 0.3f, appInfo.getScreenWidth() * 0.3f,
                avatarIdentifier, "player");
        gsCurrent.addRenderable(pPlayer);
    }

}
