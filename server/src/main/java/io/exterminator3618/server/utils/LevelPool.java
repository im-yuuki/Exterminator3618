package io.exterminator3618.server.utils;

import java.util.List;
import java.util.Random;

public class LevelPool {

    private static final List<String> availableMaps = List.of(
            "level1",
            "level2",
            "level3"
    );

    private static final Random random = new Random();

    public static String getRandomMapCode() {
        int index = random.nextInt(availableMaps.size());
        return availableMaps.get(index);
    }

}
