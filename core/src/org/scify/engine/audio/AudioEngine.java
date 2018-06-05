package org.scify.engine.audio;

public interface AudioEngine {
    void pauseCurrentlyPlayingAudios();
    void playAudio(String filePath);
    void playMusicLoop(String filePath);
    void stopMusic(String filePath);
    void loadSound(String filePath);
    void stopAllMusic();
    void disposeResources();
}
