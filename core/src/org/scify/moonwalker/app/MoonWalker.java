package org.scify.moonwalker.app;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.screens.GameLauncher;

/**
 * The MoonWalker class describes instances of the MoonWalker game.
 */
public class MoonWalker extends Game {

    @Override
    public void create() {
        AppInfo appInfo = AppInfo.getInstance();
        appInfo.setScreenWidth(Gdx.graphics.getWidth());
        appInfo.setScreenHeight(Gdx.graphics.getHeight());
        appInfo.setScreenDensity(Gdx.graphics.getDensity());
        this.setScreen(new GameLauncher(this));
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
