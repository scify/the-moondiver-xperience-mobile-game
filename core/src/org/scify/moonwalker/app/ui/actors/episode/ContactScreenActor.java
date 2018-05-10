package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.ContactScreenRenderable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ContactScreenActor extends FadingTableActor<ContactScreenRenderable> {

    protected Image contactDescription;
    protected Image contactAvatar;
    protected Button exitButton;

    public ContactScreenActor(Skin skin, ContactScreenRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {
        top();
        createFirstColumn();

        createSecondColumn();

        createThirdColumn();
    }

    protected void createFirstColumn() {
        float screenHeight = getHeight();
        Table table = new Table();
        table.top().right();
        contactDescription = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactDescription());
        float width = convertWidth(contactDescription.getWidth());
        float height = convertHeight(contactDescription.getHeight());
        table.add().width(0.3f * width).height(2.6f * height);
        table.row();
        table.add().expandX();
        table.add(contactDescription).width(width).height(height);
        table.row();
        table.add().expandY();
        add(table).height(screenHeight);
    }

    protected void createSecondColumn() {
        float screenHeight = getHeight();
        Table table = new Table();
        table.top();
        contactAvatar = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactAvatar());
        float width = convertWidth(contactAvatar.getWidth());
        float height = convertHeight(contactAvatar.getHeight());
        table.add(contactAvatar).width(width).height(height);
        table.row();
        table.add().expand();
        add(table).height(screenHeight).width(width);
    }

    protected void createThirdColumn() {
        float screenHeight = getHeight();
        Table table = new Table();
        table.top().right();
        exitButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getExitButton());
        float width = convertWidth(exitButton.getWidth());
        float height = convertHeight(exitButton.getHeight());
        exitButton.align(Align.right);
        table.add().height(height * 0.5f);
        table.row();
        table.add(exitButton).width(width).height(height);
        table.row();
        table.add().expand();
        add(table).height(screenHeight).expandX();
    }
}
