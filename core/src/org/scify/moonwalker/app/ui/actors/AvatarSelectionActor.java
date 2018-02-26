package org.scify.moonwalker.app.ui.actors;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.ui.renderables.AvatarSelectionRenderable;

public class AvatarSelectionActor extends ButtonList{

    protected Label label;
    public AvatarSelectionRenderable avatarSelectionRenderable;

    public AvatarSelectionActor(Skin skin) {
        super(skin, false);
        columnsNum = 3;
        addMainLabel("Select an avatar");
        debug();
    }

    public void setRenderable(AvatarSelectionRenderable renderable) {
        this.avatarSelectionRenderable = renderable;
        setImageButtonsGreyedOutExcept(renderable.getSelectedAvatar().getId());
    }

}
