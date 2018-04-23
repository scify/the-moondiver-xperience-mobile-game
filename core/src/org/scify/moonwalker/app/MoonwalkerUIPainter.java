package org.scify.moonwalker.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.CameraController;
import org.scify.moonwalker.app.ui.actors.IContainerActor;

import java.awt.*;
import java.util.Map;

/**
 * This class can paint all types of actors and sprites for a given stage.
 */
public class MoonwalkerUIPainter {
    protected Stage stage;
    protected Label fpsLabel;
    protected Skin skin;
    protected BitmapFont font;
    protected AppInfo appInfo;
    protected Batch batch;
    protected Image overallBackground = null;
    protected CameraController cameraController;

    public Image getOverallBackground() {
        return overallBackground;
    }

    public void setOverallBackground(Image overallBackground) {
        this.overallBackground = overallBackground;
    }

    public MoonwalkerUIPainter(Stage sStage, CameraController ccCameraController) {
        stage= sStage;
        batch = stage.getBatch();
        cameraController = ccCameraController;
        appInfo = AppInfo.getInstance();
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void addFPSLabel() {
        fpsLabel = new Label("", skin);
        fpsLabel.setStyle(new Label.LabelStyle(font, Color.RED));
        fpsLabel.setPosition(20, appInfo.getScreenHeight() - 20);
        // fps label has twice the normal font size
        fpsLabel.setFontScale(2);
    }

    public void drawUIRenderable(Object uiRenderable, Renderable renderable, Map<Effect, LGDXEffect> uiRenderableEffects) {

        if(uiRenderable instanceof Sprite) {
            Sprite sToDraw = (Sprite)uiRenderable;
            drawSpriteFromRenderable(renderable, sToDraw, uiRenderableEffects);
        } else {
            Actor aToDraw = (Actor)uiRenderable;
            drawActorFromRenderable(renderable, aToDraw, uiRenderableEffects);
        }
    }

    public void drawSpriteFromRenderable(Renderable renderable, Sprite sToDraw, Map<Effect, LGDXEffect> uiRenderableEffects) {
        sToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        applySpriteEffects(sToDraw, renderable, uiRenderableEffects); // Deal with effects

        if (renderable.isVisible()) {
            sToDraw.draw(batch); // Follow this approach, to use sprite all traits (including color, alpha, etc)
        }
    }



    public void drawActorFromRenderable(Renderable renderable, Actor aToDraw, Map<Effect, LGDXEffect> uiRenderableEffects) {
        aToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        applyActorEffects(aToDraw, renderable, uiRenderableEffects); // Deal with effects
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

    public synchronized void applyActorEffects(Actor aToDraw, Renderable renderable, Map<Effect, LGDXEffect> actorEffectsMap) {
        // Effect application
        /////////////////////
        if (actorEffectsMap == null)
            return;

        // For each effect
        for (Map.Entry<Effect, LGDXEffect> eCur: actorEffectsMap.entrySet()) {
            // apply
            eCur.getValue().applyToActor(aToDraw, renderable);
        }
    }

    public synchronized void applySpriteEffects(Sprite sToDraw, Renderable renderable, Map<Effect, LGDXEffect> spriteEffectsMap) {
        // Effect application
        /////////////////////
        if (spriteEffectsMap == null)
            return;

        // For each effect
        for (Map.Entry<Effect, LGDXEffect> eCur: spriteEffectsMap.entrySet()) {
            // apply
            eCur.getValue().applyToSprite(sToDraw, renderable);
        }
    }

    public void updateStage(Float delta, long lNewTime, long lLastUpdate) {
        Batch batch = stage.getBatch();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //stage.setDebugAll(true);
        synchronized (batch) {
            batch.begin();
            if (fpsLabel != null) {
                fpsLabel.setText(String.valueOf(1000 / (lNewTime - lLastUpdate)));
                fpsLabel.draw(batch, 1);
            }

            if (overallBackground != null)
                overallBackground.draw(batch, 1);

            batch.end();
            cameraController.update();
            stage.act(delta);
            stage.draw();
        }
    }

    public void applyActorVisibility(Actor aToDraw, Renderable renderable) {
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

    }}
