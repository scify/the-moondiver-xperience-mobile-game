package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomActor extends TableActor implements Updateable {

    protected RoomRenderable renderable;
    protected Cell phone;

    public RoomActor (Skin skin, RoomRenderable renderable) {
        super(skin);
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        //addBackground(renderable.getImgPath());
    }

    public void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        Texture texture = imgUrlToTexture(renderable.PHONE_OFF_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        image.setAlign(Align.center);
        add(image).width(convertWidth(texture.getWidth())).height(convertHeight(texture.getHeight()));
    }

    @Override
    public void update(Renderable renderable) {

    }

    protected float convertHeight(float initialHeight) {
        int initialBackgroundHeight = 1080;
        float ret = getHeight() * initialHeight;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    protected float convertWidth(float initialWidth) {
        int initialBackgroundWidth = 1920;
        float ret = getWidth() * initialWidth;
        ret = ret / initialBackgroundWidth;
        return ret;
    }
}
