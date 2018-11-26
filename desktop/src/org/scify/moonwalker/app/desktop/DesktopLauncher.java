package org.scify.moonwalker.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.scify.moonwalker.app.MoonWalker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Custom dimensions for desktop
		config.height = 411;
		config.width = 731;
		new LwjglApplication(new MoonWalker(new DesktopAnalyticsLogger()), config);
	}
}
