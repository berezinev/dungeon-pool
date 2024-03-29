package com.datasacura.test;

import com.datasacura.test.Dungeon.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.datasacura.test.Dungeon.Block.AIR;
import static com.datasacura.test.Dungeon.Block.GROUND;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DungeonPoolTest {
    private Random random;

    @BeforeEach
    void setUp() {
        random = new Random(12);
    }

    @Test
    void creationWithNonEmptyListShouldSucceed() {
        //Given: There is at least one dungeon available
        Block[][] area = {
                {GROUND, AIR, AIR},
                {AIR, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Dungeon dungeon = new Dungeon(area);
        List<Dungeon> givenDungeons = singletonList(dungeon);

        //Then: DungeonPool is created with this list
        DungeonPool pool = new DungeonPool(givenDungeons);

        //Expected: The creation should succeed without exceptions
        assertNotNull(pool, "DungeonPool should be successfully created with a non-empty list of dungeons.");
    }

    @Test
    void creationWithNullListShouldThrowIllegalArgumentException() {
        //Given: a null list of dungeons
        List<Dungeon> dungeons = null;

        //Then: creating a DungeonPool should throw an IllegalArgumentException
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, () -> new DungeonPool(dungeons));

        //Expected:
        assertEquals("Dungeons list must have at least one element", expectedException.getMessage(),
                "The exception message should indicate that the dungeons list cannot be null or empty.");
    }

    @Test
    void creationWithEmptyListShouldThrowIllegalArgumentException() {
        //Given: a null list of dungeons
        List<Dungeon> dungeons = new ArrayList<>();

        //Then: creating a DungeonPool should throw an IllegalArgumentException
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, () -> new DungeonPool(dungeons));

        //Expected:
        assertEquals("Dungeons list must have at least one element", expectedException.getMessage(),
                "The exception message should indicate that the dungeons list cannot be null or empty.");
    }

    @Test
    void creationWithListContainingNullShouldThrowException() {
        //Given: a list of dungeons containing null
        Block[][] area = {
                {GROUND, AIR, AIR},
                {AIR, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Dungeon dungeon = new Dungeon(area);
        List<Dungeon> dungeons = Arrays.asList(dungeon, null);

        //Then: creating a DungeonPool should throw a NullPointerException
        NullPointerException expectedException = assertThrows(NullPointerException.class, () -> new DungeonPool(dungeons));

        //Expected:
        assertEquals("Dungeons list contains null-dungeon.", expectedException.getMessage(),
                "The exception message should indicate that the dungeons list cannot contain null.");
    }

    @Test
    void creationWithNonPassableDungeonShouldThrowException() {
        //Given: a list of dungeons containing null
        Block[][] area = {
                {GROUND, AIR, AIR},
                {GROUND, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Dungeon dungeon = new Dungeon(area);
        List<Dungeon> dungeons = List.of(dungeon);

        //Then: creating a DungeonPool should throw a IllegalArgumentException
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, () -> new DungeonPool(dungeons));

        //Expected:
        assertEquals("Dungeon list contains non-passable dungeon.", expectedException.getMessage(),
                "The exception message should indicate that all dungeons in the list must be passable.");
    }

    @Test
    void creationWithDifferentDungeonsShouldThrowException() {
        //Given: a list of dungeons containing different dungeons
        Block[][] area1 = {
                {AIR, AIR, AIR},
                {GROUND, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Block[][] area2 = {
                {GROUND, AIR, AIR},
                {AIR, AIR, GROUND},
        };
        Dungeon dungeon1 = new Dungeon(area1);
        Dungeon dungeon2 = new Dungeon(area2);
        List<Dungeon> dungeons = List.of(dungeon1, dungeon2);

        //Then: creating a DungeonPool should throw a IllegalArgumentException
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, () -> new DungeonPool(dungeons));

        //Expected:
        assertEquals("Dungeons must have same number of rows to be compatible.", expectedException.getMessage(),
                "The exception message should indicate that all dungeons in the list must be have the same number of rows.");
    }

    @Test
    void createXSequenceWithZeroLengthShouldReturnEmptySequence() {
        //Given: a DungeonPool with only one dungeon
        Block[][] area = {
                {GROUND, AIR, AIR},
                {AIR, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Dungeon dungeon = new Dungeon(area);
        DungeonPool pool = new DungeonPool(List.of(dungeon));

        //Then: requesting a sequence longer than the number of available dungeons
        int requestedLength = 0;
        List<Dungeon> xSequence = pool.createXSequence(requestedLength);

        //Expected:
        assertTrue(xSequence.isEmpty(), "The sequence should be empty when requesting a sequence of zero length.");
    }

    @Test
    void createXSequenceWithLengthGreaterThanAvailableShouldThrowException() {
        //Given: a DungeonPool with only one dungeon
        Block[][] area = {
                {GROUND, AIR, AIR},
                {AIR, AIR, GROUND},
                {GROUND, GROUND, GROUND}
        };
        Dungeon dungeon = new Dungeon(area);
        DungeonPool pool = new DungeonPool(List.of(dungeon));

        //Then: requesting a sequence longer than the number of available dungeons
        int requestedLength = 2;
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, () -> pool.createXSequence(requestedLength));

        //Expected:
        assertEquals("Expected sequence length is greater than number of available dungeons.", expectedException.getMessage(),
                "The exception message should indicate that the requested sequence length exceeds the number of available dungeons.");
    }

    @Test
    void findSequenceForTwoCompatibleDungeons() {
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

        DungeonPool dungeonPool = new DungeonPool(List.of(dungeon1, dungeon2));

        //Then:
        List<Dungeon> dungeonSequence = dungeonPool.createXSequence(2);

        //Expected:
        assertEquals(2, dungeonSequence.size(), "The dungeon sequence should contain exactly 2 dungeons.");
        assertEquals(dungeon1, (dungeonSequence.get(0)), "The first dungeon in the sequence should be dungeon1.");
        assertEquals(dungeon2, (dungeonSequence.get(1)), "The first dungeon in the sequence should be dungeon2.");
    }

    @Test
    void findSequenceForTwoCompatibleDungeonsWithTwoOptions() {
        //Given:
        Block[][] area1 = {
                {GROUND, AIR, AIR},
                {GROUND, AIR, GROUND},
                {AIR, GROUND, GROUND}
        };
        Block[][] area2a = {
                {AIR, AIR, GROUND},
                {GROUND, GROUND, AIR},
                {GROUND, GROUND, GROUND}
        };
        Block[][] area2b = {
                {AIR, AIR, GROUND},
                {GROUND, AIR, AIR},
                {GROUND, GROUND, GROUND}
        };

        Dungeon dungeon1 = new Dungeon(area1);
        Dungeon dungeon2a = new Dungeon(area2a);
        Dungeon dungeon2b = new Dungeon(area2b);

        DungeonPool dungeonPool = new DungeonPool(List.of(dungeon1, dungeon2a, dungeon2b));

        //Then:
        List<Dungeon> dungeonSequence = dungeonPool.createXSequence(2);

        //Expected:
        assertEquals(2, dungeonSequence.size(), "The dungeon sequence should contain exactly 2 dungeons.");
        assertEquals(dungeon1, dungeonSequence.get(0), "First dungeon in the sequence should be dungeon1.");
        assertTrue(dungeonSequence.get(1).equals(dungeon2a) || dungeonSequence.get(1).equals(dungeon2b),
                "Second dungeon in the sequence should be either dungeon2a or dungeon2b.");
    }

    @Test
    void findSequenceForRandomTest() {
        //Given:
        List<Dungeon> dungeons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dungeons.addAll(generateDungeonsPath(50, 100));
        }

        DungeonPool dungeonPool = new DungeonPool(dungeons);

        //Then:
        List<Dungeon> xSequence = dungeonPool.createXSequence(50);

        //Expected:
        assertEquals(50, xSequence.size(),
                "The dungeon sequence should contain exactly 50 dungeons when requesting a sequence of length 50.");
    }

    private List<Dungeon> generateDungeonsPath(int length, int dungeonSize) {
        List<Dungeon> result = new ArrayList<>(dungeonSize);
        Set<Integer> enters = generateRandomIndexes(dungeonSize);
        Set<Integer> exits = generateRandomIndexes(dungeonSize);
        for (int i = 0; i < length; i++) {
            Block[][] area = generateGroundArea(dungeonSize);
            openEntrancesAt(area, enters);
            openExitsAt(area, exits);
            enters = exits;
            exits = generateRandomIndexes(dungeonSize);
            result.add(new Dungeon(area));
        }
        return result;
    }

    private Block[][] generateGroundArea(int size) {
        Block[][] area = new Block[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                area[row][col] = GROUND;
            }
        }
        return area;
    }

    private void openEntrancesAt(Block[][] area, Collection<Integer> rows) {
        for (int row : rows) {
            area[row][0] = AIR;
        }
    }

    private void openExitsAt(Block[][] area, Collection<Integer> rows) {
        for (int row : rows) {
            int length = area[row].length;
            area[row][length - 1] = AIR;
        }
    }

    private Set<Integer> generateRandomIndexes(int size) {
        Set<Integer> indexes = new HashSet<>();
        int count = 1;
        for (int i = 0; i < count; i++) {
            indexes.add(random.nextInt(size));
        }
        return indexes;
    }
}