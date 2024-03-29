package com.datasacura.test;

import com.datasacura.test.Dungeon.Block;
import org.junit.jupiter.api.Test;

import static com.datasacura.test.Dungeon.Block.AIR;
import static com.datasacura.test.Dungeon.Block.GROUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DungeonTest {

    @Test
    void createDungeonWithNullAreaAndGetNPE() {
        //Given:
        Block[][] area = null;

        //Then:
        Exception expectedException = assertThrows(NullPointerException.class, () -> new Dungeon(area),
                "Creating a dungeon with a null area should throw NullPointerException.");

        //Expected:
        assertEquals("Area must be not null.", expectedException.getMessage(),
                "Exception message should indicate that the area must not be null.");
    }

    @Test
    void createDungeonWithEmptyAreaAndGetIllegalArgumentException() {
        //Given:
        Block[][] area = {};

        //Then:
        Exception expectedException = assertThrows(IllegalArgumentException.class, () -> new Dungeon(area),
                "Creating a dungeon with an empty area should throw IllegalArgumentException");

        //Expected:
        assertEquals("Area must have at least one element.", expectedException.getMessage(),
                "Exception message should indicate that the area must have at least one element.");
    }

    @Test
    void createDungeonWithNonRectangleAreaAndGetIllegalArgumentException() {
        //Given:
        Block[][] area = {{AIR, GROUND, AIR}, {GROUND, AIR}};

        //Then:
        Exception expectedException = assertThrows(IllegalArgumentException.class, () -> new Dungeon(area),
                "Creating a dungeon with a non-rectangle area should throw IllegalArgumentException");

        //Expected:
        assertEquals("Area must be a rectangle.", expectedException.getMessage(),
                "Exception message should indicate that the area must be a rectangle.");
    }

    @Test
    void createDungeonWithCorrectArea() {
        //Given:
        Block[][] area = {
                {AIR, AIR},
                {GROUND, AIR}
        };

        //Then:
        Dungeon dungeon = new Dungeon(area);

        //Expected:
        assertNotNull(dungeon, "A dungeon created with a valid area should successfully initialize.");
    }

    @Test
    void testIfTwoDungeonsAreCompatible() {
        //Given:
        Block[][] area1 = {
                {GROUND, AIR, AIR},
                {AIR, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Block[][] area2 = {
                {AIR, AIR, AIR},
                {GROUND, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon1 = new Dungeon(area1);
        Dungeon dungeon2 = new Dungeon(area2);

        //Then-Expected:
        assertTrue(dungeon1.isCompatibleWith(dungeon2),
                "Dungeon1 should be compatible with Dungeon2 for a valid pair.");
        assertFalse(dungeon2.isCompatibleWith(dungeon1),
                "Dungeon2 should not be deemed compatible with Dungeon1, indicating directional compatibility.");
    }

    @Test
    void testIfClosedDungeonIsPassable() {
        //Given:
        Block[][] area = {
                {GROUND, GROUND, GROUND},
                {GROUND, GROUND, GROUND},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon = new Dungeon(area);

        //Then-Expected:
        assertFalse(dungeon.isPassable(), "A completely closed dungeon should be recognized as not passable.");
    }

    @Test
    void testIfDungeonWithOnlyEntranceIsPassable() {
        //Given:
        Block[][] area = {
                {AIR, GROUND, GROUND},
                {GROUND, GROUND, GROUND},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon = new Dungeon(area);

        //Then-Expected:
        assertFalse(dungeon.isPassable(),
                "A dungeon with only an entrance and no exit should be recognized as not passable.");
    }

    @Test
    void testIfDungeonWithOnlyExitIsPassable() {
        //Given:
        Block[][] area = {
                {GROUND, GROUND, GROUND},
                {GROUND, GROUND, AIR},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon = new Dungeon(area);

        //Then-Expected:
        assertFalse(dungeon.isPassable(),
                "A dungeon with only an exit and no entrance should be recognized as not passable.");
    }

    @Test
    void testIfOpenDungeonIsPassable() {
        //Given:
        Block[][] area = {
                {AIR, GROUND, GROUND},
                {GROUND, GROUND, AIR},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon = new Dungeon(area);

        //Then-Expected:
        assertTrue(dungeon.isPassable(),
                "A dungeon with both an entrance and an exit should be recognized as passable.");
    }

    @Test
    void copyDungeonAndGetCorrectResult() {
        //Given:
        Block[][] area = {
                {AIR, GROUND, GROUND},
                {GROUND, GROUND, AIR},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon = new Dungeon(area);

        //Then:
        Dungeon dungeonCopy = dungeon.copy();

        //Expected:
        assertNotNull(dungeonCopy, "A dungeon with both an entrance and an exit should be recognized as passable.");
        assertEquals(dungeon, dungeonCopy, "The original dungeon and its copy should be equal.");
        assertNotSame(dungeon, dungeonCopy, "The original dungeon and its copy should not be the same.");
        assertEquals(dungeon.hashCode(), dungeonCopy.hashCode(),
                "The hash code of the original dungeon and its copy should be the same, indicating a proper copy.");
    }

    @Test
    void equalsReflexive() {
        //Given:
        Dungeon dungeon = new Dungeon(new Block[][]{{AIR, GROUND}, {GROUND, AIR}});

        //Then-Expected:
        assertEquals(dungeon, dungeon,
                "The equals method should be reflexive, indicating a dungeon is equal to itself.");
    }

    @Test
    void notEqualNull() {
        //Given:
        Dungeon dungeon = new Dungeon(new Block[][]{{AIR, GROUND}, {GROUND, AIR}});

        //Then-Expected:
        assertNotEquals(null, dungeon, "A dungeon should not be considered equal to null.");
    }

    @Test
    void hashCodeConsistency() {
        //Given:
        Dungeon dungeon = new Dungeon(new Block[][]{{AIR, GROUND}, {GROUND, AIR}});

        //Then-Expected:
        assertEquals(dungeon.hashCode(), dungeon.hashCode(),
                "The hash code of a dungeon should remain consistent across multiple calls.");
    }
}