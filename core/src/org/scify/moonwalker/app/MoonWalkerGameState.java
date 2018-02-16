package org.scify.moonwalker.app;

import com.badlogic.gdx.physics.box2d.World;
import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.game.Player;

import java.util.List;

public class MoonWalkerGameState extends GameState {

    public World world;

    public MoonWalkerGameState(List<GameEvent> eventQueue, Player player, World world) {
        super(eventQueue);
        this.player = player;
        this.world = world;
        if(player != null)
            this.renderableList.add(player);
    }
}
