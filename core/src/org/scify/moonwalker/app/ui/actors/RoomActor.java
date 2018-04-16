package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    protected Image phoneImage;
    protected Texture phoneTexture;

    public RoomActor(Skin skin, RoomRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getImgPath());
    }

    public void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
        phoneTexture = imgUrlToTexture(renderable.PHONE_OFF_IMG_PATH);
        phoneImage = new Image(new TextureRegionDrawable(new TextureRegion(phoneTexture)));
        phoneImage.setAlign(Align.center);
        phoneImage.setZIndex(1);
        phone = add(phoneImage).width(convertWidth(phoneTexture.getWidth())).height(convertHeight(phoneTexture.getHeight()));
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (RoomRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            phoneTexture.dispose();
            phoneTexture = imgUrlToTexture(this.renderable.getPhoneImagePath());
            phoneImage.setDrawable(new TextureRegionDrawable(new TextureRegion(phoneTexture)));
        }
    }
}
