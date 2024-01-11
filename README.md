# NemoLogicSolver

NemoLogicSolver is a Java library for solving Nemonemo logic puzzles. It provides a flexible and efficient solution for solving puzzles of this type.

## Features

- Solves Nemonemo logic puzzles with various configurations.
- Supports Java 7.
- Integrates with popular libraries such as Jsoup and json-simple.

## Getting Started

### Prerequisites

- Java 7 or later

### Installation

To use NemoLogicSolver in your project, you can include it as a dependency using Maven or Gradle.

#### Maven:

```xml
<dependency>
    <groupId>com.haeny</groupId>
    <artifactId>nemologic</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

```gradle
implementation 'com.haeny:nemologic:0.0.1-SNAPSHOT'
```

#### Usage

1. Initialize NemoLogicSolver with row and column hints:

```java
int[][] rowsHints = { /* Your row hints here */ };
int[][] columnHints = { /* Your column hints here */ };

NemoLogicSolver solver = new NemoLogicSolver(rowsHints, columnHints);
```

2. Process and solve the puzzle:

```java
try {
    solver.process();
} catch (Exception e) {
    e.printStackTrace();
}
```

3. Retrieve the solution:

```java
// Access the solved grid
long[] solvedGrid = solver.getGrid();

// Process the solution as needed
// ...
```

#### Dependencies
- Jsoup - Java HTML Parser
- json-simple - A simple Java toolkit for JSON

#### License
This project is licensed under the MIT License - see the LICENSE file for details.