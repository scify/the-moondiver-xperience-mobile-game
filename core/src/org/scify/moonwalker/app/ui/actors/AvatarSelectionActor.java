package org.scify.moonwalker.app.ui.actors;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.renderables.AvatarSelectionRenderable;

public class AvatarSelectionActor extends ButtonList implements Updateable {

    protected Label label;
    public AvatarSelectionRenderable avatarSelectionRenderable;

    public AvatarSelectionActor(Skin skin) {
        super(skin, false);
        columnsNum = 3;
        addMainLabel("Select an avatar");
        debug();
    }

    @Override
    public void update(Renderable renderable) {
        this.avatarSelectionRenderable = (AvatarSelectionRenderable) renderable;
        setImageButtonsGreyedOutExcept(avatarSelectionRenderable.getSelectedAvatar().getId());
    }
}
