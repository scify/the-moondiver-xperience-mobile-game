package org.scify.moonwalker.app.screens;

import com.badlogic.gdx.Screen;
import org.scify.engine.RenderingEngine;

public class GamePlayScreen implements Screen {

    private RenderingEngine renderingEngine;
    private boolean bDisposingInitiated;

    public GamePlayScreen(RenderingEngine renderingEngine) {
        this.renderingEngine = renderingEngine;
    }

    @Override
    public void show() {
        renderingEngine.initialize();
    }

    @Override
    public void render(float delta) {
        if (!bDisposingInitiated) {
            renderingEngine.render(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        renderingEngine.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public synchronized void dispose() {
        bDisposingInitiated = true;
        renderingEngine.disposeResources();
        bDisposingInitiated = false;
    }
}
