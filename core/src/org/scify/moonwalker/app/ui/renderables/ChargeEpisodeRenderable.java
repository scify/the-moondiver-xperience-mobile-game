package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;

import java.util.ArrayList;
import java.util.List;

public class ChargeEpisodeRenderable extends FadingTableRenderable {
    List<Renderable> allRenderables = new ArrayList<>();

    public ChargeEpisodeRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath) {
        super(xPos, yPos, width, height, type, id, bgImagePath);
    }

    public ChargeEpisodeRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath, boolean bStartVisibility) {
        super(xPos, yPos, width, height, type, id, bgImagePath, bStartVisibility);
    }

    public List<Renderable> getAllRenderables() {
        return allRenderables;
    }

    private void initSubRenderables() {

    }
}
