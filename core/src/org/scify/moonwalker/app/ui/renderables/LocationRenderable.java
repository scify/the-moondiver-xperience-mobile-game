package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;

public class LocationRenderable extends ChattableRenderable {
    public LocationRenderable(float xPos, float yPos, float width, float height, String id, String backgroundImagePath) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_LOCATION, id, backgroundImagePath);
    }
}
