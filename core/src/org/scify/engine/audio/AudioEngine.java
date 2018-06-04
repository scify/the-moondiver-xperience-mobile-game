package org.scify.engine.audio;

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
    void disposeSound(String filePath);
}
