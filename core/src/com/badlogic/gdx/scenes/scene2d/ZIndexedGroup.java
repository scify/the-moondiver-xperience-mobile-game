package com.badlogic.gdx.scenes.scene2d;

import org.scify.engine.renderables.Renderable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ZIndexedGroup extends Group {
    // Z-index Ordered list of actors
    Map<Actor, Integer> actorZOrders = new HashMap<>();

    @Override
    public void addActor(Actor actor) {
        if (!actorZOrders.containsKey(actor)) {
            // Assign last index
            actorZOrders.put(actor, actorZOrders.size());
        }
        super.addActor(actor);
    }

    protected void reorderActors() {
        getChildren().sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                if (o1 == o2) {
                    return 0;
                }

                // Start with z-index
                int iRes = actorZOrders.get(o1) - actorZOrders.get(o2);
                if (iRes == 0) {
                    // Continue with area (smaller gets higher zorder
                    iRes = -(int)((o1.getWidth() * o1.getHeight()) -
                            (o2.getWidth() * o2.getHeight()));
                }
                if (iRes == 0) {
                    iRes = (int)(o1.toString().hashCode() - o2.toString().hashCode());
                }

                return iRes;
            }
        });

        // Update that childrenChanged
        childrenChanged();
    }

    public void addActorForRenderable(Actor actor, Renderable renderable) {
        // Keep z-index
        actorZOrders.put(actor, renderable.getZIndex());
        reorderActors();
    }
}
