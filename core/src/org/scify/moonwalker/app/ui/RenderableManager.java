package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.scify.engine.Renderable;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.actors.AvatarSelectionActor;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;
import org.scify.moonwalker.app.ui.renderables.AvatarSelectionRenderable;

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

    public void drawRenderable(Renderable renderable) {
        Sprite sToDraw = getSpriteResourceFor(renderable);
        if (sToDraw != null) {
            drawSpriteFromRenderable(renderable, sToDraw);
        } else {
            Actor aToDraw = getActorResourceFor(renderable);
            if (aToDraw != null) {
                drawActorFromRenderable(renderable, aToDraw);
                update(renderable, aToDraw);
            }
        }
    }

    protected void update(Renderable renderable, Actor actor) {
        switch (renderable.getType()) {
            case "avatar_selection":
                AvatarSelectionRenderable avatarSelectionRenderable = (AvatarSelectionRenderable) renderable;
                AvatarSelectionActor avatarSelectionActor = (AvatarSelectionActor) actor;
                avatarSelectionActor.setRenderable(avatarSelectionRenderable);
        }
    }

    protected void drawSpriteFromRenderable(Renderable renderable, Sprite sToDraw) {
        sToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        batch.draw(sToDraw, sToDraw.getX() - (sToDraw.getWidth() / 2f), sToDraw.getY() - (sToDraw.getHeight() / 2f), sToDraw.getWidth(), sToDraw.getHeight());
    }

    protected void drawActorFromRenderable(Renderable renderable, Actor aToDraw) {
        aToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        // update the z index of the actor, according to the renderable's z index
        if(renderable.getZIndex() > 0)
            aToDraw.setZIndex(renderable.getZIndex());
        // if actor does not have a stage, it means that
        // it is the first time that is added to the stage.
        if(aToDraw.getStage() == null) {
            aToDraw.setName(renderable.getId());
            stage.addActor(aToDraw);
        }
    }

    protected Sprite getSpriteResourceFor(Renderable toDraw) {
        Sprite resource = null;
        // If I have an existing sprite
        if (renderableSpriteMap.containsKey(toDraw)) {
            // reuse it
            resource = renderableSpriteMap.get(toDraw);
        } else {
            // else
            try {
                // createSpriteResourceForType
                Sprite newResourceForRenderable = spriteFactory.createResourceForType(toDraw);
                if(newResourceForRenderable != null) {
                    // and map it to the object
                    renderableSpriteMap.put(toDraw, newResourceForRenderable);
                    resource = newResourceForRenderable;
                }
            } catch (UnsupportedRenderableTypeException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }

    protected Actor getActorResourceFor(final Renderable toDraw) {
        Actor resource = null;
        if (renderableActorMap.containsKey(toDraw))
            resource = renderableActorMap.get(toDraw);
        else {
            try {
                Actor newActorForRenderable = actorFactory.createResourceForType(toDraw);
                if(newActorForRenderable != null) {
                    resource = addActor(toDraw, newActorForRenderable);
                }
            } catch (UnsupportedRenderableTypeException e) {
                e.printStackTrace();
            }
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
        for(Iterator<Map.Entry<Renderable, Sprite>> it = renderableSpriteMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Renderable, Sprite> entry = it.next();
            entry.getValue().getTexture().dispose();
            it.remove();

        }
        for(Iterator<Map.Entry<Renderable, Actor>> it = renderableActorMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Renderable, Actor> entry = it.next();
            entry.getValue().remove();
            it.remove();
        }
    }
}
