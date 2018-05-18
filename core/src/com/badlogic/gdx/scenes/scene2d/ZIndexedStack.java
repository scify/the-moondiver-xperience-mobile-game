package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import org.scify.engine.renderables.Renderable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ZIndexedStack extends Stack {
    // Z-index Ordered list of actors
    Map<Actor, Integer> actorZOrders = new HashMap<>();
    Map<Actor, Renderable> actorRenderables = new HashMap<>();

    protected void updateActorZOrderBasedOnRenderables() {
        // For each actor connected to a renderable
        for (Map.Entry<Actor, Renderable> aCur: actorRenderables.entrySet()) {
            // Update its z-order based on the renderable
            actorZOrders.put(aCur.getKey(), aCur.getValue().getZIndex());
        }

    }

    protected void reorderActors() {
        getChildren().sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                if (o1.equals(o2)) {
                    return 0;
                }

                // Start with z-index
                int iRes = actorZOrders.get(o1) - actorZOrders.get(o2);
                if (iRes == 0) {
                    // Continue with area (smaller gets higher zorder
                    iRes = -(int) ((o1.getWidth() * o1.getHeight()) -
                            (o2.getWidth() * o2.getHeight()));
                }
                // If still equal use hashcodes
                if (iRes == 0) {
                    iRes = (int) (o1.hashCode() - o2.hashCode());
                }

                return iRes;
            }
        });

        // Update that childrenChanged
        childrenChanged();
    }


    @Override
    public void layout() {
        // Always update with the latest renderable z-order
        updateActorZOrderBasedOnRenderables();
        reorderActors();

        super.layout();
    }

    @Override
    public void addActor(Actor actor) {
        if (!actorZOrders.containsKey(actor)) {
            // Assign last index
            actorZOrders.put(actor, actorZOrders.size());
        }
        super.addActor(actor);
    }

    public void addActorForRenderable(Actor actor, Renderable renderable) {
        // Keep z-index
        actorZOrders.put(actor, renderable.getZIndex());
        reorderActors();
    }
}