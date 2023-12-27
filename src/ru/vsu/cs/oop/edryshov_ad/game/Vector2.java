package ru.vsu.cs.oop.edryshov_ad.game;

public class Vector2 {
    public static final double EPSILON = 1e-6f;

    private final int x;
    private final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int dot(Vector2 other) {
        return x * other.getX() + y * other.getY();
    }

    public double distanceTo(Vector2 other) {
        int difX = x - other.getX(), difY = y - other.getY();
        return Math.sqrt(difX * difX + difY * difY);
    }

    public Vector2 getRotated() {
        return getRotated(true);
    }

    public Vector2 getRotated(boolean clockwise) {
        if (clockwise) {
            return new Vector2(y, -x);
        } else {
            return new Vector2(-y, x);
        }
    }

    public Vector2 getNegative() {
        return new Vector2(-x, -y);
    }
}
