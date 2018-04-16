package org.scify.moonwalker.app;

import com.badlogic.gdx.physics.box2d.World;
import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.Player;

import java.util.List;

/**
 * Game state for the moon walker game.
 */
public class MoonWalkerGameState extends GameState {

    /**
     * Gdx-specific class that holds all items that obey to physics laws
     */
    public World world;

    public MoonWalkerGameState(List<GameEvent> eventQueue, Player player, World world) {
        super(eventQueue);
        this.player = player;
        this.world = world;
        if(player != null)
            addRenderable(player);
    }

    public MoonWalkerGameState(MoonWalkerGameState moonWalkerGameState) {
        super(moonWalkerGameState);
        world = moonWalkerGameState.world;
    }
}
