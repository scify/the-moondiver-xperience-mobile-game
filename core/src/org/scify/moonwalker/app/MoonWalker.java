package org.scify.moonwalker.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.screens.GameLauncher;

import io.sentry.Sentry;

import java.io.*;
import java.util.Properties;

/**
 * The MoonWalker class describes instances of the MoonWalker game.
 */
public class MoonWalker extends Game {

    GameLauncher gameLauncher;

    @Override
    public void create() {
        // loading Sentry in desktop produces a
        // "java.lang.NoClassDefFoundError: org/slf4j/LoggerFactory" exception to be thrown
        if(!Gdx.app.getType().equals(Application.ApplicationType.Desktop))
            initErrorLogger();
        AppInfo appInfo = AppInfo.getInstance();
        appInfo.setScreenWidth(Gdx.graphics.getWidth());
        appInfo.setScreenHeight(Gdx.graphics.getHeight());
        appInfo.setScreenDensity(Gdx.graphics.getDensity());
        gameLauncher = new GameLauncher(this);
        setScreen(gameLauncher);
    }

    private void initErrorLogger() {
        try {
            FileHandle propertiesFileHandle = Gdx.files
                    .internal("config.properties");
            Properties properties = new Properties();
            properties.load(new BufferedInputStream(propertiesFileHandle.read()));
            Sentry.init(properties.getProperty("Sentry.DSN"));
            Sentry.getContext().addExtra("release", properties.getProperty("version"));
            Sentry.getContext().addExtra("platform", Gdx.app.getType());
            Sentry.getContext().addExtra("device_height", Gdx.app.getGraphics().getHeight());
            Sentry.getContext().addExtra("device_width", Gdx.app.getGraphics().getWidth());
            Sentry.getContext().addExtra("device_density", Gdx.app.getGraphics().getDensity());
            Sentry.getContext().addExtra("device_delta_time", Gdx.app.getGraphics().getDeltaTime());
            Sentry.getContext().addExtra("device_version", Gdx.app.getVersion());
            Sentry.getContext().addExtra("full_screen", Gdx.app.getGraphics().isFullscreen());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
