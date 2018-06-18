package org.scify.moonwalker.app.screens;

import com.badlogic.gdx.Screen;
import org.scify.engine.RenderingEngine;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.conversation.RandomResponse;
import org.scify.moonwalker.app.game.conversation.RandomResponseFactory;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.ActorFactory;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

public class GamePlayScreen implements Screen {

    private RenderingEngine renderingEngine;
    private boolean bDisposingInitiated;

    public GamePlayScreen(RenderingEngine renderingEngine) {
        this.renderingEngine = renderingEngine;
    }

    @Override
    public void show() {
        renderingEngine.initialize();
        System.err.println("Engine: " + renderingEngine.toString());
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
        // Dispose all singletons
        ThemeController.getInstance().dispose(); // Do NOT move elsewhere, otherwise the OpenGL context
            // required to free the fonts will not be present

        try {
            LGDXRenderableBookKeeper.getInstance().dispose();
        } catch (LGDXRenderableBookKeeper.UninitializedBookKeeperException e) {
            // Ignore
            System.err.println("INFO: Bookkeeper not initialized. Ignoring...");
        }

        ActorFactory.getInstance().dispose();

        // Update rendering engine that we have a hide event
        renderingEngine.sendHideEventToEpisode();

        ResourceLocator.getInstance().dispose();

        UserInputHandlerImpl.getInstance().dispose();

        GameInfo.getInstance().dispose();

        AppInfo.getInstance().dispose();

        RandomResponseFactory.getInstance().dispose();


        // Clean-up self
        dispose();
    }

    @Override
    public synchronized void dispose() {
        bDisposingInitiated = true;
        renderingEngine.disposeResources();
        bDisposingInitiated = false;
    }
}
