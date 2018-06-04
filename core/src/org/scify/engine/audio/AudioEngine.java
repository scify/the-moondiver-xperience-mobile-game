package org.scify.engine.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public interface AudioEngine {
    void pauseCurrentlyPlayingAudios();
    void pauseSound(String filePath);
    void resumeSound(String filePath);
    void playSound(String filePath);
    void playSoundLoop(String filePath);
    void pauseAndPlaySound(String filePath);
    void stopSound(String filePath);
    void stopAllSounds();
    void disposeResources();
    void loadSound(String filePath);
    void disposeSound(String filePath);
}
