package io.exterminator3618.server.models;

import io.exterminator3618.server.utils.EventDeserializeError;

public interface InGameEvent {

    InGameEvent deserialize(String data) throws EventDeserializeError;

    String serialize();

    int getTimestamp();

    /**
     * Return 0 is acceptable if the event does not affect score/combo.
     *
     * @return current score/combo at the time of the event
     */
    int getCurrentScore();

    /**
     * Return 0 is acceptable if the event does not affect combo.
     *
     * @return current combo at the time of the event
     */
    int getCurrentCombo();

}
