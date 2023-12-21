package ru.vsu.cs.oop.edryshov_ad.game;

public class Ship implements Comparable<Ship> {
    private final int id;
    private final int firingRange;
    private final int sailRange;
    private final int minSailDepth;
    private final int size;

    private int health;

    private Vector2 direction;

    //TODO сделать билдер с дефолтными значениями
    public Ship(
            int id, int firingRange, int sailRange,
            int minSailDepth, int size, int health,
            Vector2 direction
    ) {
        this.id = id;
        this.firingRange = firingRange;
        this.sailRange = sailRange;
        this.minSailDepth = minSailDepth;
        this.size = size;
        this.health = health;

        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public int getFiringRange() {
        return firingRange;
    }

    public int getSailRange() {
        return sailRange;
    }

    public int getSize() {
        return size;
    }

    public int getHealth() {
        return health;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public int getMinSailDepth() {
        return minSailDepth;
    }

    public boolean isSunk() {
        return health <= 0;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public void changeHealth(int difference) {
        this.health = Math.max(0, health + difference);
    }

    @Override
    public int compareTo(Ship ship) {
        return Integer.compare(id, ship.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ship ship) {
            return id == ship.getId();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Ship %d", id);
    }
}
