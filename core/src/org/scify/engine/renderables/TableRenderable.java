package org.scify.engine.renderables;

import org.scify.engine.UserAction;

import java.util.HashMap;
import java.util.Map;

public class TableRenderable extends Renderable {
    protected ImageRenderable tableBGRenderable = null;
    protected String bgImgPath;
    protected static Map<String, ImageRenderable> imageRenderableBuffer;

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public TableRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        bgImgPath = null;
        if (imageRenderableBuffer == null) {
            imageRenderableBuffer = new HashMap<>();
        }
    }

    public TableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImgPath) {
        super(xPos, yPos, width, height, type, id);
        this.bgImgPath = bgImgPath;
        if (imageRenderableBuffer == null) {
            imageRenderableBuffer = new HashMap<>();
        }
        if (bgImgPath != null) {
            tableBGRenderable = createImageRenderable("bg_" + bgImgPath, bgImgPath, false, true, 0);
        }
    }

    public TableRenderable(String type, String id) {
        super(type, id);
        bgImgPath = null;
    }

    // TODO: Implement add child?

    protected ActionButtonRenderable createTextButton(String id, String text, UserAction userAction, boolean positionDrawable, boolean visibility, int zIndex) {
        ActionButtonRenderable ret = new ActionButtonRenderable(Renderable.ACTOR_TEXT_BUTTON, id);
        ret.setTitle(text);
        ret.setUserAction(userAction);
        setRenderableAttributes(ret, positionDrawable, visibility, zIndex);
        return ret;
    }

    protected ImageRenderable createImageRenderable(String id, String img, boolean positionDrawable, boolean visibility, int zIndex) {
        ImageRenderable ret;
        if (imageRenderableBuffer.containsKey(id)) {
            ret = imageRenderableBuffer.get(id);
        } else {
            ret = new ImageRenderable(id, img);
            imageRenderableBuffer.put(id, ret);
        }
        setRenderableAttributes(ret, positionDrawable, visibility, zIndex);
        return ret;
    }

    protected TextLabelRenderable createTextLabelRenderable(String id, String text, boolean positionDrawable, boolean visibility, int zIndex) {
        TextLabelRenderable ret = new TextLabelRenderable(Renderable.ACTOR_LABEL, id);
        ret.setLabel(text);
        setRenderableAttributes(ret, positionDrawable, visibility, zIndex);
        return ret;
    }

    protected TextLabelRenderable createTextRotatableLabelRenderable(String id, String text, boolean positionDrawable, boolean visibility, int zIndex) {
        TextLabelRenderable ret = new TextLabelRenderable(Renderable.ACTOR_ROTATABLE_LABEL, id);
        ret.setLabel(text);
        setRenderableAttributes(ret, positionDrawable, visibility, zIndex);
        return ret;
    }

    protected void setRenderableAttributes(Renderable renderable, boolean positionDrawable, boolean visibility, int zIndex) {
        renderable.setPositionDrawable(positionDrawable);
        renderable.setVisible(visibility);
        renderable.setZIndex(zIndex);
    }

    public static void resetBufferedRenderables() {
        imageRenderableBuffer.clear();
    }
}
