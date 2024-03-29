# Dungeon Pool

## Overview

The `Dungeon Pool` is designed to create and manage dungeons within a gaming world. Key components include:

- `Dungeon`: A class that defines the structure of a dungeon, including its passability and compatibility with other dungeons.
- `DungeonPool`: A class that manages a collection of dungeons, providing functionality to generate sequences of compatible dungeons.

## Features

- **Dungeon Creation**: Dynamically create dungeons with specified structures.
- **Compatibility Checks**: Determine if two dungeons are compatible based on their structure.
- **Sequence Generation**: Generate sequences of dungeons that are compatible with each other.
- **Unit Testing**: Comprehensive tests for the core functionalities.

## Getting Started

### Prerequisites

- Java JDK 11 or newer
- Maven

### Building the Project

To compile the project and generate the executable, navigate to the project directory and run:

```shell
mvn clean compile
```

### Running Tests

Execute the following command to run the unit tests:

```shell
mvn test
```

## Usage

### Creating a Dungeon

```java
Dungeon.Block[][] area = {
    {Dungeon.Block.AIR, Dungeon.Block.GROUND},
    {Dungeon.Block.GROUND, Dungeon.Block.AIR}
};
Dungeon dungeon = new Dungeon(area);
```

### Creating a DungeonPool

```java
List<Dungeon> dungeons = Arrays.asList(dungeon1, dungeon2, dungeon3);
DungeonPool pool = new DungeonPool(dungeons);
```

### Generating a Compatible Sequence

```java
List<Dungeon> sequence = pool.createXSequence(2);
```

## Contributing

We welcome contributions to the Dungeon Project! Please feel free to submit pull requests or create issues for bugs and feature requests.

## License

This project is licensed under the MIT License.