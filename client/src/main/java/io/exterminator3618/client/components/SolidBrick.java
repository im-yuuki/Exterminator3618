package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;

/**
 * Represents an indestructible brick that cannot be destroyed by the ball.
 * It acts as a permanent wall or obstacle.
 */
public class SolidBrick extends Brick {

    /**
     * Creates a new indestructible brick.
     *
     * @param x initial X position in pixels
     * @param y initial Y position in pixels
     */
    public SolidBrick(int x, int y) {
        // Sử dụng một hình ảnh trông cứng cáp, ví dụ THICK_YELLOW_BRICK
        // hitPoints có thể là bất kỳ số nào, vì nó sẽ không bao giờ bị trừ.
        // Type là "indestructible" để phân biệt.
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.THICK_BLUE_BRICK, 1, "solid_brick");
    }

    /**
     * Ghi đè (Override) phương thức takeHit.
     * Đây là điểm mấu chốt: phương thức này không làm gì cả.
     * Nó không trừ hitPoints và luôn trả về false (không bị phá hủy).
     * @return luôn luôn là false
     */
    @Override
    public boolean takeHit() {
        // Không làm gì cả. Gạch này không nhận sát thương.
        return false;
    }
}