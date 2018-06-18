package org.scify.moonwalker.app;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.screens.GameLauncher;

import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;

/**
 * The MoonWalker class describes instances of the MoonWalker game.
 */
public class MoonWalker extends Game {

    GameLauncher gameLauncher;

    @Override
    public void create() {
        AppInfo appInfo = AppInfo.getInstance();
        appInfo.setScreenWidth(Gdx.graphics.getWidth());
        appInfo.setScreenHeight(Gdx.graphics.getHeight());
        appInfo.setScreenDensity(Gdx.graphics.getDensity());
        gameLauncher = new GameLauncher(this);
        setScreen(gameLauncher);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    /**
     * Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     *
     * @param screen may be {@code null}
     */
    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
