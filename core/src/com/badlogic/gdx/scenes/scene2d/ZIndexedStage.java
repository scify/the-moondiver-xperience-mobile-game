package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.TableActor;

public class ZIndexedStage extends Stage {
    protected ZIndexedGroup orderedRoot;

    public ZIndexedStage() {
        super();
    }

    public ZIndexedStage(Viewport viewport) {
        super(viewport);
    }

    public ZIndexedStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    @Override
    public void addActor(Actor actor) {
        replaceRootObject();

        super.addActor(actor);
    }


    /**
     * Adds an actor, utilizing the z-index of a related renderable.
     * @param actor The actor to add to the stage.
     * @param renderable The renderable with the required z-order.
     */
    public void addActorForRenderable(Actor actor, Renderable renderable) {
        // Ascertain that we use the ordered root object
        replaceRootObject();

        orderedRoot.addActorForRenderable(actor, renderable);
        super.addActor(actor);
    }

    protected void replaceRootObject() {
        if (orderedRoot == null) {
            orderedRoot = new ZIndexedGroup();
            setRoot(orderedRoot);
            orderedRoot.setStage(this);
        }
    }

}
