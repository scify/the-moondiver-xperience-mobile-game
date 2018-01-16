package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.scify.engine.GameEvent;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.MoonWalkerPlayer;
import org.scify.engine.Renderable;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.ui.UnsupportedRenderableTypeException;

import java.util.HashMap;
import java.util.Map;

public class MoonWalkerPhysicsRules extends org.scify.moonwalker.app.game.rules.PhysicsRules {

    World world;
    Map<Renderable, Body> renderableBodyMap = new HashMap<Renderable, Body>();
    private final float keyStrokeAcceleration = 70f;
    private GameInfo gameInfo;

    public MoonWalkerPhysicsRules(int worldX, int worldY) {
        super(worldX, worldY);
        createWorld();
        gameInfo = GameInfo.getInstance();
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
        Body resource;
        // If I have an existing sprite
        if(renderableBodyMap.containsKey(renderable)) {
            // reuse it
            resource = renderableBodyMap.get(renderable);
        } else {
            // else
            // getResourceForType
            Body newResourceForRenderable = getResourceForRenderable(renderable);
            // and map it to the object
            renderableBodyMap.put(renderable, newResourceForRenderable);
            resource = newResourceForRenderable;
        }
        return resource;
    }

    protected Body getResourceForRenderable(Renderable renderable) {
        String sType = renderable.getType();
        Body bToReturn = null;
        // Get a sprite for this world object type
        switch (sType) {
            case "PLAYER":
                bToReturn = createBody(BodyDef.BodyType.DynamicBody, (float) (gameInfo.getScreenWidth() * 0.2), (float) (gameInfo.getScreenWidth() * 0.2), renderable.getX(), renderable.getY());
                break;
        }
        if(bToReturn == null)
            throw new UnsupportedRenderableTypeException("Renderable: " + sType + " is not supported.");
        return bToReturn;
    }

    private Body createBody(BodyDef.BodyType bodyType, float width, float height, float xPos, float yPos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(xPos , yPos);

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        // IMPORTANT shape takes half-width and half-height parameters, according to documentation
        shape.setAsBox(width / 2 , height / 2);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        MoonWalkerGameState currentState = (MoonWalkerGameState) gsCurrent;
        if(userAction != null)
            handleUserAction(userAction, currentState);
        handlePositionRules(currentState);
        for(Renderable renderable: currentState.getRenderableList()) {
            Body body = getResourceFor(renderable);
            renderable.setX(body.getPosition().x);
            renderable.setY(body.getPosition().y);
        }
        // how many times to calculate physics in a second
        // delta time is the time between 2 frames
        // the second and third parameter defines how many calculations
        // will be done when 2 bodies collide
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        return super.getNextState(gsCurrent, userAction);
    }

    private void handleUserAction(UserAction userAction, MoonWalkerGameState gameState) {
        MoonWalkerPlayer pPlayer = gameState.getPlayer();
        Body body = getResourceFor(pPlayer);
        GameEvent event = null;
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
        if(event != null) {
            gameState.getEventQueue().add(event);
        }
    }

    private void handlePositionRules(MoonWalkerGameState gameState) {
        MoonWalkerPlayer pPlayer = gameState.getPlayer();
        Body body = getResourceFor(pPlayer);
        if(body.getPosition().y > gameInfo.getScreenHeight()) {
            body.setLinearVelocity(body.getLinearVelocity().x, -keyStrokeAcceleration);
            gameState.getEventQueue().add(new GameEvent("PLAYER_TOP_BORDER"));
        }
        if(body.getPosition().x < 0 ) {
            body.setLinearVelocity(+keyStrokeAcceleration, body.getLinearVelocity().y);
            gameState.getEventQueue().add(new GameEvent("PLAYER_LEFT_BORDER"));
        }
        if(body.getPosition().y < 0 ) {
            body.setLinearVelocity(body.getLinearVelocity().x, +keyStrokeAcceleration);
            gameState.getEventQueue().add(new GameEvent("PLAYER_BOTTOM_BORDER"));
        }
        if(body.getPosition().x > gameInfo.getScreenWidth() ) {
            body.setLinearVelocity(-keyStrokeAcceleration, body.getLinearVelocity().y);
            gameState.getEventQueue().add(new GameEvent("PLAYER_RIGHT_BORDER"));
        }
    }

    @Override
    public void disposeResources() {
        for(Map.Entry<Renderable, Body> entry : renderableBodyMap.entrySet()) {
            world.destroyBody(entry.getValue());
        }
        world.dispose();
    }
}
