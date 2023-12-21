package ru.vsu.cs.oop.edryshov_ad.game;

public enum CardinalDirection {
    NORTH {
        @Override
        public CardinalDirection getRight() {
            return EAST;
        }

        @Override
        public CardinalDirection getLeft() {
            return WEST;
        }

        @Override
        public CardinalDirection getNegative() {
            return SOUTH;
        }
    },
    WEST {
        @Override
        public CardinalDirection getRight() {
            return NORTH;
        }

        @Override
        public CardinalDirection getLeft() {
            return SOUTH;
        }

        @Override
        public CardinalDirection getNegative() {
            return EAST;
        }
    },
    SOUTH {
        @Override
        public CardinalDirection getRight() {
            return WEST;
        }

        @Override
        public CardinalDirection getLeft() {
            return EAST;
        }

        @Override
        public CardinalDirection getNegative() {
            return NORTH;
        }
    },
    EAST {
        @Override
        public CardinalDirection getRight() {
            return SOUTH;
        }

        @Override
        public CardinalDirection getLeft() {
            return NORTH;
        }

        @Override
        public CardinalDirection getNegative() {
            return WEST;
        }
    };

    public abstract CardinalDirection getRight();
    public abstract CardinalDirection getLeft();
    public abstract CardinalDirection getNegative();

    public CardinalDirection getRotated(boolean right) {
        if (right) {
            return getRight();
        } else {
            return getLeft();
        }
    }
}
