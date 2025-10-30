package io.exterminator3618.client.utils;

import java.util.List;

/**
 * Helper class to store game's states.
 */
public final class GameSaveData {

    private GameSaveData() { }

    /**
     * Game's infomation.
     */
    public static class SaveData {
        public int level;
        public int score;
        public int lives;

        public Obj ball;
        public Obj paddle;
        public List<Obj> extraBalls;
        public List<BrickState> bricks;
        public List<PowerUpState> activePowerUps;
    }

    /**
     * Moveable object's state (ball, paddle, extra balls).
     * Reuses fields for different object types.
     */
    public static class Obj {
        public int x, y, width, height;
        public double vx, vy;
        public String region;
        public boolean bool1; // reuse: ball: isHeavy, paddle: isSticky
        public boolean bool2; // ball: isStuck
        public int i1; // ball: stuckOffsetX
        public int cb; // ball: comboCount
    }

    /**
     * Brick's state.
     */
    public static class BrickState {
        public int x, y, width, height;
        public String region;
        public int hp;
        public String type;
    }

    /**
     * Active timed power-up's state.
     */
    public static class PowerUpState {
        public String type;
        public float remaining;
    }
}
