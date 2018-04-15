package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.RotateEffect;

public class RotateLGDXEffect extends RotateEffect implements LGDXEffect {
    public static String PARAM_ORIGIN_X = "originX";
    public static String PARAM_ORIGIN_Y = "originY";

    public RotateLGDXEffect() {
        super();

        params.put(PARAM_ORIGIN_X, "0.0");
        params.put(PARAM_ORIGIN_Y, "0.0");

    }

    public RotateLGDXEffect(double dFromAngle, double dToAngle, double dDurationMSec) {
        super(dFromAngle, dToAngle, dDurationMSec);

        params.put(PARAM_ORIGIN_X, "0.0");
        params.put(PARAM_ORIGIN_Y, "0.0");
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Reset origin to sprite center
        // TODO: Check if we should use actor center and when
        setOriginX(aTarget.getWidth() / 2.0);
        setOriginY(aTarget.getHeight() / 2.0);

        // DEBUG LINES
//        System.err.println("Target alpha: " + String.valueOf(dTargetAngle));
        //////////////
        // Update target rotation, also using origin
        aTarget.setOrigin((float)getOriginX(), (float)getOriginY());
        aTarget.setRotation((float)dTargetAngle);
    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Reset origin to sprite center
        // TODO: Check if we should use sprite center and when
        setOriginX(sTarget.getWidth() / 2.0);
        setOriginY(sTarget.getHeight() / 2.0);

        // Update target rotation, also using origin
        sTarget.setOrigin((float)getOriginX(), (float)getOriginY());
        sTarget.setRotation((float)dTargetAngle);

    }

    protected double getOriginX() {
        return Double.valueOf(params.get(PARAM_ORIGIN_X));
    }

    protected double getOriginY() {
        return Double.valueOf(params.get(PARAM_ORIGIN_Y));
    }

    protected void setOriginX(double dNewX) {
        params.put(PARAM_ORIGIN_X,String.valueOf(dNewX));
    }

    protected void setOriginY(double dNewY) {
        params.put(PARAM_ORIGIN_Y,String.valueOf(dNewY));
    }
}
