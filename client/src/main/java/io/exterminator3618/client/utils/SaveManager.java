package io.exterminator3618.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import static io.exterminator3618.client.Constants.SAVE_FILE;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.screens.GameScreen;

public final class SaveManager {

    private SaveManager() { }

    public static void saveGame(Exterminator3618 game, GameScreen gameScreen) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        GameSaveData.SaveData data = gameScreen.exportState();
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        handle.writeString(json.prettyPrint(data), false);
    }

    public static boolean hasSave(Exterminator3618 game) {
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        return handle.exists() && handle.length() > 0;
    }

    public static void clearSave(Exterminator3618 game) {
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        if (handle.exists()) handle.delete();
    }

    public static void loadInto(Exterminator3618 game, GameScreen gameScreen) {
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        if (!handle.exists()) return;
        Json json = new Json();
        GameSaveData.SaveData data = json.fromJson(GameSaveData.SaveData.class, handle);
        gameScreen.importState(data);
    }
}


