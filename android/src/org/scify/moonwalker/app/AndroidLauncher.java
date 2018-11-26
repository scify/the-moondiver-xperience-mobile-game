package org.scify.moonwalker.app;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// this sets the app to full screen
		config.useImmersiveMode = true;
		initialize(new MoonWalker(new AndroidAnalyticsLogger(getContext())), config);
	}
}
