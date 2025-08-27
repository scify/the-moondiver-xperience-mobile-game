package org.scify.moonwalker.app;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;

import org.scify.moonwalker.app.helpers.AnalyticsLogger;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.screens.GameLauncher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

import io.sentry.Sentry;
import io.sentry.SentryOptions; // Added for Sentry.OptionsConfiguration

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
            final String sentryDSNValue = properties.getProperty("Sentry.DSN");
            final String releaseVersion = properties.getProperty("release");

            if (sentryDSNValue != null && sentryDSNValue.length() > 10) {
                Sentry.init(new Sentry.OptionsConfiguration<SentryOptions>() {
                    @Override
                    public void configure(@NonNull SentryOptions options) {
                        options.setDsn(sentryDSNValue);
                        options.setRelease(releaseVersion);
                        // Disable trace sampling
                        options.setTracesSampleRate(0.0);
                    }
                });
                // Set extras after init
                Sentry.setExtra("release", releaseVersion); // Still useful to set as extra for context
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
