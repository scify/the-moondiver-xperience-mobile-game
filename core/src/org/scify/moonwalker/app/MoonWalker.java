package org.scify.moonwalker.app;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.screens.GameLauncher;

/**
 * The MoonWalker class describes instances of the MoonWalker game.
 */
public class MoonWalker extends Game {

    @Override
    public void create() {
        GameInfo gameInfo = GameInfo.getInstance();
        gameInfo.setScreenWidth(Gdx.graphics.getWidth());
        gameInfo.setScreenHeight(Gdx.graphics.getHeight());
        gameInfo.setScreenDensity(Gdx.graphics.getDensity());
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
