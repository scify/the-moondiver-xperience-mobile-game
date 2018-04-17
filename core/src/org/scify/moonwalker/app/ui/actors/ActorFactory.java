package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.engine.renderables.NextConversationRenderable;
import org.scify.engine.renderables.TwoChoiceConversationRenderable;
import org.scify.engine.renderables.effects.libgdx.LGDXContainerStack;
import org.scify.moonwalker.app.ui.ComponentFactory;
import org.scify.moonwalker.app.ui.UnsupportedRenderableTypeException;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.calculator.CalculatorComponent;
import org.scify.moonwalker.app.ui.actors.conversation.MultipleChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.conversation.NextConversationActor;
import org.scify.moonwalker.app.ui.actors.conversation.TwoChoiceConversationActor;
import org.scify.moonwalker.app.ui.renderables.*;

public class ActorFactory extends ComponentFactory {
    // USE AS SINGLETON
    protected static ActorFactory factory = null;

    public static ActorFactory getInstance() {
        return factory;
    }

    public static ActorFactory getInstance(Skin skin) {
        factory = new ActorFactory(skin);
        return factory;
    }
    // USE AS SINGLETON - END

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
            case "rotatable_label":
                Stack gParent = new LGDXContainerStack();
                gParent.setTransform(true);
                label = new Label(((TextLabel)renderable).getLabel(), skin);
                gParent.setWidth(renderable.getWidth());
                gParent.setHeight(renderable.getHeight());
                label.setWrap(true);
                gParent.add(label);
                toReturn = gParent;
                break;
            case "image":
                toReturn = createImage(renderable.getImgPath(), renderable);
                break;
            case "cockpit":
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case "main_menu":
                toReturn = createMainMenuActor((MainMenuRenderable) renderable);
                break;
            case "rotatable_text_button":
                gParent = new LGDXContainerStack();
                gParent.setTransform(true);
                TextButton tbBtn =  createTextButton((ActionButton) renderable);
                tbBtn.setWidth(renderable.getWidth());
                tbBtn.setHeight(renderable.getHeight());
                gParent.add(tbBtn);
                toReturn = gParent;
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
            case "next_conversation":
                toReturn = createNextConversationActor((NextConversationRenderable) renderable);
                break;
            case "multiple_choice_conversation":
                toReturn = createMultipleChoiceConversationActor((MultipleChoiceConversationRenderable) renderable);
                break;
            case "two_choice_conversation":
                toReturn = createTwoChoiceConversationActor((TwoChoiceConversationRenderable) renderable);
                break;
            case "room":
                toReturn = createRoomActor((RoomRenderable) renderable);
                break;
            case "contact_screen":
                toReturn = createContactScreenActor((ContactScreenRenderable) renderable);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }

    protected Image createImage(String imgFileRelevantPath, Renderable renderable) {
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgFileRelevantPath)))));

        if (renderable.getWidth() == 0 && renderable.getHeight() == 0) {
            // Delegate image size to renderable
            renderable.setWidth(img.getWidth());
            renderable.setHeight(img.getHeight());
        }
        img.setSize(renderable.getWidth(), renderable.getHeight());
        return img;
    }

    protected Button createButton(ActionButton button) {
        if (button.getType().equals("image_button"))
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
        if (actionButton.getWidth() != 0)
            btn.setWidth(actionButton.getWidth());
        if (actionButton.getHeight() != 0)
            btn.setHeight(actionButton.getHeight());
        btn.pad(actionButton.getPadding());
    }

    private MainMenuActor createMainMenuActor(MainMenuRenderable renderable) {
        MainMenuActor actor = new MainMenuActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createRoomActor(final RoomRenderable renderable) {
        RoomActor actor = new RoomActor(skin, renderable);
        actor.init();
        return actor;
    }

    private Actor createCockpitActor(final CockpitRenderable renderable) {
        CockpitActor actor = new CockpitActor(skin, renderable);

        Button navigateBtn = createButton(renderable.getNavigateButton());
        addButtonListener(navigateBtn, renderable.getNavigateButton());
        Button launchBtn = createButton(renderable.getLaunchButton());
        addButtonListener(launchBtn, renderable.getLaunchButton());
        Button chargeEpisodeBtn = createButton(renderable.getChargeButton());
        addButtonListener(chargeEpisodeBtn, renderable.getChargeButton());

        actor.setPosition(renderable.getxPos(), renderable.getyPos());
        actor.setNavigateButton(navigateBtn);
        actor.setLaunchButton(launchBtn);
        actor.setChargeEpisodeButton(chargeEpisodeBtn);
        actor.setMapButton(createButton(renderable.getMapButton()));
        actor.setSpaceshipPartsButton(createButton(renderable.getSpaceshipPartsButton()));
        actor.setContactButtons(createButton(renderable.getContactButtonSimple()), createButton(renderable.getContactButtonLighted()));
        actor.init();
        return actor;
    }

    private Actor createContactScreenActor(final ContactScreenRenderable renderable) {
        ContactScreenActor actor = new ContactScreenActor(skin, renderable);

        actor.setPosition(renderable.getxPos(), renderable.getyPos());
        actor.init();
        return actor;
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

    private Actor createNextConversationActor(NextConversationRenderable renderable) {
        NextConversationActor actor = new NextConversationActor(skin, renderable);
        actor.setZIndex(1);
        actor.setButton(createButton(renderable.getButtonNext()));
        actor.init(renderable.getButtonNextStatus());
        return actor;
    }

    private Actor createMultipleChoiceConversationActor(MultipleChoiceConversationRenderable renderable) {
        MultipleChoiceConversationActor actor = new MultipleChoiceConversationActor(skin, renderable);
        //actor.setZIndex(1);
        int btnIndex = 1;
        for (ActionButton button : renderable.getButtons()) {
            actor.addButton(createButton(button), btnIndex);
            btnIndex++;
        }
        return actor;
    }

    private Actor createTwoChoiceConversationActor(TwoChoiceConversationRenderable renderable) {
        TwoChoiceConversationActor actor = new TwoChoiceConversationActor(skin, renderable);
        actor.setZIndex(1);
        java.util.List<ActionButton> buttons = renderable.getButtons();
        actor.init(createButton(buttons.get(0)), createButton(buttons.get(1)));
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
