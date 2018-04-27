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
import org.scify.engine.renderables.*;
import org.scify.engine.renderables.effects.libgdx.LGDXContainerStack;
import org.scify.moonwalker.app.ui.ComponentFactory;
import org.scify.moonwalker.app.ui.UnsupportedRenderableTypeException;
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
            case "main_menu":
                toReturn = createMainMenuActor((MainMenuRenderable) renderable);
                break;
            case "room":
                toReturn = createRoomActor((RoomRenderable) renderable);
                break;
            case "forest":
                toReturn = createForestActor((ForestRenderable) renderable);
                break;
            case "cockpit":
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case "rotatable_text_button":
                gParent = new LGDXContainerStack();
                gParent.setTransform(true);
                TextButton tbBtn =  createTextButton((ActionButtonWithEffect) renderable);
                tbBtn.setWidth(renderable.getWidth());
                tbBtn.setHeight(renderable.getHeight());
                gParent.add(tbBtn);
                toReturn = gParent;
                break;
            case "text_button":
                toReturn = createTextButton((ActionButtonWithEffect) renderable);
                break;
            case "image_button":
                toReturn = createImageButton((ActionButtonWithEffect) renderable);
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
            case "contact_screen":
                toReturn = createContactScreenActor((ContactScreenRenderable) renderable);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }

    protected ImageWithEffect createImage(String imgFileRelevantPath, Renderable renderable) {
        ImageWithEffect img = new ImageWithEffect(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imgFileRelevantPath)))));

        if (renderable.getWidth() == 0 && renderable.getHeight() == 0) {
            // Delegate image size to renderable
            renderable.setWidth(img.getWidth());
            renderable.setHeight(img.getHeight());
        }
        img.setSize(renderable.getWidth(), renderable.getHeight());
        return img;
    }

    protected Button createButton(ActionButtonWithEffect button) {
        if (button.getType().equals("image_button"))
            return createImageButton(button);
        else if (button.getType().equals("text_button"))
            return createTextButton(button);
        return null;
    }

    protected TextButtonWithEffect createTextButton(ActionButtonWithEffect actionButtonWithEffect) {
        TextButtonWithEffect btn = new TextButtonWithEffect(actionButtonWithEffect.getTitle(), skin);
        setCommonAttrsAndListener(btn, actionButtonWithEffect);
        return btn;
    }

    protected ImageButtonWithEffect createImageButton(ActionButtonWithEffect actionButtonWithEffect) {
        Drawable btnImage = new SpriteDrawable(new Sprite(new Texture(resourceLocator.getFilePath(actionButtonWithEffect.getImgPath()))));
        ImageButtonWithEffect btn = new ImageButtonWithEffect(btnImage);
        setCommonAttrsAndListener(btn, actionButtonWithEffect);
        return btn;
    }

    protected void setCommonAttrsAndListener(Button btn, ActionButtonWithEffect actionButtonWithEffect) {
        setButtonDimensions(actionButtonWithEffect, btn);
        btn.setName(actionButtonWithEffect.getId());
        addButtonListener(btn, actionButtonWithEffect);
    }

    protected void setButtonDimensions(ActionButtonWithEffect actionButtonWithEffect, Button btn) {
        btn.setPosition(actionButtonWithEffect.getxPos(), actionButtonWithEffect.getyPos());
        if (actionButtonWithEffect.getWidth() != 0)
            btn.setWidth(actionButtonWithEffect.getWidth());
        if (actionButtonWithEffect.getHeight() != 0)
            btn.setHeight(actionButtonWithEffect.getHeight());
        btn.pad(actionButtonWithEffect.getPadding());
    }

    private MainMenuActor createMainMenuActor(MainMenuRenderable renderable) {
        MainMenuActor actor = new MainMenuActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createRoomActor(final RoomRenderable renderable) {
        RoomActor actor = new RoomActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createForestActor(final ForestRenderable renderable) {
        ForestActor actor = new ForestActor(skin, renderable);
        actor.setZIndex(0);
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
        for (ActionButtonWithEffect button : renderable.getButtons()) {
            actor.addButton(createButton(button), btnIndex);
            btnIndex++;
        }
        return actor;
    }

    private Actor createTwoChoiceConversationActor(TwoChoiceConversationRenderable renderable) {
        TwoChoiceConversationActor actor = new TwoChoiceConversationActor(skin, renderable);
        actor.setZIndex(1);
        java.util.List<ActionButtonWithEffect> buttons = renderable.getButtons();
        actor.init(createButton(buttons.get(0)), createButton(buttons.get(1)));
        return actor;
    }

    private void addButtonListener(Button button, final ActionButtonWithEffect actionButtonWithEffect) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                userInputHandler.addUserAction(actionButtonWithEffect.getUserAction());
            }
        });
    }

}
