package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.TableActor;
import org.scify.moonwalker.app.ui.actors.Updateable;


public class MultipleChoiceConversationActor extends TableActor<MultipleChoiceConversationRenderable> implements Updateable {
    protected Label lineLabel;
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

        Table centralTable = new Table();

        lineLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getQuestion());
        lineLabel.setWrap(true);
        lineLabel.setAlignment(Align.center);
        centralTable.add(lineLabel).center().width(width * 0.90f).padBottom(height * 0.07f);
        centralTable.row();
        centralTable.row();
        centralTable.row();
        centralTable.row();
        //Buttons
        Table buttonsTable = new Table();
        buttonsTable.defaults();
        buttonsTable.center();
        float buttonWidth = 0.40f * width;
        float buttonHeight = 0.2f * height;
        buttonTopLeft = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonTopLeft());
        setButtonAttributes(buttonTopLeft, buttonsTable, buttonWidth, buttonHeight, renderable.getConversationButtonTopLeft().isDefaultButtonSkin());
        buttonsTable.add().height(buttonHeight).width(0.05f * width);
        buttonTopRight = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonTopRight());
        setButtonAttributes(buttonTopRight, buttonsTable, buttonWidth, buttonHeight, renderable.getConversationButtonTopRight().isDefaultButtonSkin());
        buttonsTable.row();
        buttonsTable.add().height(buttonHeight/2).colspan(3);
        buttonsTable.row();
        buttonBottomLeft = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonBottomLeft());
        setButtonAttributes(buttonBottomLeft, buttonsTable, buttonWidth, buttonHeight, renderable.getConversationButtonBottomLeft().isDefaultButtonSkin());
        buttonsTable.add().height(buttonHeight).width(0.05f * width);
        buttonBottomRight = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonBottomRight());
        setButtonAttributes(buttonBottomRight, buttonsTable, buttonWidth, buttonHeight, renderable.getConversationButtonBottomRight().isDefaultButtonSkin());
        centralTable.add(buttonsTable).width(buttonWidth);
        add(centralTable).height(height * 0.95f).width(width * 0.95f).center();

        add().height(height).width(0.01f * width);

    }

    protected void setButtonAttributes(Button button, Table buttonsTable, float buttonWidth, float buttonHeight, boolean defaultButtonSkin) {
        ThemeController themeController = ThemeController.getInstance();
        Skin skinToDraw = getSkin();
        if(!defaultButtonSkin)
            skinToDraw = themeController.getWrongAnswerSkin();
        button.setStyle(skinToDraw.get("default", TextButton.TextButtonStyle.class));
        buttonsTable.add(button).width(buttonWidth).height(buttonHeight);
    }

    @Override
    public void update(Renderable renderable) {
        MultipleChoiceConversationRenderable conversationRenderable = (MultipleChoiceConversationRenderable)renderable;
    }
}
