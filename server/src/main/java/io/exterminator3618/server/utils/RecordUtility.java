package io.exterminator3618.server.utils;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.data.Match;
import io.exterminator3618.server.data.Record;
import io.exterminator3618.server.models.FrameDumpEvent;
import io.exterminator3618.server.models.GenericEvent;
import io.exterminator3618.server.models.HpChangeEvent;
import io.exterminator3618.server.models.InGameEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class RecordUtility {

    private static final String DELIMITER = "\\";
    private static final String DATA_START_SEQUENCE = "!3636$START";
    private static final String DATA_END_SEQUENCE = "!3636$END";

    private final ArrayList<InGameEvent> events = new ArrayList<>();

    @Getter
    private int currentScore = 0;

    @Getter
    private int currentCombo = 0;

    @Getter
    private int maxCombo = 0;

    @Getter
    private int currentHp = 5;

    @Getter
    @Setter
    private boolean isFinished = false;

    public void logEvent(@NonNull InGameEvent event) {
        if (isFinished) {
            return;
        }
        if (event instanceof FrameDumpEvent) {
            currentScore = event.getCurrentScore();
            currentCombo = event.getCurrentCombo();
            maxCombo = Math.max(maxCombo, currentCombo);
            currentHp = ((FrameDumpEvent) event).getHp();
            isFinished = ((FrameDumpEvent) event).isFinished();
        }
        events.add(event);
    }

    public void logEvent(@NonNull String rawEventData) {
        var event = switch (rawEventData.split(":")[0]) {
            case "FRAME" -> new FrameDumpEvent().deserialize(rawEventData);
            case "HP" -> new HpChangeEvent().deserialize(rawEventData);
            // for now we map only to generic event type
            default -> new GenericEvent().deserialize(rawEventData);
        };
        logEvent(event);
    }

    public void logEvents(@NonNull List<String> rawEventDataList) {
        for (String rawEventData : rawEventDataList) {
            logEvent(rawEventData);
        }
    }

    public void logEvents(InGameEvent... newEvents) {
        for (InGameEvent event : newEvents) {
            logEvent(event);
        }
    }

    /**
     * Recalculate data and export to Record entity.
     * @param account account to link the record to
     * @param match match to link the record to
     * @return Record entity
     */
    public Record export(Account account, Match match) {
        StringBuilder eventData = new StringBuilder();
        eventData.append(DATA_START_SEQUENCE).append(DELIMITER);
        int highestScore = 0;
        int highestCombo = 0;
        for (InGameEvent event : events) {
            if (event.getCurrentCombo() > highestCombo) {
                eventData.append(event.serialize()).append(DELIMITER);
                highestScore = Math.max(highestScore, event.getCurrentScore());
                highestCombo = Math.max(highestCombo, event.getCurrentCombo());
            }
        }
        eventData.append(DATA_END_SEQUENCE);
        var record = new Record();
        record.setAccount(account);
        record.setMatch(match);
        record.setScore(highestScore);
        record.setHighestCombo(highestCombo);
        record.setData(eventData.toString());
        return record;
    }

}
