# Assembly Compiler

This is a trans-compiler which will generate source code for the Nios-II DE0 processor given input in a custom pseudocode (Pseudo-Assembly).

### Overview

Example 1: Starter Program
```
compile nios-ii de0

main:
end
```
Breakdown:

`compile nios-ii de0`: This indicates that this should be compiled for Nios-II DE0 Assembly. Currently this is the only option, and must be included at the top of every file.

`main:`: This is the declaration of a main function, or entry point. Instructions immediately following this will be executed at runtime.

`end`: This indicates the end of the current block (in this case the main function). Every function (including main) must have a matching `end`.

### Basic Syntax

Precompiled Assembly consists of single line instructions. Every line must be terminated by either a `;` (for the majority of statements), a `:` (for conditional statements). There are a few exceptions:
* `compile` requires no line terminator. It must appear first in your program.
* A few control statements don't require a line terminator, but will compile with or without one. As a general rule, include a semicolon after every standard statement, and a colon after every conditional. (`else` should have a colon, `return` should have a semicolon)

Comments start with two forward slashes (`//`). Anything on the rest of that line will be ignored by the compiler. In the case that a comment is inserted within a function, the compiler will generate a Nios-II comment and insert it in the closest spot it can find. Default "template" comments will also be inserted to document additions to the program where necessary.

### Operators

Operators are used in register expressions to form the majority of code. They convert two register values, or one register and one immediate value to a single value to be assigned to a result register or memory location.

As a general rule, operators are similar to C or Java style operators. When an operator is prefixed with a `?`, that typically means an alternate form of the operator (unsigned, logical, or high half-byte)

Standard Operators Syntax:
* `+`: Addition
* `-`: Subtraction
* `*`: Multiplication
* `/`: Integer division - Cannot be done with an immediate value
* `?/`: Unsigned integer division
* `&`: Bitwise AND
* `|`: Bitwise OR
* `^`: Bitwise XOR
* `?&`: Bitwise AND high half-byte - Only with immediate values
* `?|`: Bitwise OR high half-byte - Only with immediate values
* `?^`: Bitwise XOR high half-byte - Only with immediate values
* `<<`: Bit shift left
* `>>`: Bit shift right
* `?>>`: Bit shift right (logical / unsigned)

Unary Operators Syntax:
* `++`: Increment by one
* `--`: Decrement by one

All standard operators can also be added to an `=` to form a assignment-operator. (i.e. `+` -> `+=`.) 
```
// These two statements are the same
r2 = r2 * r4;
r2 *= r4;

// As are these two
r3 = r3 ?& 0xFF;
r3 ?&= 0xFF;
```

### Conditionals

Conditionals are very similar to other languages. They must consist of two registers with a conditional operator in the middle.

Conditional Syntax:
* `==`: equal to
* `!=`: Not equal to
* `<=`: Less than or equal to
* `<`: Strictly less than
* `>=`: Greater than or equal to
* `>`: Strictly greater than

Example Usage:
```
if r3 >= r0:
    // some statements
end
```

### Variables and Constants

There are two main types of variables, based on how they are compiled. The first type is constants:
```
const MY_CONSTANT = 1243;
```
These are translated down to `.equ` assembly directives. They can take any integer value. The other type is standard variables:
```
int anInteger = 3;
```
These are placed in memory. They can be used directly or as a pointer. They can also be written to, unlike constants.

The general variable declaration syntax is:
```
<type> <variableName> [= <value>];
```
This will declare a variable with type `type` and the name `variableName`. It will assign to it the value `value` if present, otherwise it will leave it unassigned.

Valid variable types include `int` (32-bit word), `byte` (8-bit byte), `string` (byte-sized ASCII text), and `var[<size>]` (variable sized data, see below)

Examples:
```
int anInteger; // Allocates space for an integer
byte B = 0xF; // a byte with hex value 0xF = 15
string words = "this is a string. each character is 1 byte";
int anArray = 1, 2, 3, 4, 5, 6; // This declares an array with six concecutive values
byte ByteArray = 0, 1, 0, 1; // Bytes can be arrays too
```

Variable sized data can be declared with the `var` keyword. It requires a specifier within square brackets for the size of the data (in bytes). Note that variables declared this way cannot be assigned to immediately.

Example:
```
var[20] largeThing; // This is a 20-byte long piece of memory
```

### Control Statements

Precompiled Assembly comes with three main control blocks: `if`, `else`, and `while`. (Note that the `while` loop closer resembles a `do-while` loop in most languages, as it will always execute at least once). It also includes a `return` keyword to immediately break out of a function.

Syntax for `if`:
```
if <condition>:
    // statements here
end
```
The statements inside the `if - end` block will only execute if `condition` returns true.

Syntax for `if-else`:
```
if <condition>:
    // statements A
else: // The colon here is optional
    // statements B
end
```
Statements A will only execute if the condition is true, otherwise statements B will execute. There is no explicit `else if`, however it can be created by chaining nested `if-else` blocks.

Syntax for `while`:
```
while <condition>:
    // statements
end
```
The statements inside the `while` block will execute at least once, and repeat until the condition is false.

Syntax for `return`:
```
return [optional rX];
```
If `rX` is not present, this will simply jump to the end of the current function. With an `rX` present, it will insert additional line which will move that register to `r2` (as per convention).

### Other Functions

Functions are declared with either the keyword `function`, or as a `void function` if the function has no return value.

Example:
```
function AddValues:
    r2 = r3 + r4;
end
```
Breakdown:
* `function` Declares a function. This must be done outside of `main`.
* `AddValues` This is the name of the function. A colon is used to end the line.
* `r2 = r3 + r4;` This is a basic register expression.
* `end` This will end the function and return.

By convention, functions are assumed to take all arguments in r2, r3... and return to r2. Additionally, functions should not modify their arguments (with the exception of r2 if it returns). This is handled automatically by interpreting the contents of the function. It is important to declare functions that have no return value as `void`, otherwise `r2` will not get saved if overwritten.

### Register Expressions

Register Expressions are responsible for most lines of code that can be written. They can operate on one, two or three registers, can read and write from memory, or read constant values.

There are a few different ways to use register expressions:

Assignment:
* `rX = rY`: Assign `rY` to `rX`.
* `rX = CONSTANT`: Assign a constant value to `rX`.
* `rX = VARIABLE`: Assign `rX` to the value of a variable.
* `rX = &VARIABLE`: Assign `rX` to the memory address of a variable

Operators:
* `rX = rY <operator> rZ`: Assign the value of `rY <operator> rZ` to the register `rX`
* `rX <operator-assignment> rY`: An alias for `rX = rX <operator> rY`.
* `rX <unary-operator>`: Used with the two unary operators. An alias for `rX = rX <operator> 1`.

Register expressions can also use registers as pointers:

* `*rX[OFFSET]`: This indicates to set the value at the memory address `rX` plus an offset. It is used on the left hand side of register expressions. 
* `&rX[OFFSET]`: This indicates the value at the memory address of `rX`, plus an offset. The square brackets and offset are optional.

And finally, when accessing variables or memory locations, it can be useful to specify the type of data by casting.

Example:
```
r2 = (byte) &r3;
r2 = (io) &r3;
r2 = (byteio) &r3;
```
These three expressions inside parenthesis indicate to take a specific type of load instruction (word, byte, wordio, or byteio). The default is word.
Similarly for assignment to an io memory location (for example):
```
*r3 = (io) r2;
``` 

