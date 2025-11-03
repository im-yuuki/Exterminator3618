package io.exterminator3618.server.models;

import io.exterminator3618.server.utils.EventDeserializeError;
import lombok.Getter;

/**
 * Data schema is HP:<timestamp>:<+/->:<currentHP>
 */
public class HpChangeEvent implements InGameEvent {

    private int timestamp;
    @Getter
    private boolean isHeal;
    @Getter
    private int currentHp;

    @Override
    public InGameEvent deserialize(String data) throws EventDeserializeError {
        String[] parts = data.split(":");
        if (parts.length != 4 || !parts[0].equals("HP")) {
            throw new EventDeserializeError("Invalid HP change event format");
        }
        try {
            timestamp = Integer.parseInt(parts[1]);
            isHeal = parts[2].equals("+");
            currentHp = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            throw new EventDeserializeError("Invalid number format in HP change event", e);
        }
        return this;
    }

    @Override
    public String serialize() {
        return "HP:" + timestamp + ":" + (isHeal ? "+" : "-") + ":" + currentHp;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public int getCurrentScore() {
        return 0;
    }

    @Override
    public int getCurrentCombo() {
        return 0;
    }

}
