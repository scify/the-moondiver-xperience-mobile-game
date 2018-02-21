package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndState;
import org.scify.engine.rules.PhysicsRules;
import org.scify.engine.Player;
import org.scify.moonwalker.app.game.BodyFactory;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.util.HashMap;
import java.util.Map;

public class MoonWalkerPhysicsRules extends PhysicsRules implements ContactListener{

    World world;
    protected Map<Renderable, Body> renderableBodyMap = new HashMap<>();
    private final float keyStrokeAcceleration = 70f;
    private GameInfo gameInfo;
    protected BodyFactory bodyFactory;
    private boolean bDisposalOngoing;

    public MoonWalkerPhysicsRules(int worldX, int worldY) {
        super(worldX, worldY);
        createWorld();
        gameInfo = GameInfo.getInstance();
        bodyFactory = new BodyFactory(world);
    }

    private void createWorld() {
        // creating the world
        // the parameters of the vector define in which position the gravity will be applied
        // for example, here we define that the gravity will be applied only in the y axis
        // (second parameter)
        // the second parameter allows the bodies that this world contains to sleep,
        // in order for the game to not calculate the bodies' position all the time, when the bodies are not moving
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);
    }

    protected Body getResourceFor(Renderable renderable) {
        // If I have an existing sprite, reuse it
        if(renderableBodyMap.containsKey(renderable))
            return renderableBodyMap.get(renderable);
        else
            return createBodyFromRenderableAndAddToMap(renderable);
    }

    protected Body createBodyFromRenderableAndAddToMap(Renderable renderable) {
        Body newResourceForRenderable;
        try {
            newResourceForRenderable = bodyFactory.createResourceForRenderable(renderable);
            if(newResourceForRenderable != null) {
                // and map it to the object
                renderableBodyMap.put(renderable, newResourceForRenderable);
                return newResourceForRenderable;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public synchronized GameState getNextState(GameState gsCurrent, UserAction userAction) {
        if (bDisposalOngoing)
            return gsCurrent;

        if(userAction != null)
            handleUserAction(userAction, gsCurrent);
        handleBorderRules(gsCurrent);
        for(Renderable renderable: gsCurrent.getRenderableList()) {
            Body body = getResourceFor(renderable);
            if(body != null) {
                // body position is relevant to its body, so we need to
                // subtract the half of width and height, respectively.
                renderable.setxPos(body.getPosition().x - renderable.getWidth() / 2f);
                renderable.setyPos(body.getPosition().y - renderable.getHeight() / 2f);
            }
        }
        // how many times to calculate physics in a second
        // delta time is the time between 2 frames
        // the second and third parameter defines how many calculations
        // will be done when 2 bodies collide
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        return super.getNextState(gsCurrent, userAction);
    }

    private void handleUserAction(UserAction userAction, GameState gameState) {
        Player pPlayer = gameState.getPlayer();
        if(pPlayer != null) {
            Body playerBody = getResourceFor(pPlayer);
            if (playerBody != null)
                moveBody(userAction, playerBody);
        }
    }

    private void moveBody(UserAction userAction, Body body) {
        switch (userAction.getActionCode()) {
            case UP:
                body.setLinearVelocity(body.getLinearVelocity().x, +keyStrokeAcceleration);
                break;
            case DOWN:
                body.setLinearVelocity(body.getLinearVelocity().x, -keyStrokeAcceleration);
                break;
            case LEFT:
                body.setLinearVelocity(-keyStrokeAcceleration, body.getLinearVelocity().y);
                break;
            case RIGHT:
                body.setLinearVelocity(+keyStrokeAcceleration, body.getLinearVelocity().y);
                break;
            default:
                break;
        }
    }

    private void handleBorderRules(GameState gameState) {
        Player pPlayer = gameState.getPlayer();
        if(pPlayer != null) {
            Body body = getResourceFor(pPlayer);
            if(body != null) {
                if (body.getPosition().y + pPlayer.getHeight() / 2f > gameInfo.getScreenHeight()) {
                    body.setLinearVelocity(body.getLinearVelocity().x, -keyStrokeAcceleration);
                    addPlayerBorderEvents(gameState, 0);
                }
                if (body.getPosition().y - pPlayer.getHeight() / 2f < 0) {
                    body.setLinearVelocity(body.getLinearVelocity().x, +keyStrokeAcceleration);
                    addPlayerBorderEvents(gameState, 1);
                }
                if (body.getPosition().x - pPlayer.getHeight() / 2f < 0) {
                    body.setLinearVelocity(+keyStrokeAcceleration, body.getLinearVelocity().y);
                    addPlayerBorderEvents(gameState, 2);
                }
                if (body.getPosition().x + pPlayer.getHeight() / 2f > gameInfo.getScreenWidth()) {
                    body.setLinearVelocity(-keyStrokeAcceleration, body.getLinearVelocity().y);
                    addPlayerBorderEvents(gameState, 3);
                }
            }
        }
    }

    protected void addPlayerBorderEvents(GameState gameState, int direction) {

        gameState.addGameEvent(new GameEvent("BORDER_UI"));
        gameState.addGameEvent(new GameEvent("PLAYER_BORDER"));
        switch (direction) {
            case 0:
                gameState.addGameEvent(new GameEvent("PLAYER_TOP_BORDER"));
                break;
            case 1:
                gameState.addGameEvent(new GameEvent("PLAYER_BOTTOM_BORDER"));
                break;
            case 2:
                gameState.addGameEvent(new GameEvent("PLAYER_LEFT_BORDER"));
                break;
            case 3:
                gameState.addGameEvent(new GameEvent("PLAYER_RIGHT_BORDER"));
                break;
        }
    }

    @Override
    public synchronized void disposeResources() {
        // block loop
        bDisposalOngoing = true;
        for(Map.Entry<Renderable, Body> entry : renderableBodyMap.entrySet()) {
            world.destroyBody(entry.getValue());
        }
        // Do NOT dispose of world, because it appears that it is called again (?) by
        // the garbage collector

        //unblock loop
        bDisposalOngoing = false;
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        return null;
    }


    @Override
    public void gameStartedEvents(GameState currentState) {

    }

    @Override
    public void gameEndedEvents(GameState currentState) {

    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }

    @Override
    public void setInitialState(GameState gameState) {

    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
