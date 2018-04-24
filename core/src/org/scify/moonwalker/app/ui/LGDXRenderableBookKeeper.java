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
import org.scify.engine.renderables.effects.libgdx.EffectNotRegisteredException;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectFactory;
import org.scify.engine.renderables.ActionButtonWithEffect;
import org.scify.moonwalker.app.ui.actors.ActorFactory;
import org.scify.moonwalker.app.ui.actors.IContainerActor;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.security.InvalidParameterException;
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

    protected Map<Actor, Map<Effect, LGDXEffect>> actorEffects;
    protected Map<Sprite, Map<Effect, LGDXEffect>> spriteEffects;

    protected Batch batch;
    protected Stage stage;

    public LGDXRenderableBookKeeper(ThemeController themeController, UserInputHandler userInputHandler) {
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        actorFactory = ActorFactory.getInstance(themeController.getSkin());
        actorFactory.setUserInputHandler(userInputHandler);
        this.spriteFactory = new SpriteFactory(themeController.getSkin());
        this.effectFactory = LGDXEffectFactory.getFactorySingleton();

        this.actorEffects = new HashMap<>();
        this.spriteEffects = new HashMap<>();
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

    public Object createAndAddLGDXObjectForRenderable(Renderable renderable) {
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

    public LGDXEffect getOrCreateLGDXEffectForSprite(Effect eCur, Sprite sToDraw) {
        if (!spriteEffects.containsKey(sToDraw)) {
            spriteEffects.put(sToDraw, new HashMap<Effect, LGDXEffect>());
        }

        // If I have not created a LGDXEffect for the specified effect
        if (!spriteEffects.get(sToDraw).containsKey(eCur)) {
            try {
                // Create the LGDXEffect and update the map
                spriteEffects.get(sToDraw).put(eCur, effectFactory.getEffectFor(eCur));
                // DEBUG INFO
                System.err.println("Added effect for " + eCur.toString() + " on sprite " + sToDraw.toString());
                /////////////

            } catch (EffectNotRegisteredException e) {
                e.printStackTrace();
                System.err.println("Ignoring...");
            }
        }

        LGDXEffect lgeRes = spriteEffects.get(sToDraw).get(eCur);
        // Update actor with new effect
        ((EffectTarget)sToDraw).addEffect(lgeRes);

        return lgeRes;

    }

    public LGDXEffect getOrCreateLGDXEffectForActor(Effect eCur, Actor aToDraw) throws InvalidParameterException {
        if (!(aToDraw instanceof EffectTarget)) {
            // DEBUG LINES
            System.err.println("Actor " + aToDraw.toString() + " is not an EffectTarget. Ignoring...");
            //////////////
            return null;
        }

        if (!actorEffects.containsKey(aToDraw)) {
                actorEffects.put(aToDraw, new HashMap<Effect, LGDXEffect>());
            // DEBUG LINES
            System.err.println("Initialized effect list for actor " + aToDraw.toString() + ".");
            //////////////
        }

        if (!actorEffects.get(aToDraw).containsKey(eCur)) {
            try {
                actorEffects.get(aToDraw).put(eCur, effectFactory.getEffectFor(eCur));
                // DEBUG LINES
                System.err.println("Created new effect for " + eCur.toString() + "," + aToDraw.toString() + ".");
                //////////////
            } catch (EffectNotRegisteredException e) {
                e.printStackTrace();
                System.err.println("Ignoring...");
            }
        }

        LGDXEffect lgeRes = actorEffects.get(aToDraw).get(eCur);
        // Update actor with new effect
        ((EffectTarget)aToDraw).addEffect(lgeRes);

        return lgeRes;
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

                // DEBUG LINES
                printActors();
                /////////////
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
            userInputHandler.addClickListenerForActor((ActionButtonWithEffect) toDraw, newActorForRenderable);
        }
    }

    public void reset() {
        renderableSpriteMap = new HashMap<>();
        renderableActorMap = new HashMap<>();
        actorEffects = new HashMap<>();
        spriteEffects = new HashMap<>();
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

    public synchronized void removeEffectFromRenderableAndRelatedUIElements(Effect eToRemove, Renderable renderable) {
        if(renderableExistsAsSprite(renderable)) {
            Map<Effect, LGDXEffect> mMap = spriteEffects.get(renderableSpriteMap.get(renderable));
            Sprite sToUpdate = renderableSpriteMap.get(renderable);

            if (sToUpdate != null) {
                if (sToUpdate instanceof EffectTarget) {
                    EffectTarget eCur = (EffectTarget)sToUpdate;
                    // Remove effect
                    ((EffectTarget) sToUpdate).removeEffect(eToRemove);
                }
            }
            // Finally remove mapping
            if (mMap != null) {
                spriteEffects.get(renderableSpriteMap.get(renderable)).remove(eToRemove);
            }

        }
        else {
            Map<Effect, LGDXEffect> mMap = actorEffects.get(renderableActorMap.get(renderable));


            Actor aToUpdate = renderableActorMap.get(renderable);
            if (aToUpdate != null) {
                if (aToUpdate instanceof EffectTarget) {
                    EffectTarget eCur = (EffectTarget)aToUpdate;
                    // Remove effect
                    ((EffectTarget) aToUpdate).removeEffect(eToRemove);
                }
            }

            // If a container
            if (aToUpdate instanceof IContainerActor) {
                Map<Actor,Renderable> mChildren = ((IContainerActor<Renderable>)aToUpdate).getChildrenActorsAndRenderables();
                // For each actor child
                for (Map.Entry<Actor,Renderable> meContained : mChildren.entrySet()) {
                    // remove its effects
                    removeEffectFromRenderableAndRelatedUIElements(eToRemove, meContained.getValue());
                }
            }

            // Finally remove mapping fro actorEffects
            if (mMap != null) {
                actorEffects.get(renderableActorMap.get(renderable)).remove(eToRemove);
            }

            // and update renderable in any case
            renderable.removeEffect(eToRemove);
        }

    }

    public Sprite getSpriteForRenderable(Renderable renderable) {
        return renderableSpriteMap.get(renderable);
    }

    public Map<Effect,LGDXEffect> getActorEffectsFor(Actor aToDraw) {
        return actorEffects.get(aToDraw);
    }

    public Map<Effect, LGDXEffect> getSpriteEffectsFor(Sprite sToDraw) {
        return spriteEffects.get(sToDraw);
    }

    /**
     * Returns the effects for the specific actor/sprite.
     * @param uiRepresentationOfRenderable The actor/sprite to look up.
     * @return A map between the effects of the object and their LGDXEffect aspects.
     */
    public Map<Effect, LGDXEffect> getEffectsFor(Object uiRepresentationOfRenderable) {
        Map<Effect, LGDXEffect> mRes = new HashMap<>();

        if ((uiRepresentationOfRenderable instanceof Actor) && actorEffects.containsKey(uiRepresentationOfRenderable)) {
            return actorEffects.get(uiRepresentationOfRenderable);
        }
        if ((uiRepresentationOfRenderable instanceof Sprite) && spriteEffects.containsKey(uiRepresentationOfRenderable)) {
            return spriteEffects.get(uiRepresentationOfRenderable);
        }

        return mRes;
    }

    public void updateEffectList(Renderable renderable, EffectTarget target) {
        List<Effect> toRemove = new ArrayList<>();
        Set<Effect> lEffects = renderable.getEffects();

        // For each effect on the renderable
        for (Effect eCur: lEffects) {
            // Mark effect for removal, if complete
            if (eCur.complete())
                toRemove.add(eCur);
            else {
                // If not an LGDX effect
                if (!(eCur instanceof LGDXEffect)) {
                    if (renderableExistsAsSprite(renderable)) {
                        // Handle as sprite
                        Sprite sCur = (Sprite)target;
                        LGDXEffect leCur = getOrCreateLGDXEffectForSprite(eCur,
                                sCur);
                    }
                    else
                    {
                        // Handle as actor
                        Actor aCur = (Actor)target;
                        // create corresponding LGDX effect for actor
                        getOrCreateLGDXEffectForActor(eCur, aCur);

                        // OBSOLETE: Do NOT propagate to children.
//                        // If container add to children as well
//                        if (aCur instanceof IContainerActor) {
//                            propagateEffectToChildren(aCur, renderable, eCur);
//                        }
                    }
                }
            }
        }

        // For each completed effect
        for (Effect eToRemove: toRemove) {
            // DEBUG LINES
            System.err.println("Removing " + eToRemove + " from " + renderable.toString());
            //////////////
            // Remove it
            removeEffectFromRenderableAndRelatedUIElements(eToRemove, renderable);
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
