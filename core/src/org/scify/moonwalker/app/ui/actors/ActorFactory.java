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
            case Renderable.LABEL:
                Label label = new LabelWithEffect("", skin);
                label.setWidth(renderable.getWidth());
                label.setHeight(renderable.getHeight());
                label.setWrap(true);
                toReturn = label;
                break;
            case Renderable.ROTATABLE_LABEL:
                Stack gParent = new StackWithEffect();
                gParent.setTransform(true);
                label = new Label(((TextLabelRenderable)renderable).getLabel(), skin);
                gParent.setWidth(renderable.getWidth());
                gParent.setHeight(renderable.getHeight());
                label.setWrap(true);
                gParent.add(label);
                toReturn = gParent;
                break;
            case Renderable.IMAGE:
                toReturn = createImage(renderable.getImgPath(), renderable);
                break;
            case Renderable.MAIN_MENU:
                toReturn = createMainMenuActor((MainMenuRenderable) renderable);
                break;
            case Renderable.ROOM:
                toReturn = createRoomActor((RoomRenderable) renderable);
                break;
            case Renderable.FOREST:
                toReturn = createForestActor((ForestRenderable) renderable);
                break;
            case Renderable.COCKPIT:
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case Renderable.ROTATABLE_TEXT_BUTTON:
                gParent = new StackWithEffect();
                gParent.setTransform(true);
                TextButton tbBtn =  createTextButton((ActionButtonRenderable) renderable);
                tbBtn.setWidth(renderable.getWidth());
                tbBtn.setHeight(renderable.getHeight());
                gParent.add(tbBtn);
                toReturn = gParent;
                break;
            case Renderable.TEXT_BUTTON:
                toReturn = createTextButton((ActionButtonRenderable) renderable);
                break;
            case Renderable.IMAGE_BUTTON:
                // TODO: Check
                toReturn = createImageButton((ActionButtonRenderable) renderable);
                break;
            case Renderable.CALCULATOR:
                toReturn = new CalculatorComponent(skin);
                break;
            case Renderable.SPACESHIP_CHARGER:
                toReturn = createSpaceshipControllerActor((SpaceshipChargerRenderable) renderable);
                break;
            case Renderable.MAP_LOCATION:
                toReturn = createMapEpisodeActor((MapEpisodeRenderable) renderable);
                break;
            case Renderable.NEXT_CONVERSATION:
                toReturn = createNextConversationActor((SingleChoiceConversationRenderable) renderable);
                break;
            case Renderable.MULTIPLE_CHOICE_CONVERSATION:
                toReturn = createMultipleChoiceConversationActor((MultipleChoiceConversationRenderable) renderable);
                break;
            case Renderable.TWO_CHOICE_CONVERSATION:
                toReturn = createTwoChoiceConversationActor((TwoChoiceConversationRenderable) renderable);
                break;
            case Renderable.CONTACT_SCREEN:
                toReturn = createContactScreenActor((ContactScreenRenderable) renderable);
                break;
            case Renderable.TABLE:
                toReturn = createTableActor((TableRenderable)renderable);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }

    private Actor createTableActor(TableRenderable renderable) {
        TableActor taRes = new TableActor<>(skin, renderable);

        return taRes;
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

    protected Button createButton(ActionButtonRenderable button) {
        if (button.getType().equals(Renderable.IMAGE_BUTTON))
            return createImageButton(button);
        else if (button.getType().equals(Renderable.TEXT_BUTTON))
            return createTextButton(button);
        return null;
    }

    protected TextButtonWithEffect createTextButton(ActionButtonRenderable actionButtonRenderable) {
        TextButtonWithEffect btn = new TextButtonWithEffect(actionButtonRenderable.getTitle(), skin);
        setCommonAttrsAndListener(btn, actionButtonRenderable);
        return btn;
    }

    protected ImageButtonWithEffect createImageButton(ActionButtonRenderable actionButtonRenderable) {
        Drawable btnImage = new SpriteDrawable(new Sprite(new Texture(resourceLocator.getFilePath(actionButtonRenderable.getImgPath()))));
        ImageButtonWithEffect btn = new ImageButtonWithEffect(btnImage);
        setCommonAttrsAndListener(btn, actionButtonRenderable);
        return btn;
    }

    protected void setCommonAttrsAndListener(Button btn, ActionButtonRenderable actionButtonRenderable) {
        setButtonDimensions(actionButtonRenderable, btn);
        btn.setName(actionButtonRenderable.getId());
        addButtonListener(btn, actionButtonRenderable);
    }

    protected void setButtonDimensions(ActionButtonRenderable actionButtonRenderable, Button btn) {
        btn.setPosition(actionButtonRenderable.getxPos(), actionButtonRenderable.getyPos());
        if (actionButtonRenderable.getWidth() != 0)
            btn.setWidth(actionButtonRenderable.getWidth());
        if (actionButtonRenderable.getHeight() != 0)
            btn.setHeight(actionButtonRenderable.getHeight());
        btn.pad(actionButtonRenderable.getPadding());
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

    private Actor createMapEpisodeActor(MapEpisodeRenderable renderable) {
        MapEpisodeActor mapEpisodeActor = new MapEpisodeActor(skin, renderable);
        mapEpisodeActor.setZIndex(0);
        return mapEpisodeActor;
    }

    private Actor createNextConversationActor(SingleChoiceConversationRenderable renderable) {
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
        for (ActionButtonRenderable button : renderable.getButtons()) {
            actor.addButton(createButton(button), btnIndex);
            btnIndex++;
        }
        return actor;
    }

    private Actor createTwoChoiceConversationActor(TwoChoiceConversationRenderable renderable) {
        TwoChoiceConversationActor actor = new TwoChoiceConversationActor(skin, renderable);
        actor.setZIndex(1);
        java.util.List<ActionButtonRenderable> buttons = renderable.getButtons();
        actor.init(createButton(buttons.get(0)), createButton(buttons.get(1)));
        return actor;
    }

    private void addButtonListener(Button button, final ActionButtonRenderable actionButtonRenderable) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                userInputHandler.addUserAction(actionButtonRenderable.getUserAction());
            }
        });
    }

}

