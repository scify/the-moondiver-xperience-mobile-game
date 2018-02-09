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
                toReturn = createImage("img/yoda.png", renderable);
                break;
            case "player":
                toReturn = createImage("img/player.png", renderable);
                break;
            case "boy":
                toReturn = createImage("img/boy.png", renderable);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }

    public Image createImage(String imgFileRelevantPath, Renderable renderable) {
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgFileRelevantPath)))));
        img.setSize(renderable.getWidth(), renderable.getHeight());
        return img;
    }
}
