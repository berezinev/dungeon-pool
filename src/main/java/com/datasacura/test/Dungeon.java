package com.datasacura.test;

import java.util.UUID;

import static com.datasacura.test.Dungeon.Block.AIR;
import static java.util.Arrays.stream;

public class Dungeon {

    private final UUID id;
    private final Block[][] area;
    private final int numberOfRows;
    private final int numberOfColumns;

    public Dungeon(Block[][] area) {
        this(area, UUID.randomUUID());
    }

    private Dungeon(Block[][] area, UUID id) {
        validateArea(area);
        this.numberOfRows = area.length;
        this.numberOfColumns = area[0].length;
        this.area = stream(area)
                .map(Block[]::clone)
                .toArray(Block[][]::new);
        this.id = id;
    }

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    public boolean isCompatibleWith(Dungeon otherDungeon) {
        if (otherDungeon == null) {
            throw new NullPointerException("Compatible dungeon must not be null.");
        }
        if (otherDungeon.area.length != this.area.length) {
            throw new IllegalStateException("Compatible dungeon must have the same amount of rows.");
        }

        for (int rowIndex = 0; rowIndex < this.numberOfRows; rowIndex++) {
            if (this.isExitFreeAt(rowIndex) && otherDungeon.isEntranceFreeAt(rowIndex)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPassable() {
        return haveFreeEntrance() && haveFreeExit();
    }

    public Dungeon copy() {
        return new Dungeon(this.area, this.id);
    }

    public enum Block {
        GROUND,
        AIR
    }

    private Block getEntranceAt(int rowIndex) {
        return this.area[rowIndex][0];
    }

    private Block getExitAt(int rowIndex) {
        return this.area[rowIndex][this.numberOfColumns - 1];
    }

    private boolean isEntranceFreeAt(int rowIndex) {
        return this.getEntranceAt(rowIndex) == AIR;
    }

    private boolean isExitFreeAt(int rowIndex) {
        return this.getExitAt(rowIndex) == AIR;
    }

    private boolean haveFreeEntrance() {
        for (int rowIndex = 0; rowIndex < this.numberOfRows; rowIndex++) {
            if (isEntranceFreeAt(rowIndex)) {
                return true;
            }
        }
        return false;
    }

    private boolean haveFreeExit() {
        for (int rowIndex = 0; rowIndex < this.numberOfRows; rowIndex++) {
            if (isExitFreeAt(rowIndex)) {
                return true;
            }
        }
        return false;
    }

    private void validateArea(Block[][] area) {
        if (area == null) {
            throw new NullPointerException("Area must be not null.");
        }

        if (area.length == 0 || area[0].length == 0) {
            throw new IllegalArgumentException("Area must have at least one element.");
        }

        int referenceNumberOfColumns = area[0].length;
        for (Block[] blocks : area) {
            if (blocks == null || referenceNumberOfColumns != blocks.length) {
                throw new IllegalArgumentException("Area must be a rectangle.");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dungeon dungeon = (Dungeon) o;
        return this.id == dungeon.id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
