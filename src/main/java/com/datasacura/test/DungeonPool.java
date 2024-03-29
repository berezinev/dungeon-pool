package com.datasacura.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class DungeonPool {
    private final List<Dungeon> dungeons;
    private final Map<Dungeon, List<Dungeon>> dungeonCompatibilityGraph;

    public DungeonPool(List<Dungeon> dungeons) {
        validateDungeons(dungeons);
        this.dungeons = dungeons.stream() // making a defence copy
                .map(Dungeon::copy)
                .collect(toList());
        this.dungeonCompatibilityGraph = buildDungeonCompatibilityGraph(this.dungeons);
    }

    public List<Dungeon> createXSequence(int length) {
        if (length == 0) {
            return emptyList();
        }
        if (length > dungeons.size()) {
            throw new IllegalArgumentException("Expected sequence length is greater than number of available dungeons.");
        }

        shuffle();  // alternative could be to have set of compatible dungeons which (in theory) does not guarantee order,
                    // but in real life it will not provide required randomness to the step of next dungeon choosing

        for (Dungeon startingDungeon : this.dungeons) {
            Deque<List<Dungeon>> paths = new ArrayDeque<>();
            Deque<Set<Dungeon>> visitedSets = new ArrayDeque<>(); // memory-performance tradeoff for long paths

            paths.push(List.of(startingDungeon));
            visitedSets.push(Set.of(startingDungeon));

            while (!paths.isEmpty()) {
                List<Dungeon> path = paths.pop();
                if (path.size() == length) {
                    return new ArrayList<>(path);
                }
                Set<Dungeon> visited = visitedSets.pop();
                Dungeon lastDungeon = path.get(path.size() - 1);
                for (Dungeon dungeon : this.dungeonCompatibilityGraph.get(lastDungeon)) {
                    if (!visited.contains(dungeon)) {
                        List<Dungeon> newPath = new ArrayList<>(path);
                        Set<Dungeon> newVisited = new HashSet<>(visited);
                        newPath.add(dungeon);
                        newVisited.add(dungeon);
                        paths.push(newPath);
                        visitedSets.push(newVisited);
                    }
                }
            }
        }
        throw new IllegalStateException("It is not possible to build dungeon sequence with provided length.");
    }

    private Map<Dungeon, List<Dungeon>> buildDungeonCompatibilityGraph(List<Dungeon> dungeons) {
        return dungeons.stream()
                .collect(toMap(identity(),
                        dungeon -> dungeons.stream()
                                .filter(other -> !dungeon.equals(other))
                                .filter(dungeon::isCompatibleWith)
                                .collect(toList())));
    }

    private void validateDungeons(List<Dungeon> dungeons) {
        if (dungeons == null || dungeons.isEmpty()) {
            throw new IllegalArgumentException("Dungeons list must have at least one element");
        }
        dungeons.forEach(this::validateDungeon);
        int referenceNumberOfRows = dungeons.get(0).getNumberOfRows();
        if (!dungeons.stream().allMatch(dungeon -> referenceNumberOfRows == dungeon.getNumberOfRows())) {
            throw new IllegalArgumentException("Dungeons must have same number of rows to be compatible.");
        }
    }

    private void validateDungeon(Dungeon dungeon) {
        requireNonNull(dungeon, "Dungeons list contains null-dungeon.");
        if (!dungeon.isPassable()) {
            throw new IllegalArgumentException("Dungeon list contains non-passable dungeon.");
        }
    }

    private void shuffle() {
        Collections.shuffle(this.dungeons);
        this.dungeonCompatibilityGraph.values().forEach(Collections::shuffle);
    }
}
