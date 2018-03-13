package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.conversation.MultipleConversationLines;
import org.scify.engine.conversation.SingleConversationLine;
import org.scify.moonwalker.app.ui.actors.*;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.calculator.CalculatorComponent;
import org.scify.moonwalker.app.ui.renderables.*;

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
            case "girl":
                toReturn = createImage("img/girl.png", renderable);
                break;
            case "image":
                toReturn = createImage(renderable.getImgPath(), renderable);
                break;
            case "cockpit":
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case "avatar_selection":
                toReturn = createAvatarSelectionActor((AvatarSelectionRenderable) renderable);
                break;
            case "buttons_list_vertical":
                toReturn = createVerticalButtonList((ButtonsListRenderable) renderable);
                break;
            case "text_button":
                toReturn = createTextButton((ActionButton) renderable);
                break;
            case "image_button":
                toReturn = createImageButton((ActionButton) renderable);
                break;
            case "calculator":
                toReturn = new CalculatorComponent(skin);
                break;
            case "spaceship_charger":
                toReturn = createSpaceshipControllerActor((SpaceshipChargerRenderable) renderable);
                break;
            case "map_location":
                toReturn = createMapLocationActor((MapLocationRenderable) renderable);
                break;
            case "single_conversation_line":
                toReturn = createSingleConversationLineActor((SingleConversationLine) renderable);
                break;
            case "multiple_conversation_lines":
                toReturn = createMultipleSelectionActor((MultipleConversationLines) renderable);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }


    protected Image createImage(String imgFileRelevantPath, Renderable renderable) {
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgFileRelevantPath)))));
        img.setSize(renderable.getWidth(), renderable.getHeight());
        return img;
    }

    protected Button createButton(ActionButton button) {
        if(button.getType().equals("image_button"))
            return createImageButton(button);
        else if (button.getType().equals("text_button"))
            return createTextButton(button);
        return null;
    }

    protected TextButton createTextButton(ActionButton actionButton) {
        TextButton btn = new TextButton(actionButton.getTitle(), skin);
        setCommonAttrsAndListener(btn, actionButton);
        return btn;
    }

    protected ImageButton createImageButton(ActionButton actionButton) {
        Drawable btnImage = new SpriteDrawable(new Sprite(new Texture(resourceLocator.getFilePath(actionButton.getImgPath()))));
        ImageButton btn = new ImageButton(btnImage);
        setCommonAttrsAndListener(btn, actionButton);
        return btn;
    }

    protected void setCommonAttrsAndListener(Button btn, ActionButton actionButton) {
        setButtonDimensions(actionButton, btn);
        btn.setName(actionButton.getId());
        addButtonListener(btn, actionButton);
    }

    protected void setButtonDimensions(ActionButton actionButton, Button btn) {
        btn.setPosition(actionButton.getxPos(), actionButton.getyPos());
        if(actionButton.getWidth() != 0)
            btn.setWidth(actionButton.getWidth());
        if(actionButton.getHeight() != 0)
            btn.setHeight(actionButton.getHeight());
        btn.pad(actionButton.getPadding());
    }

    private Actor createCockpitActor(final CockpitRenderable renderable) {
        CockpitActor2 actor = new CockpitActor2(skin, renderable);

        Button launchBtn = createButton(renderable.getNavigateButton());
        addButtonListener(launchBtn, renderable.getNavigateButton());
        Button launch2Btn = createButton(renderable.getNavigateButton());
        addButtonListener(launch2Btn, renderable.getLaunchButton());
        Button chargeEpisodeBtn = createButton(renderable.getChargeButton());
        addButtonListener(chargeEpisodeBtn, renderable.getChargeButton());

        actor.setPosition(renderable.getxPos(), renderable.getyPos());
        actor.setNavigateButton(launchBtn);
        actor.setLaunchButton(launch2Btn);
        actor.setChargeEpisodeButton(chargeEpisodeBtn);
        actor.setMapButton(createButton(renderable.getMapButton()));
        actor.setSpaceshipPartsButton(createButton(renderable.getSpaceshipPartsButton()));
        actor.setContactButton(createButton(renderable.getContactButton()));
        actor.init();
        return actor;
    }

    private Actor createAvatarSelectionActor(final AvatarSelectionRenderable renderable) {
        AvatarSelectionActor actor = new AvatarSelectionActor(skin);
        Button boyBtn = createButton(renderable.getBoySelection());
        Button girlBtn = createButton(renderable.getGirlSelection());
        Button selectionBtn = createButton(renderable.getSelectBtn());

        actor.addButton(boyBtn);
        actor.addButton(selectionBtn);
        actor.addButton(girlBtn);
        return actor;
    }

    private ButtonList createVerticalButtonList(ButtonsListRenderable buttons) {
        ButtonList list = new ButtonList(skin, true);
        list.addMainLabel("Select an Action");
        for(final ActionButton button : buttons.getButtonList()) {
            list.addButton(createResourceForType(button));
        }
        return list;
    }

    protected Actor createSpaceshipControllerActor(final SpaceshipChargerRenderable renderable) {
        SpaceshipChargerActor spaceshipControllerActor = new SpaceshipChargerActor(skin, renderable);
        spaceshipControllerActor.setCalculatorButton(createButton(renderable.getCalculatorButton()));
        spaceshipControllerActor.setChargeButton(createButton(renderable.getChargeButton()));
        spaceshipControllerActor.setEscapeButton(createButton(renderable.getEscapeButton()));
        spaceshipControllerActor.addSubTables();
        return spaceshipControllerActor;
    }

    private Actor createMapLocationActor(MapLocationRenderable renderable) {
        MapLocationActor mapLocationActor = new MapLocationActor(skin, renderable, createButton(renderable.getButton()));
        return mapLocationActor;
    }

    private Actor createSingleConversationLineActor(SingleConversationLine conversationLine) {
        AvatarWithMessageActor actor = new AvatarWithMessageActor(skin, conversationLine);
        actor.setZIndex(1);
        actor.setButton(createButton(conversationLine.getButtonNext()));
        return actor;
    }

    private Actor createMultipleSelectionActor(MultipleConversationLines multipleConversationLines) {
        MultipleSelectionActor actor = new MultipleSelectionActor(skin, multipleConversationLines);
        actor.setZIndex(1);
        int btnIndex = 1;
        for(ActionButton button : multipleConversationLines.getButtons()) {
            actor.addButton(createButton(button), btnIndex);
            btnIndex++;
        }
        return actor;
    }

    private void addButtonListener(Button button, final ActionButton actionButton) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                userInputHandler.addUserAction(actionButton.getUserAction());
            }
        });
    }
}
