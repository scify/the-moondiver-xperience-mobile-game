package org.scify.moonwalker.app;

import com.badlogic.gdx.Game;

import org.scify.moonwalker.app.screens.GameLauncher;

public class MoonWalker extends Game {

    @Override
    public void create() {
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
