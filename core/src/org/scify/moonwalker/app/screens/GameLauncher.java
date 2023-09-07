package org.scify.moonwalker.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ZIndexedStage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.sentry.Sentry;
import org.scify.engine.RenderingEngine;
import org.scify.engine.Scenario;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.MoonWalker;
import org.scify.moonwalker.app.game.scenarios.MoonWalkerScenario;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.MoonWalkerRenderingEngine;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;

/**
 * This class implements a screen (a.k.a. activity) which launches the MoonWalker game.
 */
public class GameLauncher implements Screen {

    private final MoonWalker app;
    private ZIndexedStage stage;
    private SpriteBatch batch;
    private Viewport gameViewport;
    private OrthographicCamera mainCamera;
    private AppInfo appInfo;
    Thread thread;
    RenderingEngine renderingEngine;


    /**
     * We initialize the game, taking into account the underlying MoonWalker (LibGDX) application.
     * @param app The application that hosts the activity.
     */
    public GameLauncher(MoonWalker app) {
        this.app = app;
        batch = new SpriteBatch();
        appInfo = AppInfo.getInstance();
        int width = appInfo.getScreenWidth();
        int height = appInfo.getScreenHeight();
        mainCamera = new OrthographicCamera(width, height);
        mainCamera.position.set(width / 2f, height / 2, 0);

        gameViewport = new StretchViewport(width, height,
                mainCamera);
        stage = new ZIndexedStage(gameViewport, batch);
    }



    @Override
    public void show() {
        startNewGame();
    }

    /**
     * Starts the game.
     */
    private void startNewGame() {
        final UserInputHandler userInputHandler = UserInputHandlerImpl.getInstance();
        renderingEngine = new MoonWalkerRenderingEngine(userInputHandler, batch, stage);
        final Scenario mainGameScenario = new MoonWalkerScenario();
        Sentry.captureMessage("testing SDK setup");
        app.setScreen(new GamePlayScreen(renderingEngine));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mainGameScenario.start(renderingEngine, userInputHandler);
            }
        });

        thread.start();
    }

    @Override
    public void render(float delta) {
        System.out.println("rendering: " + this.getClass().getCanonicalName() + " delta: " + delta);
    }

    @Override
    public void resize(int width, int height) {

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
    public void dispose() {
        stage.dispose();
    }
}
