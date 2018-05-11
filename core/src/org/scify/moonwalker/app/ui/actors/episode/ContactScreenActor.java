package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.ContactScreenRenderable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ContactScreenActor extends FadingTableActor<ContactScreenRenderable> {

    protected Image contactDescription;
    protected Image contactAvatar;

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
    }

    protected void createFirstColumn() {
        float screenHeight = getHeight();
        Table table = new Table();
        table.top();
        contactDescription = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactDescription());
        float width = convertWidth(contactDescription.getWidth());
        float height = convertHeight(contactDescription.getHeight());
        float columnWidth = width + 0.3f * width;
        table.add().width(columnWidth).height(2.6f * height);
        table.row();
        table.add(contactDescription).width(width).height(height).right();
        table.row();
        table.add().expandY();
        add(table).height(screenHeight).width(columnWidth).center();
    }

    protected void createSecondColumn() {
        float screenHeight = getHeight();
        Table table = new Table();
        table.top();
        contactAvatar = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getContactAvatar());
        float width = convertWidth(contactAvatar.getWidth());
        float height = convertHeight(contactAvatar.getHeight());
        table.add(contactAvatar).width(width).height(height).left();
        table.row();
        table.add().expand();
        add(table).height(screenHeight).expandX().left();
    }
}
