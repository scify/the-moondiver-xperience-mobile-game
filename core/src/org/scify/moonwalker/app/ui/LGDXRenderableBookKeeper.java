package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.scify.engine.UserInputHandler;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.engine.renderables.effects.LGDXIgnorableEffect;
import org.scify.engine.renderables.effects.libgdx.EffectNotRegisteredException;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectFactory;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.ui.actors.ActorFactory;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.util.*;

/**
 * This class undertakes the task of keeping the mappings between all non-LGDX renderables, effects, etc. to their
 * LGDX counterparts.
 */
public class LGDXRenderableBookKeeper {

    protected Map<Renderable, Sprite> renderableSpriteMap;
    protected Map<Renderable, Actor> renderableActorMap;
    protected ComponentFactory<Actor> actorFactory;
    protected ComponentFactory<Sprite> spriteFactory;
    protected LGDXEffectFactory<LGDXEffect> effectFactory;
    protected UserInputHandlerImpl userInputHandler;

//    protected Map<Actor, Map<Effect, LGDXEffect>> actorEffects;
//    protected Map<Sprite, Map<Effect, LGDXEffect>> spriteEffects;

    protected Batch batch;
    protected Stage stage;

    public LGDXRenderableBookKeeper(ThemeController themeController, UserInputHandler userInputHandler) {
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        actorFactory = ActorFactory.getInstance(themeController.getSkin());
        actorFactory.setUserInputHandler(userInputHandler);
        this.spriteFactory = new SpriteFactory(themeController.getSkin());
        this.effectFactory = LGDXEffectFactory.getFactorySingleton();
    }

    /**
     * Singleton pattern variable.
     */
    protected static LGDXRenderableBookKeeper instance;

    /**
     * Initializes the LGDXRenderableBookKeeper with a given environment (theme controller, user input handler)
     * @param themeController The controller.
     * @param userInputHandler The user input handler.
     * @return The bookkeeper instance created.
     */
    public static LGDXRenderableBookKeeper initBookKeeper(ThemeController themeController, UserInputHandler userInputHandler) throws AlreadyInitializedBookKeeperException {
        if (instance == null) {
            instance = new LGDXRenderableBookKeeper(themeController, userInputHandler);
            return instance;
        }
        else
            throw new AlreadyInitializedBookKeeperException("Bookkeeper already initialized.");

    }


    /**
     * Returns the singleton instance of bookkeeper, if it was initialized before, through a call to initBookKeeper.
     * Throws a runtime {@link UninitializedBookKeeperException}, if the keeper was not initialized.
     * @return The {@link LGDXRenderableBookKeeper}.
     */
    public static LGDXRenderableBookKeeper getInstance() {
        if (instance == null)
            throw new UninitializedBookKeeperException("Should first call init book keeper.");

        return instance;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns an LGDX equivalent of the renderable, also updating any applied effects.
     * If an equivalent has not been created, it also creates it.
     * @param renderable The renderable we want to return.
     * @return The updated LGDX equivalent of the renderable.
     */
    public Object getUIRepresentationOfRenderable(Renderable renderable) {
        Object oRes;
        if (!renderableUIRepresentationExists(renderable)) {
            oRes = createAndAddLGDXObjectForRenderable(renderable);
        } else {
            if (renderableActorMap.containsKey(renderable))
                oRes = renderableActorMap.get(renderable);
            else
                oRes = renderableSpriteMap.get(renderable);
        }

        // Update effects
        updateEffectList(renderable, (EffectTarget) oRes);

        return oRes;
    }

    protected Object createAndAddLGDXObjectForRenderable(Renderable renderable) {
        // Try to create as sprite
        Sprite sprite = createSpriteResourceFor(renderable);
        // If it does not fail
        if (sprite != null) {
            // Update sprite map
            renderableSpriteMap.put(renderable, sprite);

            return sprite;
        } else {
            // else we create as an actor
           return createActorResourceFor(renderable);
        }
    }

    public LGDXEffect getOrCreateLGDXEffectForRenderable(Effect eCur, Renderable renderable) {
        try {
            LGDXEffect lgeCur = effectFactory.getEffectFor(eCur);
            // Update actor with new effect
            renderable.addEffect(lgeCur);
            return lgeCur;
        } catch (EffectNotRegisteredException e) {
            // If NOT an ignorable effect (i.e. not a meta-effect or similar)
            if (!(eCur instanceof LGDXIgnorableEffect)) {
                e.printStackTrace();
                System.err.println("Ignoring...");
            }
        }

        return null;

    }


    protected Sprite createSpriteResourceFor(Renderable toDraw) {
        Sprite resource = null;
        try {
            // createSpriteResourceForType
            Sprite newResourceForRenderable = spriteFactory.createResourceForType(toDraw);
            if(newResourceForRenderable != null) {
                resource = newResourceForRenderable;
            }
        } catch (UnsupportedRenderableTypeException e) {
            e.printStackTrace();
        }
        return resource;
    }

    protected Actor createActorResourceFor(final Renderable toDraw) {
        return createActorResourceFor(toDraw, actorFactory);
    }


    protected Actor createActorResourceFor(final Renderable toDraw, ComponentFactory<Actor> cfFactory) {
        Actor resource = null;
        try {
            Actor newActorForRenderable = cfFactory.createResourceForType(toDraw);
            if(newActorForRenderable != null) {
                resource = newActorForRenderable;
                addActor(toDraw, newActorForRenderable);

            }
        } catch (UnsupportedRenderableTypeException e) {
            e.printStackTrace();
        }
        return resource;
    }

    protected Actor addActor(final Renderable toDraw, Actor newActorForRenderable) {
        addClickListenerIfButton(toDraw, newActorForRenderable);
        renderableActorMap.put(toDraw, newActorForRenderable);
        return  newActorForRenderable;
    }

    protected void addClickListenerIfButton(final Renderable toDraw, Actor newActorForRenderable) {
        if(toDraw.getType().equals("text_button") || toDraw.getType().equals("image_button")) {
            userInputHandler.addClickListenerForActor((ActionButtonRenderable) toDraw, newActorForRenderable);
        }
    }

    public void reset() {
        renderableSpriteMap = new HashMap<>();
        renderableActorMap = new HashMap<>();
    }

    public void dispose() {
        synchronized (stage) {
            // Dispose of actors
            for(Actor actor : stage.getActors()) {
                System.out.println("removing: " + actor.getName());
                actor.addAction(Actions.removeActor());
            }

            // Dispose of sprites (?)
            for (Sprite s : renderableSpriteMap.values()) {
                try {
                    s.getTexture().dispose();
                } catch (Exception e) {
                    System.err.println("Could not dispose of texture. Ignoring... Error as follows:");
                    e.printStackTrace(System.err);
                }
            }

            reset();
        }
    }

    public boolean renderableUIRepresentationExists(Renderable renderable) {
        return renderableExistsAsSprite(renderable) || renderableExistsAsActor(renderable);
    }

    public boolean renderableExistsAsSprite(Renderable renderable) {
        return renderableSpriteMap.containsKey(renderable);
    }

    public boolean renderableExistsAsActor(Renderable renderable) {
        return renderableActorMap.containsKey(renderable);
    }

    public Actor getOrCreateActorResourceFor(Renderable renderable) {
        Actor toReturn;
        if(renderableExistsAsActor(renderable))
            toReturn = renderableActorMap.get(renderable);
        else
            toReturn = createActorResourceFor(renderable);

        // update actor effects, if any
        if (toReturn instanceof EffectTarget) {
            updateEffectList(renderable, (EffectTarget) toReturn);
        }

        return toReturn;
    }

    public void printActors() {
        System.out.println("printActors");
        for(Actor stageActor : stage.getActors()) {
            System.out.println("Actor " + stageActor.getClass() + " " + stageActor.getName() + " " + stageActor.getZIndex());
        }
    }

    public Sprite getSpriteForRenderable(Renderable renderable) {
        return renderableSpriteMap.get(renderable);
    }

    public void updateEffectList(Renderable renderable, EffectTarget target) {
        List<Effect> toRemove = new ArrayList<>();
        // Get renderable effects
        Set<Effect> lEffects = new HashSet<>(renderable.getEffects());


        // For each effect on the renderable
        for (Effect eCur: lEffects) {
            // Mark effect for removal, if complete
            if (eCur.complete())
                toRemove.add(eCur);
            else {
                // If not an LGDX effect
                if (!(eCur instanceof LGDXEffect)) {
                    getOrCreateLGDXEffectForRenderable(eCur, renderable);
                    // Remove source effect from renderable, since we replaced it
                    renderable.removeEffect(eCur);
                }
            }
        }

        // For each completed effect
        for (Effect eToRemove: toRemove) {
            // DEBUG LINES
//             System.err.println("Removing " + eToRemove + " from " + renderable.toString());
            //////////////
            // Remove it
            renderable.removeEffect(eToRemove);
//            removeEffectFromRenderableAndRelatedUIElements(eToRemove, renderable);
        }
    }

    public void setCustomActorForRenderable(Renderable rCountdownTable, Actor aTable) {
        addActor(rCountdownTable, aTable);
    }

//    protected void propagateEffectToChildren(Actor aParent, Renderable rParent, Effect eCur) {
//        if (eCur.complete())
//            return;
//
//        IContainerActor<Renderable> caToDraw = (IContainerActor)aParent;
//        // For every child
//        Map<Actor,Renderable> mChildren = caToDraw.getChildrenActorsAndRenderables();
//        for (Map.Entry<Actor,Renderable> mCur : mChildren.entrySet()) {
//            // create corresponding LGDX effect for child actor
//            getOrCreateLGDXEffectForActor(eCur, mCur.getKey());
//
//            // If child is a container repeat recursively
//            if (mCur.getKey() instanceof IContainerActor) {
//                propagateEffectToChildren(mCur.getKey(), mCur.getValue(), eCur);
//            }
//        }
//    }


    /**
     * Uninitialized bookkeeper exception.
     */
    public static class UninitializedBookKeeperException extends RuntimeException {
        public UninitializedBookKeeperException(String sMsg) {
            super(sMsg);
        }
    }

    /**
     * Already initialized bookkeeper exception.
     */
    public static class AlreadyInitializedBookKeeperException extends Throwable {
        public AlreadyInitializedBookKeeperException(String sMsg) {
            super(sMsg);
        }
    }
}
