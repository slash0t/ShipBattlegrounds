package ru.vsu.cs.oop.edryshov_ad.game.field;

public class FieldException extends RuntimeException {
    public FieldException(String errorMessage) {
        super("Error performing field action: " + errorMessage);
    }
}