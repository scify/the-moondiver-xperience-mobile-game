package org.scify.moonwalker.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ZIndexedStage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.CameraController;
import org.scify.moonwalker.app.ui.actors.IContainerActor;

import java.util.Set;

/**
 * This class can paint all types of actors and sprites for a given stage.
 */
public class MoonwalkerUIPainter {
    protected ZIndexedStage stage;
    protected Label fpsLabel;
    protected Skin skin;
    protected BitmapFont font;
    protected AppInfo appInfo;
    protected SpriteBatch batch;
    protected Image overallBackground = null;
    protected CameraController cameraController;

    public Image getOverallBackground() {
        return overallBackground;
    }

    public void setOverallBackground(Image overallBackground) {
        this.overallBackground = overallBackground;
    }

    public MoonwalkerUIPainter(ZIndexedStage sStage, CameraController ccCameraController) {
        stage = sStage;
        batch = (SpriteBatch)stage.getBatch();
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

    public void drawUIRenderable(Object uiRenderable, Renderable renderable) {
        if (uiRenderable instanceof Sprite) {
            Sprite sToDraw = (Sprite) uiRenderable;
            drawSpriteFromRenderable(renderable, sToDraw);
        } else {
            Actor aToDraw = (Actor) uiRenderable;
            drawActorFromRenderable(renderable, aToDraw);
        }
    }

    public void drawSpriteFromRenderable(Renderable renderable, Sprite sToDraw) {
        sToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        applySpriteEffects(sToDraw, renderable); // Deal with effects

        // Apply visibility
        if (renderable.isVisible() && (renderable.getZIndex() >= 0)) {
            sToDraw.draw(batch); // Follow this approach, to use sprite all traits (including color, alpha, etc)
        }
    }


    public void drawActorFromRenderable(Renderable renderable, Actor aToDraw) {
        // Update position, when appropriate
        if (renderable.isPositionDrawable())
            aToDraw.setPosition(renderable.getxPos(), renderable.getyPos());
        // Apply effects
        applyActorEffects(aToDraw, renderable); // Deal with effects

        // Update visibility
        applyActorVisibility(aToDraw, renderable); // Apply visibility


        // if actor does not have a stage, it means that
        // it is the first time that it is added to the stage.
        if (aToDraw.getStage() == null) {
            //System.out.println("new actor with name: " + renderable.getId());
            aToDraw.setName(renderable.getId());

            // If actor does not have a parent
            if (aToDraw.getParent() == null) {
                // we should add it to the stage
                stage.addActorForRenderable(aToDraw, renderable);
            }
        }
    }

    public synchronized void applyActorEffects(Actor aToDraw, Renderable renderable) {

        // Effect application
        /////////////////////
        Set<Effect> actorEffects = renderable.getEffects();
        if (actorEffects == null)
            return;

        // For each effect
        for (Effect eCur : actorEffects) {
            if (eCur instanceof LGDXEffect) {
                // apply for LGDX effects
                ((LGDXEffect) eCur).applyToActor(aToDraw, renderable);
            } else {
                // apply for generic effects
                eCur.applyTo(renderable);
            }
        }
    }

    public synchronized void applySpriteEffects(Sprite sToDraw, Renderable renderable) {

        // Effect application
        /////////////////////
        Set<Effect> spriteEffects = renderable.getEffects();

        if (spriteEffects == null)
            return;

        // For each effect
        for (Effect eCur : spriteEffects) {
            if (eCur instanceof LGDXEffect) {
                // apply for LGDX effects
                ((LGDXEffect) eCur).applyToSprite(sToDraw, renderable);
            } else {
                // apply for generic effects
                eCur.applyTo(renderable);
            }
        }
    }

    public void updateStageBG(Float delta, long lNewTime, long lLastUpdate) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //stage.setDebugAll(true);
        if (fpsLabel != null) {
            fpsLabel.setText(String.valueOf(1000 / (lNewTime - lLastUpdate)));
            fpsLabel.draw(batch, 1);
        }

        if (overallBackground != null)
            overallBackground.draw(batch, 1);

    }

    public void applyActorVisibility(Actor aToDraw, Renderable renderable) {
        // If a container
        if (aToDraw instanceof IContainerActor) {
            IContainerActor<Renderable> caToDraw = (IContainerActor) aToDraw;
            // For every child
            for (Actor aCur : caToDraw.getChildrenActorsAndRenderables().keySet()) {
                // Apply effect to child
                applyActorVisibility(aCur, caToDraw.getChildrenActorsAndRenderables().get(aCur));
            }

        }

        // Update visibility
        aToDraw.setVisible(renderable.isVisible());

        // update the z index of the actor, according to the renderable's z index
        // Take into account also the z-index rule (<0 means invisible)
        if (renderable.getZIndex() < 0)
        {
            // TODO: Check in the future if it needs to be removed
            aToDraw.setVisible(false);
        }
    }
}
