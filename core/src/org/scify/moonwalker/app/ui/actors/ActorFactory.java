package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.*;
import org.scify.moonwalker.app.ui.ComponentFactory;
import org.scify.engine.renderables.UnsupportedRenderableTypeException;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
import org.scify.moonwalker.app.ui.actors.episode.ChargeEpisodeActor;
import org.scify.moonwalker.app.ui.actors.conversation.MultipleChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.conversation.SingleChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.conversation.TwoChoiceConversationActor;
import org.scify.moonwalker.app.ui.actors.episode.*;
import org.scify.moonwalker.app.ui.renderables.*;
import org.scify.moonwalker.app.ui.sprites.SpriteWithEffects;

public class ActorFactory extends ComponentFactory {

    // USE AS SINGLETON
    protected static ActorFactory factory = null;

    public static ActorFactory getInstance() {
        return factory;
    }

    public static ActorFactory getInstance(Skin skin, LGDXRenderableBookKeeper bookKeeper) {
        factory = new ActorFactory(skin,bookKeeper);
        return factory;
    }
    // USE AS SINGLETON - END

    private ActorFactory(Skin skin, LGDXRenderableBookKeeper bookKeeper) {
        super(skin, bookKeeper);
    }

    public Actor createResourceForType(Renderable renderable) {
        Actor toReturn;
        switch (renderable.getType()) {
            case Renderable.ACTOR_LABEL:
                Label label = new ActorLabelWithEffect("", skin);
                label.setWidth(renderable.getWidth());
                label.setHeight(renderable.getHeight());
                label.setWrap(true);
                label.setText(((TextLabelRenderable) renderable).getLabel());
                toReturn = label;
                break;
            case Renderable.ACTOR_ROTATABLE_LABEL:
                Stack gParent = new StackWithEffect<ActorLabelWithEffect>();
                gParent.setTransform(true);
                label = new ActorLabelWithEffect(((TextLabelRenderable) renderable).getLabel(), skin);
                gParent.setWidth(renderable.getWidth());
                gParent.setHeight(renderable.getHeight());
                gParent.setZIndex(renderable.getZIndex());
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
                TextButton tbBtn = createTextButton((ActionButtonRenderable) renderable);
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
            case Renderable.ACTOR_EPISODE_INTRO:
                toReturn = createIntroActor((IntroRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_ROOM:
                toReturn = createRoomActor((RoomRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_DREAMING_ROOM:
                toReturn = createDreamingRoomActor((DreamingRoomRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_FOREST:
                toReturn = createForestActor((ForestRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_MOON_LANDING:
                toReturn = createMoonLandingActor((MoonLandingRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_CREDITS:
                toReturn = createCreditsActor((CreditsRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_FULL_IMAGE:
                toReturn = createFullImageActor((FullImageRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_COCKPIT:
                toReturn = createCockpitActor((CockpitRenderable) renderable);
                break;
            case Renderable.ACTOR_EPISODE_CHARGER:
                toReturn = createChargeEpisodeActor((ChargeEpisodeRenderable) renderable);
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
            case Renderable.ACTOR_EPISODE_LOCATION:
                toReturn = createLocationActor((LocationRenderable) renderable);
                break;
            case Renderable.ACTOR_TABLE:
                toReturn = createTableActor((TableRenderable) renderable);
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
        Texture texture = bookKeeper.getTexture(imgFileRelevantPath);
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        ImageWithEffect img = new ImageWithEffect(textureRegionDrawable, resourceLocator);

        if (renderable.getWidth() == 0 && renderable.getHeight() == 0) {
            // Delegate image size to renderable
            renderable.setWidth(img.getWidth());
            renderable.setHeight(img.getHeight());
        }
        img.setSize(renderable.getWidth(), renderable.getHeight());
        img.setX(renderable.getxPos());
        img.setY(renderable.getyPos());
        img.setZIndex(renderable.getZIndex());
        return img;
    }

    protected TextButtonWithEffect createTextButton(ActionButtonRenderable actionButtonRenderable) {
        TextButtonWithEffect btn = new TextButtonWithEffect(actionButtonRenderable.getTitle(), skin);
        setCommonAttrsAndListener(btn, actionButtonRenderable);
        return btn;
    }

    protected ImageButtonWithEffect createImageButton(ActionButtonRenderable actionButtonRenderable) {
        Texture texture = bookKeeper.getTexture(actionButtonRenderable.getImgPath());
        Sprite sprite = new Sprite(texture);
        Drawable btnImage = new SpriteDrawable(sprite);
        ImageButtonWithEffect btn = new ImageButtonWithEffect(btnImage);
        setCommonAttrsAndListener(btn, actionButtonRenderable);
        return btn;
    }

    protected void setCommonAttrsAndListener(Button btn, ActionButtonRenderable actionButtonRenderable) {
        setButtonDimensions(actionButtonRenderable, btn);
        btn.setName(actionButtonRenderable.getId());
        btn.setZIndex(actionButtonRenderable.getZIndex());
        addButtonListener(btn, actionButtonRenderable);
    }

    protected void setButtonDimensions(ActionButtonRenderable actionButtonRenderable, Button btn) {
        if (actionButtonRenderable.getWidth() != 0)
            btn.setWidth(actionButtonRenderable.getWidth());
        if (actionButtonRenderable.getHeight() != 0)
            btn.setHeight(actionButtonRenderable.getHeight());
        btn.setPosition(actionButtonRenderable.getxPos(), actionButtonRenderable.getyPos());
        btn.pad(actionButtonRenderable.getPadding());
    }

    private MainMenuActor createMainMenuActor(MainMenuRenderable renderable) {
        MainMenuActor actor = new MainMenuActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createIntroActor(final IntroRenderable renderable) {
        IntroActor actor = new IntroActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createRoomActor(final RoomRenderable renderable) {
        RoomActor actor = new RoomActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createDreamingRoomActor(final DreamingRoomRenderable renderable) {
        DreamingRoomActor actor = new DreamingRoomActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createForestActor(final ForestRenderable renderable) {
        ForestActor actor = new ForestActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createMoonLandingActor(final MoonLandingRenderable renderable) {
        MoonLandingActor actor = new MoonLandingActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createCreditsActor(final CreditsRenderable renderable) {
        CreditsActor actor = new CreditsActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createFullImageActor(final FullImageRenderable renderable) {
        FullImageActor actor = new FullImageActor(skin, renderable);
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

    protected Actor createSpaceshipInventoryControllerActor(final SpaceshipInventoryRenderable renderable) {
        SpaceshipInventoryActor actor = new SpaceshipInventoryActor(skin, renderable);
        actor.setZIndex(0);
        return actor;
    }

    private Actor createMapEpisodeActor(MapEpisodeRenderable renderable) {
        MapEpisodeActor mapEpisodeActor = new MapEpisodeActor(skin, renderable);
        return mapEpisodeActor;
    }

    private Actor createLocationActor(LocationRenderable renderable) {
        LocationActor actor = new LocationActor(skin, renderable);
        return actor;
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
        actor.setZIndex(100);
        return actor;
    }

    private void addButtonListener(Button button, final ActionButtonRenderable actionButtonRenderable) {
        button.addListener(new ChangeListener() {
            @Override
            public synchronized void changed(ChangeEvent event, Actor actor) {
                // If already clicked once, and only once is allowed
                if (actionButtonRenderable.isOneClickAllowed() && actionButtonRenderable.isClickedOnce()) {
                    // DEBUG LINES
                    // Gdx.app.log("UI", "Ignoring click after the first on single click button...");
                    //////////////
                    return; // Do nothing
                }
                // Else send the event
                userInputHandler.addUserAction(actionButtonRenderable.getUserAction());
                // and update the renderable appropriately
                actionButtonRenderable.setClickedOnce(true);
            }
        });
    }

    public void dispose() {
        factory = null;
    }
}

