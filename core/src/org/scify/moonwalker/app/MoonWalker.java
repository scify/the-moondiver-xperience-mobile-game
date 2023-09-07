package org.scify.moonwalker.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import org.scify.moonwalker.app.helpers.AnalyticsLogger;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.screens.GameLauncher;

import io.sentry.Sentry;

import java.io.*;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * The MoonWalker class describes instances of the MoonWalker game.
 */
public class MoonWalker extends Game {

    GameLauncher gameLauncher;

    public MoonWalker(AnalyticsLogger analyticsLogger) {
        AppInfo appInfo = AppInfo.getInstance();
        appInfo.setAnalyticsLogger(analyticsLogger);
    }

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
            String sentryDSN = properties.getProperty("Sentry.DSN")+"?release="+ URLEncoder.encode(properties.getProperty("release"), "UTF-8");
            if (sentryDSN.length() > 10) {
                Sentry.init(sentryDSN);
                Sentry.setExtra("release", properties.getProperty("release"));
                Sentry.setExtra("platform", String.valueOf(Gdx.app.getType()));
                Sentry.setExtra("device_height", String.valueOf(Gdx.app.getGraphics().getHeight()));
                Sentry.setExtra("device_width", String.valueOf(Gdx.app.getGraphics().getWidth()));
                Sentry.setExtra("device_density", String.valueOf(Gdx.app.getGraphics().getDensity()));
                Sentry.setExtra("device_delta_time", String.valueOf(Gdx.app.getGraphics().getDeltaTime()));
                Sentry.setExtra("device_version", String.valueOf(Gdx.app.getVersion()));
                Sentry.setExtra("full_screen", String.valueOf(Gdx.app.getGraphics().isFullscreen()));
            }
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
