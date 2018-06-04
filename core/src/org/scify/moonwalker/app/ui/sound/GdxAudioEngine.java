package org.scify.moonwalker.app.ui.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.scify.engine.audio.AudioEngine;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GdxAudioEngine implements AudioEngine {

    protected Map<String, Sound> stringAudiosMap;
    protected Set<String> persistentAudios;
    protected ResourceLocator resourceLocator;

    public GdxAudioEngine() {
        stringAudiosMap = new HashMap<>();
        resourceLocator = new ResourceLocator();
        persistentAudios = new HashSet<>();
    }

    @Override
    public void loadSound(String filePath) {
        persistentAudios.add(filePath);
        if (!stringAudiosMap.containsKey(filePath)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            stringAudiosMap.put(filePath, sound);
        }
    }

    @Override
    public void stopAllSounds() {
        for (String entry: stringAudiosMap.keySet()) {
            stopSound(entry);
        }
    }

    @Override
    public void disposeSound(String filePath) {
        if (stringAudiosMap.containsKey(filePath)) {
            persistentAudios.remove(filePath);
            Sound sound = stringAudiosMap.get(filePath);
            stringAudiosMap.remove(filePath);
            sound.dispose();
        }
    }

    @Override
    public void pauseCurrentlyPlayingAudios() {
        for (Map.Entry<String, Sound> stringAudio : stringAudiosMap.entrySet()) {
            Sound currSound = stringAudio.getValue();
            if (currSound != null)
                currSound.stop();
        }
    }

    @Override
    public void pauseSound(String filePath) {
        Sound sound = stringAudiosMap.get(filePath);
        if (sound != null)
            sound.pause();
    }

    @Override
    public void stopSound(String filePath) {
        Sound sound = stringAudiosMap.get(filePath);
        if (sound != null)
            sound.stop();
    }

    @Override
    public void resumeSound(String filePath) {
        Sound sound = stringAudiosMap.get(filePath);
        if (sound != null)
            sound.resume();
    }

    @Override
    public void playSound(String filePath) {
        if (stringAudiosMap.containsKey(filePath)) {
            Sound sound = stringAudiosMap.get(filePath);
            sound.play();
        }else {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            if (sound != null) {
                if (stringAudiosMap.get(filePath) == null)
                    stringAudiosMap.put(filePath, sound);
                sound.play();
            }
        }
    }

    @Override
    public void playSoundLoop(String filePath) {
        if (stringAudiosMap.containsKey(filePath)) {
            Sound sound = stringAudiosMap.get(filePath);
            sound.loop();
        }else {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
            if (sound != null) {
                if (stringAudiosMap.get(filePath) == null)
                    stringAudiosMap.put(filePath, sound);
                sound.loop();
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
        for (Map.Entry<String, Sound> stringAudio : stringAudiosMap.entrySet()) {
            Sound currSound = stringAudio.getValue();
            if (currSound != null && persistentAudios.contains(stringAudio.getKey()) == false)
                currSound.dispose();
        }
    }
}
