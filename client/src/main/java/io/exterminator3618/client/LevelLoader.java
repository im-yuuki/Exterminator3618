package io.exterminator3618.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này có nhiệm vụ duy nhất là đọc file thiết kế màn chơi
 * và tạo ra một danh sách các viên gạch.
 */
public class LevelLoader {

    public static List<Brick> load(String filePath) {
        List<Brick> bricks = new ArrayList<>();

        // Sử dụng LibGDX để đọc file từ thư mục assets
        FileHandle handle = Gdx.files.internal(filePath);
        String text = handle.readString();

        // Tách file thành từng dòng
        String[] lines = text.split("\\r?\\n");

        // Vị trí bắt đầu vẽ lưới gạch từ trên xuống
        int startY = Constants.WINDOW_HEIGHT - 100;

        for (int row = 0; row < lines.length; row++) {
            String line = lines[row];
            char[] chars = line.toCharArray();
            // Vị trí bắt đầu vẽ lưới gạch từ trái qua
            int startX = 50;

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