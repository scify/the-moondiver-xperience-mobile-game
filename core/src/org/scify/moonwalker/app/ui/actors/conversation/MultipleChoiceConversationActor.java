package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;


public class MultipleChoiceConversationActor extends FadingTableActor<MultipleChoiceConversationRenderable> {

    protected Button buttonTopLeft;
    protected Button buttonBottomLeft;
    protected Button buttonTopRight;
    protected Button buttonBottomRight;

    public MultipleChoiceConversationActor(Skin skin, MultipleChoiceConversationRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        init();
    }

    protected void init() {
        float width = renderable.getWidth();
        float height = renderable.getHeight();
        defaults();
        center();
        setWidth(width);
        setHeight(height);
        addBackground(renderable.getTableBGRenderable(), width, height);

        add().height(height).width(0.01f * width);


        add().height(height).width(0.01f * width);

        //Buttons
        Table buttonsTable = new Table();
        buttonsTable.defaults();
        buttonsTable.center();
        float buttonWidth = 0.40f * width;
        float buttonHeight = 0.3f * height;
        buttonTopLeft = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonTopLeft());
        buttonsTable.add(buttonTopLeft).width(buttonWidth).height(buttonHeight);
        buttonsTable.add().height(buttonHeight).width(0.05f * width);
        buttonTopRight = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonTopRight());
        buttonsTable.add(buttonTopRight).width(buttonWidth).height(buttonHeight);
        buttonsTable.row();
        buttonsTable.add().height(buttonHeight/2).colspan(3);
        buttonsTable.row();
        buttonBottomLeft = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonBottomLeft());
        buttonsTable.add(buttonBottomLeft).width(buttonWidth).height(buttonHeight);
        buttonsTable.add().height(buttonHeight).width(0.05f * width);
        buttonBottomRight = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonBottomRight());
        buttonsTable.add(buttonBottomRight).width(buttonWidth).height(buttonHeight);
        add(buttonsTable).width(buttonWidth);

        add().height(height).width(0.01f * width);
    }
}
