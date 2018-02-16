package org.scify.moonwalker.app.game;

import com.badlogic.gdx.physics.box2d.*;
import org.scify.engine.Renderable;

public class BodyFactory {

    private World world;

    public BodyFactory(World world) {
        this.world = world;
    }

    public Body createResourceForRenderable(Renderable renderable) throws Exception {
        if(renderable == null) {
            throw new Exception("Renderable cannot be null");
        }
        String sType = renderable.getType();

        Body bToReturn = null;
        // Get a sprite for this world object type
        switch (sType) {
            case "player":
                bToReturn = createBody(BodyDef.BodyType.DynamicBody, renderable.getWidth(), renderable.getHeight(), renderable.getxPos(), renderable.getyPos());
                break;
        }
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
}
