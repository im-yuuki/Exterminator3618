package io.exterminator3618.client;

import io.exterminator3618.client.components.Ball;
import io.exterminator3618.client.components.Brick;
import io.exterminator3618.client.components.Paddle;
import io.exterminator3618.client.utils.Physics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionTest {

    Ball ball1 = new Ball(15, 15, 10, 10, null, 5, 5);
    Ball ball2 = new Ball(20, 20, 10, 10, null, -5, -5);
    Ball ball3 = new Ball(100, 99, 10, 10, null, -5, 0);

    Paddle paddle = new Paddle(80, 90, 50, 10, null);

    Brick brick1 = new Brick(10, 10, 30, 10, null, 1, "strong");
    Brick brick2 = new Brick(50, 50, 30, 10, null, 1, "strong");

    @Test
    public void testObjectColision() {
        assertTrue(Physics.checkAABBCollision(ball1, ball2));
        assertFalse(Physics.checkAABBCollision(ball1, ball3));
        assertTrue(Physics.checkAABBCollision(ball2, ball2));
    }

    @Test
    public void testBallPaddleCollision() {
        ball3.setVelocity(-5, -5);
        assertTrue(Physics.checkBallPaddleCollision(ball3, paddle, 105));
        assertFalse(Physics.checkBallPaddleCollision(ball3, paddle, 90));
    }

    @Test
    public void testBallBrickCollision() {
        assertTrue(Physics.checkBallBrickCollision(ball1, brick1));
        assertFalse(Physics.checkBallBrickCollision(ball3, brick2));
    }

}
