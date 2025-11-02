package io.exterminator3618.client.utils;

import io.exterminator3618.client.*;
import io.exterminator3618.client.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to read the level design file and generate a list of bricks.
 *
 */
public class LevelLoader {
    private static final Logger log = LoggerFactory.getLogger(LevelLoader.class);

    public static List<Brick> load(InputStream fileStream) {
        if (fileStream == null) {
            log.error("Level file stream is null");
            return new ArrayList<>();
        }

        List<Brick> bricks = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.warn("Load level failed", e);
        }

        int startY = Constants.WINDOW_HEIGHT - 120;

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] chars = line.toCharArray();
            int startX = Constants.BRICK_START_X;

            for (int col = 0; col < chars.length; col++) {
                char brickType = chars[col];

                int x = startX + col * (Constants.BRICK_WIDTH + Constants.BRICK_SPACING);
                int y = startY - row * (Constants.BRICK_HEIGHT + Constants.BRICK_SPACING);

                switch (brickType) {
                    case 'N':
                        bricks.add(new NormalBrick(x, y));
                        break;
                    case 'S':
                        bricks.add(new StrongBrick(x, y));
                        break;
                    case 'M':
                        bricks.add(new MultiBallBrick(x, y));
                        break;
                    case 'P':
                        bricks.add(new PowerUpBrick(x, y));
                        break;
                    case 'X':
                        bricks.add(new SolidBrick(x, y));
                        break;
                }
            }
        }
        return bricks;
    }
}