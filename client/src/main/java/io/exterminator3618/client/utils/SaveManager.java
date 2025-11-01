package io.exterminator3618.client.utils;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.screens.GameScreen;

public final class SaveManager {
    
    private static final String SAVE_KEY = "save_data";

    private static final JsonValue.PrettyPrintSettings printSettings = initPrintSettings();

    public static void saveGame(Exterminator3618 game, GameScreen gameScreen) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        GameSaveData.SaveData data = gameScreen.exportState();
        game.getPreferences().putString(SAVE_KEY, json.prettyPrint(data, printSettings));
    }
    
    public static boolean hasSave(Exterminator3618 game) {
        Preferences prefs = game.getPreferences();
        return prefs.contains(SAVE_KEY) && !prefs.getString(SAVE_KEY).isEmpty();
    }
    
    public static void clearSave(Exterminator3618 game) {
        Preferences prefs = game.getPreferences();
        if (prefs.contains(SAVE_KEY)) {
            prefs.remove(SAVE_KEY);
        }
    }
    
    public static void loadInto(Exterminator3618 game, GameScreen gameScreen) {
        Preferences prefs = game.getPreferences();
        if (!prefs.contains(SAVE_KEY)) return;
        Json json = new Json();
        GameSaveData.SaveData data = json.fromJson(GameSaveData.SaveData.class, prefs.getString(SAVE_KEY));
        gameScreen.importState(data);
    }

    private static JsonValue.PrettyPrintSettings initPrintSettings() {
        JsonValue.PrettyPrintSettings obj = new JsonValue.PrettyPrintSettings();
        obj.outputType = JsonWriter.OutputType.json;
        obj.wrapNumericArrays = false;
        obj.singleLineColumns = Integer.MAX_VALUE;
        return obj;
    };

}


