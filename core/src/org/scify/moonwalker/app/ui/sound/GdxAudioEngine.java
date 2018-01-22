package org.scify.moonwalker.app.ui.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.scify.engine.audio.AudioEngine;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.HashMap;
import java.util.Map;

public class GdxAudioEngine implements AudioEngine{

    protected Map<String, Sound> stringAudiosMap;
    protected ResourceLocator resourceLocator;

    public GdxAudioEngine() {
        stringAudiosMap = new HashMap<>();
        resourceLocator = new ResourceLocator();
    }

    @Override
    public void pauseCurrentlyPlayingAudios() {
        for(Map.Entry<String, Sound> stringAudio : stringAudiosMap.entrySet()) {
            Sound currSound = stringAudio.getValue();
            if(currSound != null)
                currSound.stop();
        }
    }

    @Override
    public void pauseSound(String filePath) {
        Sound sound = stringAudiosMap.get(filePath);
        if(sound != null)
            sound.pause();
    }

    @Override
    public void resumeSound(String filePath) {
        Sound sound = stringAudiosMap.get(filePath);
        if(sound != null)
            sound.resume();
    }

    @Override
    public void playSound(String filePath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
        if(sound != null) {
            if(stringAudiosMap.get(filePath) == null)
                stringAudiosMap.put(filePath, sound);
            sound.play();
        }
    }

    @Override
    public void playSoundLoop(String filePath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(resourceLocator.getFilePath(filePath)));
        if(sound != null) {
            if(stringAudiosMap.get(filePath) == null)
                stringAudiosMap.put(filePath, sound);
            long soundId = sound.play();
            sound.setLooping(soundId, true);
        }
    }

    @Override
    public void pauseAndPlaySound(String filePath) {
        pauseCurrentlyPlayingAudios();
        playSound(filePath);
    }

    @Override
    public void disposeResources() {
        for(Map.Entry<String, Sound> stringAudio : stringAudiosMap.entrySet()) {
            Sound currSound = stringAudio.getValue();
            if(currSound != null)
                currSound.dispose();
        }
    }
}
