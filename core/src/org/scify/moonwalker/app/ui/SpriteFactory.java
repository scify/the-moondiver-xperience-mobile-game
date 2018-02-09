package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.Renderable;

public class SpriteFactory extends ComponentFactory{

    public SpriteFactory(Skin skin) {
        super(skin);
    }

    @Override
    public Sprite createResourceForType(Renderable renderable) {
        Sprite sToReturn = null;
        // Get a sprite for this world object type
        switch (renderable.getType()) {
//            case "player":
//                Texture playerImg = new Texture(resourceLocator.getFilePath("img/player.png"));
//                Sprite playerSprite = new Sprite(playerImg);
//                playerSprite.setSize(renderable.getWidth(), renderable.getHeight());
//                sToReturn = playerSprite;
//                break;
        }
        return sToReturn;
    }


}
