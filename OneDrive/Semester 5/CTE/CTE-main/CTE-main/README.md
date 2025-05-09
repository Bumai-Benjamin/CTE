# Compiler for V Language

This project implements a compiler for a simple V language, processing code through seven stages of compilation.

## Project Structure

The compiler consists of the following stages:

1. **Lexical Analysis (Scanner)**
   - Tokenizes input code
   - Identifies keywords, identifiers, operators, and symbols
   - Reports lexical errors

2. **Syntax Analysis (Parser)**
   - Validates code structure
   - Checks for proper statement formation
   - Reports syntax errors

3. **Semantic Analysis**
   - Validates variable declarations and usage
   - Checks for semantic errors
   - Tracks variable scope

4. **Intermediate Code Representation (ICR)**
   - Generates intermediate code
   - Represents program in a simpler form

5. **Code Generation (CG)**
   - Converts intermediate code to target code
   - Generates assembly-like instructions

6. **Code Optimization (CO)**
   - Optimizes generated code
   - Removes redundant operations
   - Combines similar operations

7. **Target Machine Code (TMC)**
   - Converts optimized code to binary
   - Generates 32-bit machine instructions

## Language Features

The V language supports:
- Keywords: BEGIN, INTEGER, LET, INPUT, WRITE, END
- Identifiers: Single letters (A-Z, a-z)
- Operators: +, -, /, *
- Symbols: =, ;

## Error Handling

The compiler detects and reports:
- Lexical errors (invalid characters, misspelled keywords)
- Syntax errors (invalid expressions, combined operators)
- Semantic errors (undeclared variables, invalid symbols)

## Usage

1. Compile the project:
   ```bash
   javac *.java
   ```

2. Run the compiler:
   ```bash
   java Main
   ```

3. Choose processing mode:
   - Option 1: Process all lines through all 7 stages
   - Option 2: Check for errors in all lines, but process only valid lines (5, 7, 8) through all stages

## Example Program

```
BEGIN
INTEGER A, B, C, E, M, N, G, H, I, a, c
INPUT A, B, C
LET B = A */ M
LET G = a + c
temp = <s%**h - j / w +d +*$&;
M = A/B+C
N = G/H-I+a*B/c
WRITE M
WRITEE F;
END
```

## Implementation Details

- Each compiler stage implements the `CompilerStage` interface
- Error reporting includes line numbers and positions
- Code optimization includes:
  - Redundant operation removal
  - Operation combination
  - Dead code elimination
- Target machine code uses 32-bit instructions with:
  - 4-bit opcodes
  - 8-bit operands
  - Proper binary formatting
