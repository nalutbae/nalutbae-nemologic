# NemoLogicSolver

NemoLogicSolver is a Java library for solving Nemonemo logic puzzles. It provides a flexible and efficient solution for solving puzzles of this type.

## Features

- Solves Nemonemo logic puzzles with various configurations.
- Supports Java 7.
- Integrates with popular libraries such as Jsoup and json-simple.

## Getting Started

### Prerequisites

- Java 7 or later

### Usage

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

### Dependencies
- Jsoup - Java HTML Parser
- json-simple - A simple Java toolkit for JSON

### License
This project is licensed under the [The Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)