package io.exterminator3618.server.models;

import io.exterminator3618.server.utils.EventDeserializeError;
import lombok.Getter;

/**
 * Data schema is FRAME:<timestamp>:<isFinished>:<hp>:<currentScore>:<currentCombo>
 */
public class FrameDumpEvent implements InGameEvent {

    private int timestamp;
    @Getter
    private boolean isFinished;
    @Getter
    private int hp;
    private int currentScore;
    private int currentCombo;

    @Override
    public InGameEvent deserialize(String data) throws EventDeserializeError {
        String[] parts = data.split(":");
        if (parts.length != 6) {
            throw new EventDeserializeError("Invalid data schema for FrameDumpEvent");
        }
        try {
            this.timestamp = Integer.parseInt(parts[1]);
            this.isFinished = Boolean.parseBoolean(parts[2]);
            this.hp = Integer.parseInt(parts[3]);
            this.currentScore = Integer.parseInt(parts[4]);
            this.currentCombo = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
            throw new EventDeserializeError("Invalid number format in FrameDumpEvent", e);
        }
        return this;
    }

    @Override
    public String serialize() {
        return "FRAME:" + timestamp + ":" + isFinished + ":" + hp + ":" + currentScore + ":" + currentCombo;
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
        return currentScore;
    }

}
