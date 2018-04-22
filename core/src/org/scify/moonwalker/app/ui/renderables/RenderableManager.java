package org.scify.moonwalker.app.ui.renderables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.scify.engine.UserInputHandler;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.libgdx.EffectNotRegisteredException;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectFactory;
import org.scify.moonwalker.app.ui.ComponentFactory;
import org.scify.moonwalker.app.ui.SpriteFactory;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.UnsupportedRenderableTypeException;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.actors.ActorFactory;
import org.scify.moonwalker.app.ui.actors.IContainerActor;
import org.scify.moonwalker.app.ui.actors.Updateable;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.util.*;

public class RenderableManager {

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

    public RenderableManager(ThemeController themeController, UserInputHandler userInputHandler) {
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        actorFactory = ActorFactory.getInstance(themeController.getSkin());
        actorFactory.setUserInputHandler(userInputHandler);
        this.spriteFactory = new SpriteFactory(themeController.getSkin());
        this.effectFactory = LGDXEffectFactory.getFactorySingleton();

        this.actorEffects = new HashMap<>();
        this.spriteEffects = new HashMap<>();
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void createAndAddRenderable(Renderable renderable) {
        Sprite sprite = createSpriteResourceFor(renderable);
        if (sprite != null) {
            renderableSpriteMap.put(renderable, sprite);
        } else {
           createActorResourceFor(renderable);
        }
    }

    public void drawRenderable(Renderable renderable) {

        if(renderableExistsAsSprite(renderable)) {
            Sprite sToDraw = renderableSpriteMap.get(renderable);
            drawSpriteFromRenderable(renderable, sToDraw);
        } else {
            Actor aToDraw = renderableActorMap.get(renderable);
            drawActorFromRenderable(renderable, aToDraw);
            if (aToDraw.isVisible())
                update(renderable, aToDraw);
            else
                aToDraw.remove();
        }
    }

    protected synchronized void applyActorEffects(Actor aToDraw, Renderable renderable) {
        // Effect application
        /////////////////////
        if (actorEffects.get(aToDraw) == null)
            return;

        // For each effect
        for (Map.Entry<Effect, LGDXEffect> eCur: actorEffects.get(aToDraw).entrySet()) {
            // apply
            eCur.getValue().applyToActor(aToDraw, renderable);
        }
    }

    protected synchronized void applySpriteEffects(Sprite sToDraw, Renderable renderable) {
        // Effect application
        /////////////////////
        if (spriteEffects.get(sToDraw) == null)
            return;

        // For each effect
        for (Map.Entry<Effect, LGDXEffect> eCur: spriteEffects.get(sToDraw).entrySet()) {
            // apply
            eCur.getValue().applyToSprite(sToDraw, renderable);
        }
    }

    public LGDXEffect getOrCreateLGDXEffectForSprite(Effect eCur, Sprite sToDraw) {
        if (!spriteEffects.containsKey(sToDraw)) {
            spriteEffects.put(sToDraw, new HashMap<Effect, LGDXEffect>());
        }

        if (!spriteEffects.get(sToDraw).containsKey(eCur)) {
            try {
                spriteEffects.get(sToDraw).put(eCur, effectFactory.getEffectFor(eCur));
            } catch (EffectNotRegisteredException e) {
                e.printStackTrace();
                System.err.println("Ignoring...");
            }
        }

        return spriteEffects.get(sToDraw).get(eCur);

    }

    public LGDXEffect getOrCreateLGDXEffectForActor(Effect eCur, Actor aToDraw) {
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

        return actorEffects.get(aToDraw).get(eCur);

    }

    protected void update(Renderable renderable, Actor actor) {
        // if the Actor implements the Updatable interface, pass the renderable as argument
        // for it to be updated
        if(actor instanceof Updateable) {
            ((Updateable) actor).update(renderable);
        }
    }

    protected void drawSpriteFromRenderable(Renderable renderable, Sprite sToDraw) {
        // TODO: Check if we should use
        sToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        applySpriteEffects(sToDraw, renderable); // Deal with effects

        // OBSOLETE:
//        batch.draw(sToDraw, sToDraw.getX(), sToDraw.getY(), sToDraw.getWidth(), sToDraw.getHeight());
        if (renderable.isVisible()) {
            // TODO: Examine if all is fine now
            // TODO: Examine if all is fine now
            sToDraw.draw(batch); // Follow this approach, to use sprite all traits (including color, alpha, etc)
        }
    }


    protected void drawActorFromRenderable(Renderable renderable, Actor aToDraw) {
        aToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        applyActorEffects(aToDraw, renderable); // Deal with effects
        applyActorVisibility(aToDraw, renderable); // Apply visibility


        // if actor does not have a stage, it means that
        // it is the first time that it is added to the stage.
        if(aToDraw.getStage() == null) {
            //System.out.println("new actor with name: " + renderable.getId());
            aToDraw.setName(renderable.getId());
            stage.addActor(aToDraw);
        }else {// update the z index of the actor, according to the renderable's z index
            // Take into account also the z-index rule (<0 means invisible)
            if(renderable.getZIndex() >= 0)
                aToDraw.setZIndex(renderable.getZIndex());
            else {
                aToDraw.setVisible(false);
            }
        }
    }

    private void applyActorVisibility(Actor aToDraw, Renderable renderable) {
        // If a container
        if (aToDraw instanceof IContainerActor) {
            IContainerActor<Renderable> caToDraw = (IContainerActor)aToDraw;
            // For every child
            for (Actor aCur: caToDraw.getChildrenActorsAndRenderables().keySet()) {
                // Apply effect to child
                applyActorVisibility(aCur, caToDraw.getChildrenActorsAndRenderables().get(aCur));
            }

        }

        // Update visibility
        aToDraw.setVisible(renderable.isVisible());

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
        Actor resource = null;
        try {
            Actor newActorForRenderable = actorFactory.createResourceForType(toDraw);
            if(newActorForRenderable != null) {
                resource = newActorForRenderable;
                addActor(toDraw, newActorForRenderable);
                printActors();
            }
        } catch (UnsupportedRenderableTypeException e) {
            e.printStackTrace();
        }
        return resource;
    }

    private Actor addActor(final Renderable toDraw, Actor newActorForRenderable) {
        addClickListenerIfButton(toDraw, newActorForRenderable);
        renderableActorMap.put(toDraw, newActorForRenderable);
        return  newActorForRenderable;
    }

    protected void addClickListenerIfButton(final Renderable toDraw, Actor newActorForRenderable) {
        if(toDraw.getType().equals("text_button") || toDraw.getType().equals("image_button")) {
            userInputHandler.addClickListenerForActor((ActionButton) toDraw, newActorForRenderable);
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
            for(Actor actor : stage.getActors()) {
                System.out.println("removing: " + actor.getName());
                actor.addAction(Actions.removeActor());
            }
        }
    }

    public boolean renderableExists(Renderable renderable) {
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
        return toReturn;
    }

    public void printActors() {
        System.out.println("printActors");
        for(Actor stageActor : stage.getActors()) {
            System.out.println("Actor " + stageActor.getClass() + " " + stageActor.getName() + " " + stageActor.getZIndex());
        }
    }

    public synchronized void removeEffectFromRenderable(Effect eToRemove, Renderable renderable) {
        if(renderableExistsAsSprite(renderable)) {
            Map<Effect, LGDXEffect> mMap = spriteEffects.get(renderableSpriteMap.get(renderable));
            if (mMap != null)
                mMap.remove(eToRemove);
        }
        else {
            Map<Effect, LGDXEffect> mMap = actorEffects.get(renderableActorMap.get(renderable));
            if (mMap != null)
                mMap.remove(eToRemove);
        }

    }

    public Sprite getSpriteForRenderable(Renderable renderable) {
        return renderableSpriteMap.get(renderable);
    }
}
