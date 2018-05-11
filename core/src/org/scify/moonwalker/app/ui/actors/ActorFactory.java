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
import org.scify.moonwalker.app.ui.actors.calculator.ChargeEpisodeActor;
import org.scify.moonwalker.app.ui.actors.conversation.MultipleChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.conversation.SingleChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.conversation.TwoChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.episode.*;
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
            case Renderable.ACTOR_LABEL:
                Label label = new ActorLabelWithEffect("", skin);
                label.setWidth(renderable.getWidth());
                label.setHeight(renderable.getHeight());
                label.setWrap(true);
                label.setText(((TextLabelRenderable)renderable).getLabel());
                toReturn = label;
                break;
            case Renderable.ACTOR_ROTATABLE_LABEL:
                Stack gParent = new StackWithEffect<ActorLabelWithEffect>();
                gParent.setTransform(true);
                label = new ActorLabelWithEffect(((TextLabelRenderable)renderable).getLabel(), skin);
                gParent.setWidth(renderable.getWidth());
                gParent.setHeight(renderable.getHeight());
                label.setWrap(true);
                gParent.add(label);
                toReturn = gParent;
                break;
            case Renderable.ACTOR_IMAGE:
                toReturn = createImage(renderable.getImgPath(), renderable);
                break;
            case Renderable.ACTOR_ROTATABLE_TEXT_BUTTON:
                gParent = new StackWithEffect<TextButtonWithEffect>();
                gParent.setTransform(true);
                TextButton tbBtn =  createTextButton((ActionButtonRenderable) renderable);
                tbBtn.setWidth(renderable.getWidth());
                tbBtn.setHeight(renderable.getHeight());
                gParent.add(tbBtn);
                toReturn = gParent;
                break;
            case Renderable.ACTOR_TEXT_BUTTON:
                toReturn = createTextButton((ActionButtonRenderable) renderable);
                break;
            case Renderable.ACTOR_IMAGE_BUTTON:
                toReturn = createImageButton((ActionButtonRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_MAIN_MENU:
                toReturn = createMainMenuActor((MainMenuRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_ROOM:
                toReturn = createRoomActor((RoomRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_FOREST:
                toReturn = createForestActor((ForestRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_COCKPIT:
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_CALCULATOR:
                toReturn = createChargeEpisodeActor((ChargeEpisodeRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_SPACESHIP_CHARGER:
                toReturn = createSpaceshipChargerControllerActor((SpaceshipChargerRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_SPACESHIP_INVENTORY:
                toReturn = createSpaceshipInventoryControllerActor((SpaceshipInventoryRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_MAP_LOCATION:
                toReturn = createMapEpisodeActor((MapEpisodeRenderable) renderable);
                break;
            case Renderable.CONVERSATION_SINGLE_CHOICE:
                toReturn = createNextConversationActor((SingleChoiceConversationRenderable) renderable);
                break;
            case Renderable.CONVERSATION_MULTIPLE_CHOICE:
                toReturn = createMultipleChoiceConversationActor((MultipleChoiceConversationRenderable) renderable);
                break;
            case Renderable.CONVERSATION_TWO_CHOICE:
                toReturn = createTwoChoiceConversationActor((TwoChoiceConversationRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_CONTACT_SCREEN:
                toReturn = createContactScreenActor((ContactScreenRenderable) renderable);
                break;
            case Renderable.ACTOR_TABLE:
                toReturn = createTableActor((TableRenderable)renderable);
                break;
            default:
                throw new UnsupportedRenderableTypeException("renderable with type " + renderable.getType() + " is unsupported.");
        }
        return toReturn;
    }

    protected ChargeEpisodeActor createChargeEpisodeActor(ChargeEpisodeRenderable renderable) {
        return new ChargeEpisodeActor(skin, renderable);
    }

    protected Actor createTableActor(TableRenderable renderable) {
        TableActor taRes = new TableActor<>(skin, renderable);

        return taRes;
    }

    protected ImageWithEffect createImage(String imgFileRelevantPath, Renderable renderable) {
        // TODO: Fix. Something is wrong with the image
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
        if (button.getType().equals(Renderable.ACTOR_IMAGE_BUTTON))
            return createImageButton(button);
        else if (button.getType().equals(Renderable.ACTOR_TEXT_BUTTON))
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
        actor.setZIndex(0);
        return actor;
    }

    private Actor createContactScreenActor(final ContactScreenRenderable renderable) {
        ContactScreenActor actor = new ContactScreenActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    protected Actor createSpaceshipChargerControllerActor(final SpaceshipChargerRenderable renderable) {
        SpaceshipChargerActor spaceshipControllerActor = new SpaceshipChargerActor(skin, renderable);
        spaceshipControllerActor.setCalculatorButton(createButton(renderable.getCalculatorButton()));
        spaceshipControllerActor.setChargeButton(createButton(renderable.getChargeButton()));
        spaceshipControllerActor.setEscapeButton(createButton(renderable.getEscapeButton()));
        spaceshipControllerActor.addSubTables();
        return spaceshipControllerActor;
    }

    protected Actor createSpaceshipInventoryControllerActor(final SpaceshipInventoryRenderable renderable) {
        SpaceshipInventoryActor actor = new SpaceshipInventoryActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createMapEpisodeActor(MapEpisodeRenderable renderable) {
        MapEpisodeActor mapEpisodeActor = new MapEpisodeActor(skin, renderable);
        return mapEpisodeActor;
    }

    private Actor createNextConversationActor(SingleChoiceConversationRenderable renderable) {
        SingleChoiceConversationActor actor = new SingleChoiceConversationActor(skin, renderable);
        actor.setZIndex(100);
        return actor;
    }

    private Actor createTwoChoiceConversationActor(TwoChoiceConversationRenderable renderable) {
        TwoChoiceConversationActor actor = new TwoChoiceConversationActor(skin, renderable);
        actor.setZIndex(100);
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

    private void addButtonListener(Button button, final ActionButtonRenderable actionButtonRenderable) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                userInputHandler.addUserAction(actionButtonRenderable.getUserAction());
            }
        });
    }

}

