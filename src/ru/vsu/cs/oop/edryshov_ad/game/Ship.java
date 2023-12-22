package ru.vsu.cs.oop.edryshov_ad.game;

public class Ship implements Comparable<Ship> {
    private final int id;
    private final int firingRange;
    private final int sailRange;
    private final int minSailDepth;
    private final int size;

    private int health;

    private CardinalDirection direction;

    private Ship(Builder builder) {
        this.id = builder.id;
        this.size = builder.size;

        this.direction = builder.direction;

        this.firingRange = builder.firingRange;
        this.sailRange = builder.sailRange;
        this.minSailDepth = builder.minSailDepth;
        this.health = builder.startHealth;
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

    public CardinalDirection getDirection() {
        return direction;
    }

    public int getMinSailDepth() {
        return minSailDepth;
    }

    public boolean isSunk() {
        return health <= 0;
    }

    public void setDirection(CardinalDirection direction) {
        this.direction = direction;
    }

    public void changeHealth(int difference) {
        this.health += difference;
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

    public static class Builder {
        private final int id;
        private final int size;
        private final CardinalDirection direction;

        private int firingRange;
        private int sailRange;
        private int minSailDepth;
        private int startHealth;

        public Builder(int id, int size, CardinalDirection direction) {
            this.id = id;
            this.size = size;
            this.direction = direction;

            this.firingRange = 2;
            this.sailRange = 2;
            this.minSailDepth = 100;
            this.startHealth = size;
        }

        public Builder withFiringRange(int firingRange) {
            this.firingRange = firingRange;
            return this;
        }

        public Builder withSailRange(int sailRange) {
            this.sailRange = sailRange;
            return this;
        }

        public Builder withMinSailDepth(int minSailDepth) {
            this.minSailDepth = minSailDepth;
            return this;
        }

        public Builder withStartHealth(int startHealth) {
            this.startHealth = startHealth;
            return this;
        }

        public Ship build() {
            return new Ship(this);
        }
    }
}
