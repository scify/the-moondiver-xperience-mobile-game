package org.scify.moonwalker.app;

import com.badlogic.gdx.physics.box2d.World;

import org.scify.engine.GameEvent;
import org.scify.engine.Renderable;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.actors.MoonWalkerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MoonWalkerGameState extends GameState {

    public World world;
    protected MoonWalkerPlayer player;
    protected List<Renderable> renderableList = new ArrayList<Renderable>();

    public MoonWalkerGameState(List<GameEvent> eventQueue, MoonWalkerPlayer player, World world) {
        super(eventQueue);
        this.player = player;
        this.world = world;
        this.renderableList.add(player);
    }

    public List<Renderable> getRenderableList() {
        return renderableList;
    }

    @Override
    public List<GameEvent> getEventQueue() {
        return eventQueue;
    }

    public MoonWalkerPlayer getPlayer() {
        return player;
    }


}
