package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.Renderable;

public class ActorFactory extends ComponentFactory{

    public ActorFactory(Skin skin) {
        super(skin);
    }

    public Actor createResourceForType(Renderable renderable) {
        Actor toReturn;
        switch (renderable.getType()) {
            case "label":
                Label label = new Label("", skin);
                label.setWidth(renderable.getWidth());
                label.setHeight(renderable.getHeight());
                label.setWrap(true);
                toReturn = label;
                break;
            case "yoda":
                Image yodaImg = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/yoda.png")))));
                yodaImg.setSize(renderable.getWidth(), renderable.getHeight());
                toReturn = yodaImg;
                break;
            case "player":
                Image playerImg = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/player.png")))));

                playerImg.setSize(renderable.getWidth(), renderable.getHeight());
                toReturn = playerImg;
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }
}
