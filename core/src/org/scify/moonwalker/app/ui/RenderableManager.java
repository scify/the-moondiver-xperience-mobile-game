package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.scify.engine.Renderable;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.actors.Updateable;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenderableManager {

    private Map<Renderable, Sprite> renderableSpriteMap;
    private Map<Renderable, Actor> renderableActorMap;
    private ComponentFactory<Actor> actorFactory;
    private ComponentFactory<Sprite> spriteFactory;
    protected UserInputHandlerImpl userInputHandler;
    protected Batch batch;
    protected Stage stage;

    public RenderableManager(ThemeController themeController, UserInputHandler userInputHandler) {
        this.userInputHandler = (UserInputHandlerImpl) userInputHandler;
        this.actorFactory = new ActorFactory(themeController.getSkin());

        actorFactory.setUserInputHandler(userInputHandler);
        this.spriteFactory = new SpriteFactory(themeController.getSkin());
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
            update(renderable, aToDraw);
        }
    }

    protected void update(Renderable renderable, Actor actor) {
        // if the Actor implements the Updatable interface, pass the renderable as argument
        // for it to be updated
        if(actor instanceof Updateable) {
            ((Updateable) actor).update(renderable);
        }
    }

    protected void drawSpriteFromRenderable(Renderable renderable, Sprite sToDraw) {
        sToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        batch.draw(sToDraw, sToDraw.getX(), sToDraw.getY(), sToDraw.getWidth(), sToDraw.getHeight());
    }

    protected void drawActorFromRenderable(Renderable renderable, Actor aToDraw) {
        aToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        // update the z index of the actor, according to the renderable's z index
        if(renderable.getZIndex() >= 0)
            aToDraw.setZIndex(renderable.getZIndex());
        else
            aToDraw.setVisible(false);
        // if actor does not have a stage, it means that
        // it is the first time that is added to the stage.
        if(aToDraw.getStage() == null) {
            //System.out.println("new actor with name: " + renderable.getId());
            aToDraw.setName(renderable.getId());
            stage.addActor(aToDraw);
        }
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

    protected boolean renderableExistsAsSprite(Renderable renderable) {
        return renderableSpriteMap.containsKey(renderable);
    }

    protected boolean renderableExistsAsActor(Renderable renderable) {
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

    private void printActors() {
        //System.out.println("printActors");
        for(Actor stageActor : stage.getActors()) {
            //System.out.println("Actor " + stageActor.getClass() + " " + stageActor.getName() + " " + stageActor.getZIndex());
        }
    }
}
