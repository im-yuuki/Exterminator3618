package io.exterminator3618.client.utils;

import io.exterminator3618.client.*;
import io.exterminator3618.client.components.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này có nhiệm vụ duy nhất là đọc file thiết kế màn chơi
 * và tạo ra một danh sách các viên gạch.
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
            // Tách file thành từng dòng
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.warn("Load level failed", e);
        }

        // Vị trí bắt đầu vẽ lưới gạch từ trên xuống
        int startY = Constants.WINDOW_HEIGHT - 120;

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] chars = line.toCharArray();
            // Vị trí bắt đầu vẽ lưới gạch từ trái qua
            int startX = Constants.BRICK_START_X;

            for (int col = 0; col < chars.length; col++) {
                char brickType = chars[col];

                // Tính toán vị trí x, y cho từng viên gạch
                int x = startX + col * (Constants.BRICK_WIDTH + Constants.BRICK_SPACING);
                int y = startY - row * (Constants.BRICK_HEIGHT + Constants.BRICK_SPACING);

                // Dựa vào ký tự để tạo ra đúng loại gạch
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
                    case 'X': // Dùng tên 'SolidBrick' hoặc tên bạn đã chọn
                        bricks.add(new SolidBrick(x, y));
                        break;
                    // Thêm các loại gạch khác nếu có
                }
            }
        }
        return bricks;
    }
}