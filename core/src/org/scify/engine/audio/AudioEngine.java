package org.scify.engine.audio;

public interface AudioEngine {
    void pauseCurrentlyPlayingAudios();
    void pauseSound(String filePath);
    void playSound(String filePath);
    void pauseAndPlaySound(String filePath);
}
