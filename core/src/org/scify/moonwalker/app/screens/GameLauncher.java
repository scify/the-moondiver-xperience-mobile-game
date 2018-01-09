package org.scify.moonwalker.app.screens;

import com.badlogic.gdx.Screen;

import org.scify.moonwalker.app.MoonWalker;
import org.scify.moonwalker.app.game.GameEndState;
import org.scify.engine.GameEngine;
import org.scify.moonwalker.app.game.GameLevel;
import org.scify.moonwalker.app.game.GameProps;
import org.scify.moonwalker.app.game.GameType;
import org.scify.moonwalker.app.rules.MoonWalkerRules;
import org.scify.moonwalker.app.ui.MoonWalkerRenderingEngine;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameLauncher implements Screen {

    private final MoonWalker app;
    protected GameEngine gameEngine = null;

    public GameLauncher(MoonWalker app) {
        this.app = app;
    }

    private void initializeGameEngine() {
        GameProps props = new GameProps(new MoonWalkerRenderingEngine(), new MoonWalkerRules(), new UserInputHandlerImpl(), GameType.SINGLE_PLAYER, new GameLevel());
        this.gameEngine = new GameEngine(app);
        this.gameEngine.initialize(props);
    }

    @Override
    public void show() {
        startNewGame();
    }

    private void startNewGame() {
        // initialize should happen in the Main Thread to avoid Exception when initializing
        // new SpriteBatch in the Rendering Engine
        initializeGameEngine();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startGameThread();
            }
        });
        thread.start();
    }

    private void startGameThread() {
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<GameEndState> future = es.submit(gameEngine);
        es.shutdown();
        //this code will execute once the user exits the app
        // (either to go to next level or to exit)
        try {
            GameEndState result = future.get();
            System.out.println("Game result: " + result);
        } catch (InterruptedException | ExecutionException e) {

        }
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
