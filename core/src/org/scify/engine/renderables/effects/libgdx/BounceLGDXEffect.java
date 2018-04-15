package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.BounceEffect;

public class BounceLGDXEffect extends BounceEffect implements LGDXEffect {
    public BounceLGDXEffect() {
        super();
    }

    public BounceLGDXEffect(double dBounceWidth, double dBounceHeight, double dDurationMSec) {
        super(dBounceWidth, dBounceHeight, dDurationMSec);
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        aTarget.addAction(Actions.moveTo((float)(rRenderable.getxPos() + dTargetXOffset),
                (float)(rRenderable.getyPos() + dTargetYOffset)));
        // DEBUG LINES
//        System.err.println("Target position: " + String.valueOf(aTarget.getX()) + "," +
//                String.valueOf(aTarget.getY()));
        //////////////
    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Update target alpha
        sTarget.setPosition((float)(sTarget.getX() + dTargetXOffset), (float)(sTarget.getY() + dTargetYOffset));

    }}
