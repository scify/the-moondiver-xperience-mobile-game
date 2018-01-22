package org.scify.moonwalker.app.screens;

import com.badlogic.gdx.Screen;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalker;
import org.scify.moonwalker.app.game.scenarios.KnightAdventuresScenario;
import org.scify.moonwalker.app.ui.MoonWalkerRenderingEngine;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

public class GameLauncher implements Screen {

    private final MoonWalker app;

    public GameLauncher(MoonWalker app) {
        this.app = app;
    }

    @Override
    public void show() {
        startNewGame();
    }

    private void startNewGame() {
        UserInputHandler userInputHandler = new UserInputHandlerImpl();
        RenderingEngine renderingEngine = new MoonWalkerRenderingEngine(userInputHandler);

        final Scenario mainGameScenario = new KnightAdventuresScenario(renderingEngine, userInputHandler);

        app.setScreen(new GamePlayScreen(renderingEngine));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mainGameScenario.start();
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

    }
}
