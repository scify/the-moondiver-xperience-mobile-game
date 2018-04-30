package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.sprites.SpriteWithEffects;

public class SpriteFactory extends ComponentFactory{

    public SpriteFactory(Skin skin) {
        super(skin);
    }

    @Override
    public Sprite createResourceForType(Renderable renderable) {
        Sprite sToReturn = null;
        // Get a sprite for this world object type
        switch (renderable.getType()) {
            case Renderable.SPRITE_IMAGE: {
                Texture texture = new Texture(resourceLocator.getFilePath(renderable.getImgPath()));
                Sprite sprite = new SpriteWithEffects(texture);
                sprite.setSize(renderable.getWidth(), renderable.getHeight());
                sToReturn = sprite;
                }
                break;
            case Renderable.SPRITE_BACKGROUND_IMAGE: {
                Texture texture = new Texture(resourceLocator.getFilePath(renderable.getImgPath()));
                Sprite sprite = new SpriteWithEffects(texture);
                sprite.setSize(renderable.getWidth(), renderable.getHeight());
                sToReturn = sprite;
            }
                break;
            case Renderable.SPRITE_PLAYER: {
                Texture playerImg = new Texture(resourceLocator.getFilePath("img/player.png"));
                Sprite playerSprite = new SpriteWithEffects(playerImg);
                playerSprite.setSize(renderable.getWidth(), renderable.getHeight());
                sToReturn = playerSprite;
            }
                break;
        }
        return sToReturn;
    }


}
