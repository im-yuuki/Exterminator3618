package io.exterminator3618.client.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Quản lý âm thanh đơn giản cho game.
 * Hỗ trợ phát hiệu ứng, nhạc nền và điều chỉnh âm lượng.
 */
public class SoundManager {

    private static final Logger log = LoggerFactory.getLogger(SoundManager.class);

    private static SoundManager instance;

    // Cache âm thanh
    private final Map<String, Sound> sounds = new HashMap<>();
    private final Map<String, Music> music = new HashMap<>();

    // Âm lượng
    private float volume = 1.0f;
    private Music currentMusic;
    
    private SoundManager() {
        log.info("SoundManager singleton instance created");
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * play sound.
     *
     * @param path đường dẫn file âm thanh
     * @param loop true = nhạc nền, false = hiệu ứng
     */
    public void play(String path, boolean loop) {
        try {
            if (loop) {
                // Nhạc nền
                if (currentMusic != null) {
                    currentMusic.stop();
                }

                Music music = this.music.get(path);
                if (music == null) {
                    music = Gdx.audio.newMusic(Gdx.files.internal(path));
                    this.music.put(path, music);
                }

                currentMusic = music;
                music.setLooping(true);
                music.setVolume(volume);
                music.play();
            } else {
                // Hiệu ứng
                Sound sound = sounds.get(path);
                if (sound == null) {
                    sound = Gdx.audio.newSound(Gdx.files.internal(path));
                    sounds.put(path, sound);
                }
                sound.play(volume);
            }
        } catch (Exception e) {
            log.error("cant play: {}", path);
        }
    }

    /**
     * play
     */
    public void play(String path) {
        play(path, false);
    }

    /**
     * stop music.
     */
    public void stop() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    // dừng nhạc
    public void stopSound(String path) {
        Sound sound = sounds.get(path);
        if (sound != null) {
            sound.stop();
        }
    }

    // set volume
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    // getter
    public float getVolume() {
        return volume;
    }

    public static void resetInstance() {
        if (instance != null) {
            instance.dispose();
            instance = null;
            log.info("SoundManager singleton instance reset");
        }
    }

    /**
     * Dispose of all cached sounds and music.
     */
    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        for (Music music : music.values()) {
            music.dispose();
        }
        sounds.clear();
        music.clear();
        currentMusic = null;
        log.info("SoundManager resources disposed");
    }

}

