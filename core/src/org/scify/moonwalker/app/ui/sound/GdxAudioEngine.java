package org.scify.moonwalker.app.ui.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.scify.engine.audio.AudioEngine;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import java.util.HashMap;
import java.util.Map;

public class GdxAudioEngine implements AudioEngine {

    protected Map<String, Music> stringAudioMap;
    protected ResourceLocator resourceLocator;

    public GdxAudioEngine() {
        stringAudioMap = new HashMap<>();
        resourceLocator = new ResourceLocator();
    }

    @Override
    public void stopAllSounds() {
        for (String entry: stringAudioMap.keySet()) {
            stopSound(entry);
        }
    }

    @Override
    public void disposeSound(String filePath) {
        if (stringAudioMap.containsKey(filePath)) {
            Music music = stringAudioMap.get(filePath);
            stringAudioMap.remove(filePath);
            music.dispose();
        }
    }

    @Override
    public void pauseCurrentlyPlayingAudios() {
        for (Map.Entry<String, Music> stringMusic : stringAudioMap.entrySet()) {
            Music currSound = stringMusic.getValue();
            if (currSound != null)
                currSound.stop();
        }
    }

    @Override
    public void pauseSound(String filePath) {
        Music music = stringAudioMap.get(filePath);
        if (music != null)
            music.pause();
    }

    @Override
    public void stopSound(String filePath) {
        Music music = stringAudioMap.get(filePath);
        if (music != null)
            music.stop();
    }

    @Override
    public void resumeSound(String filePath) {
        Music music = stringAudioMap.get(filePath);
        if (music != null)
            music.play();
    }

    @Override
    public void playSound(String filePath) {
        if (stringAudioMap.containsKey(filePath)) {
            Music music = stringAudioMap.get(filePath);
            music.setLooping(false);
            music.play();
        }else {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            if (music != null) {
                if (stringAudioMap.get(filePath) == null)
                    stringAudioMap.put(filePath, music);
                music.setLooping(false);
                music.play();
            }
        }
    }

    @Override
    public void playSoundLoop(String filePath) {
        if (stringAudioMap.containsKey(filePath)) {
            Music music = stringAudioMap.get(filePath);
            music.setLooping(true);
            music.play();
        }else {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            if (music != null) {
                if (stringAudioMap.get(filePath) == null)
                    stringAudioMap.put(filePath, music);
                music.setLooping(true);
                music.play();
            }
        }
    }

    @Override
    public void pauseAndPlaySound(String filePath) {
        pauseCurrentlyPlayingAudios();
        playSound(filePath);
    }

    @Override
    public void disposeResources() {
        /*for (Map.Entry<String, Music> stringAudio : stringAudioMap.entrySet()) {
            Music currMusic = stringAudio.getValue();
            if (currMusic != null)
                currMusic.dispose();
        }*/
    }
}
