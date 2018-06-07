package org.scify.moonwalker.app.ui.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.scify.engine.audio.AudioEngine;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import java.util.HashMap;
import java.util.Map;

public class GdxAudioEngine implements AudioEngine {

    protected Map<String, Music> stringMusicMap;
    protected Map<String, Sound> stringSoundMap;
    protected ResourceLocator resourceLocator;

    public GdxAudioEngine() {
        stringMusicMap = new HashMap<>();
        stringSoundMap = new HashMap<>();
        resourceLocator = ResourceLocator.getInstance();
    }

    @Override
    public void stopAllMusic() {
        for (String entry: stringMusicMap.keySet()) {
            stopMusic(entry);
        }
    }

    @Override
    public void pauseCurrentlyPlayingAudios() {
        for (Map.Entry<String, Music> stringMusic : stringMusicMap.entrySet()) {
            Music currSound = stringMusic.getValue();
            if (currSound != null)
                currSound.stop();
        }
    }

    @Override
    public void stopMusic(String filePath) {
        Music music = stringMusicMap.get(filePath);
        if (music != null)
            music.stop();
    }

    @Override
    public void playAudio(String filePath) {
        if (stringSoundMap.containsKey(filePath)) {
            Sound sound = stringSoundMap.get(filePath);
            sound.play();
        }
        else if (stringMusicMap.containsKey(filePath)) {
            Music music = stringMusicMap.get(filePath);
            music.setLooping(false);
            music.play();
        }else {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            if (music != null) {
                if (stringMusicMap.get(filePath) == null)
                    stringMusicMap.put(filePath, music);
                music.setLooping(false);
                music.play();
            }
        }
    }

    @Override
    public void playMusicLoop(String filePath) {
        if (stringMusicMap.containsKey(filePath)) {
            Music music = stringMusicMap.get(filePath);
            music.setLooping(true);
            music.play();
        }else {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            if (music != null) {
                if (stringMusicMap.get(filePath) == null)
                    stringMusicMap.put(filePath, music);
                music.setLooping(true);
                music.play();
            }
        }
    }

    @Override
    public void loadSound(String filePath) {
        Sound sound = stringSoundMap.get(filePath);
        if (sound == null) {
            sound = Gdx.audio.newSound(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            stringSoundMap.put(filePath, sound);
        }
    }

    @Override
    public void disposeResources() {
        for (Map.Entry<String, Music> stringAudio : stringMusicMap.entrySet()) {
            Music currMusic = stringAudio.getValue();
            if (currMusic != null)
                currMusic.dispose();
        }
        stringMusicMap.clear();
    }
}
