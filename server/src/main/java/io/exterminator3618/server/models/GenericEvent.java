package io.exterminator3618.server.models;

import io.exterminator3618.server.utils.EventDeserializeError;

/**
 * Data schema is <type>:<timestamp>:<spectificData:...?>:<currentScore>:<currentCombo>
 */
public class GenericEvent implements InGameEvent {

    String type;
    int timestamp;
    String specificData;
    int currentScore;
    int currentCombo;

    @Override
    public GenericEvent deserialize(String data) throws EventDeserializeError {
        String[] parts = data.split(":");
        if (parts.length < 5) {
            throw new EventDeserializeError("Invalid data schema");
        }
        try {
            this.type = parts[0];
            this.timestamp = Integer.parseInt(parts[1]);
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < parts.length - 2; i++) {
                sb.append(parts[i]).append(":");
            }
            this.specificData = sb.deleteCharAt(sb.length() - 1).toString();
            this.currentScore = Integer.parseInt(parts[parts.length - 2]);
            this.currentCombo = Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            throw new EventDeserializeError("Invalid number format", e);
        }
        return this;
    }

    @Override
    public String serialize() {
        return type + ":" + timestamp + ":" + specificData + ":" + currentScore + ":" + currentCombo;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public int getCurrentScore() {
        return currentScore;
    }

    @Override
    public int getCurrentCombo() {
        return currentCombo;
    }

}
