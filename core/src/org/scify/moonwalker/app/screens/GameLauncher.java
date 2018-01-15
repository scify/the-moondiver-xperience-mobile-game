package org.scify.moonwalker.app.screens;

import com.badlogic.gdx.Screen;

import org.scify.engine.*;
import org.scify.moonwalker.app.KnightRaceEpisode;
import org.scify.moonwalker.app.MoonWalker;
import org.scify.moonwalker.app.game.GameEndState;
import org.scify.moonwalker.app.ui.MoonWalkerRenderingEngine;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        // TODO add GameScreen class that will take the rendering engine and the userInputHandler as parameter
        UserInputHandler userInputHandler = new UserInputHandlerImpl();
        MoonWalkerRenderingEngine renderingEngine = new MoonWalkerRenderingEngine(userInputHandler);

        Episode firstEpisode = new KnightRaceEpisode(renderingEngine, userInputHandler);
        final Scenario mainGameScenario = new Scenario(firstEpisode);
        app.setScreen(new GamePlayScreen(renderingEngine));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startScenario(mainGameScenario);
            }
        });
        thread.start();
    }

    private void startScenario(Scenario scenario) {
        Episode currentEpisode = scenario.getCurrentEpisode();
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<GameEndState> future = es.submit(currentEpisode);
        es.shutdown();
        //this code will execute once the user exits the app
        // (either to go to next level or to exit)
        try {
            GameEndState result = future.get();
            System.out.println("Game result: " + result);
        } catch (InterruptedException | ExecutionException e) {

        }
        currentEpisode.disposeResources();
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
