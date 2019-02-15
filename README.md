# Assembly Compiler

This is a trans-compiler which will generate source code for the Nios-II DE0 processor given input in a custom pseudocode language (referred to hereafter as Reduced Assembly).


### Usage:

The compiler has a basic command line syntax:
```
java -jar [compile|compilef] [input] [output]
```
* `compile`: Use this to compile from an input file to the console
* `compilef`: Use this to compile from an input file and save to an output file
* `input`: The input file argument
* `output`: The output file argument (only for `compilef`)


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

Reduced Assembly consists of single line instructions. Statements are separated by line breaks, or by semicolons (similar to python). Semicolons can also be used to terminate lines - they will be ignored by the compiler.

It is not whitespace independent - spaces are necessary to separate keywords and statements. In addition, each statement should have its own line. Note that the compiler will do the equilvant of find and replace `;` with `\n` - this means that a semicolon can be used anywhere to simulate the effect of a newline, even after conditional or control statements (after the colon)

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
r2 = r2 * r4
r2 *= r4

// As are these two
r3 = r3 ?& 0xFF
r3 ?&= 0xFF
```

Additionally, you can use conditionals as operators. They are translated into comparison (`cmp..`) operators.

```
r3 = r4 <= r2
r3 = r4 != r2
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
* `?<=`: Less than or equal to (Unsigned)
* `?<`: Strictly less than (Unsigned)
* `?>=`: Greater than or equal to (Unsigned)
* `?>`: Strictly greater than (Unsigned)

Example Usage:
```
if r3 >= r0:
    // some statements
end
```

Conditionals can also be used with logical operators, using similar syntax to python. The three logical / boolean operators are (unary) `not`, `and` and `or`. These preform the standard boolean AND, OR and NOT operations.

`(` and `)` can be used to group statements. If left out, the conditional is evaluated left to right. Note that there are no special preference rules, i.e. the following:
```
if r0 < r1 and not r2 < r4 or r2 < r0:
```
will be evaluated as:
```
if (r0 < r1) and ( not ( (r2 < r4) or (r2 < r0) )):
```

All conditionals are constructed using an explicit recursive method, and then optimized at the end of compilation. Labels involved in conditionals will follow the convention `<function prefix>_<if|else|while>number|alphabetic identifier`. These do not indicate particular sections of the conditional when compiled.

### Variables and Constants

There are two main types of variables, based on how they are compiled. The first type is constants:
```
const MY_CONSTANT = 1243
```
These are translated down to `.equ` assembly directives. They can take any integer value. The other type is standard variables:
```
int anInteger = 3
```
These are placed in memory. They can be used directly or as a pointer. They can also be written to, unlike constants.

The general variable declaration syntax is:
```
<type> <variableName> [= <value>]
```
This will declare a variable with type `type` and the name `variableName`. It will assign to it the value `value` if present, otherwise it will leave it unassigned.

Valid variable types include `int` (32-bit word), `byte` (8-bit byte), `string` (byte-sized ASCII text), and `var[<size>]` (variable sized data, see below)

Examples:
```
int anInteger // Allocates space for an integer
byte B = 0xF // a byte with hex value 0xF = 15
string words = "this is a string. each character is 1 byte"
int anArray = 1, 2, 3, 4, 5, 6 // This declares an array with six concecutive values
byte ByteArray = 0, 1, 0, 1 // Bytes can be arrays too
```

Variable sized data can be declared with the `var` keyword. It requires a specifier within square brackets for the size of the data (in bytes). Note that variables declared this way cannot be assigned to immediately.

Example:
```
var[20] largeThing // This is a 20-byte long piece of memory
```

### Control Statements

Control statements are used to construct simple break conditional statements. They are structured similar to Python, requiring no brackets to separate code blocks.

There are five control keywords: `if`, `else`, `while`, `return` and `end`. These are collectively used to form conditional statements and loops.

If Syntax:
```
if <condition>:
    // statements here
end
```
The statements inside the `if - end` block will only execute if `condition` returns true.

If-Else Syntax:
```
if <condition>:
    // statements A
else: // The colon here is optional
    // statements B
end;
```
Statements A will only execute if the condition is true, otherwise statements B will execute. There is no explicit `else if`, however it can be created by chaining nested `if-else` blocks.

While Syntax:
```
while <condition>:
    // statements
end;
```
The statements inside the `while` block will execute at least once, and repeat until the condition is false. (Similar to a C/Java `do-while`). To get a regular while functionality, you need an encompassing `if` statement:
```
if <condition>:
    while <condition>:
        // statements
    end
end
```


Return Syntax:
```
return [optional rX]
```
If `rX` is not present, this will simply jump to the end of the current function. With an `rX` present, it will insert additional line which will move that register to `r2` (as per convention).

### Other Functions

Functions are declared with either the keyword `function`, or as a `void function` if the function has no return value.

Example:
```
function AddValues:
    r2 = r3 + r4
end
```
Breakdown:
* `function` Declares a function. This must be done outside of `main`.
* `AddValues:` This is the name of the function. A colon is used to end the line.
* `r2 = r3 + r4` This is a basic register expression.
* `end` This will end the function and return.

By convention, functions are assumed to take all arguments in r2, r3... and return to r2. Additionally, functions should not modify their arguments (with the exception of r2 if it returns). This is handled automatically by interpreting the contents of the function. It is important to declare functions that have no return value as `void`, otherwise `r2` will not get saved if overwritten.

To call a function, use the `call` keyword followed by the function name, i.e.:
```
main:
    call DoStuff
end

function DoStuff:
end
```

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

* `*rX[OFFSET]`: This indicates to get/set the value at the memory address `rX` plus an offset. The offset is optional. Note that the characters `*` and `&` are completely interchangeable.
* If used on the left side of an assignment, it will store a value to a memory location.
* If used on the right side of an assignment statement, it will load a value from memory.

And finally, when accessing variables or memory locations, it can be useful to specify the type of data by casting. Casting can be done with a number of keywords, specified within brackets in a comma separated format. (Order of casts within the brackets is not important.)

Keywords:
* `byte`, `b`: Byte
* `input`, `output`, `io`: Input / Output
* `byteio`: Deprecated. Indicates both I/O and byte. Should use `byte,io` instead.
* `unsigned`, `u`: Unsigned

Example:
```
r2 = &r3
r2 = (byte) &r3
r2 = (b, unsigned, io) &r3
```
These three expressions inside parenthesis indicate to take a specific type of load instruction (word, byte, wordio, or byteio). The default is word.
Similarly for assignment to an io memory location (for example):
```
*r3 = (io) r2
``` 


### Code Examples

There are various examples of valid Pseudo-Assembly and the corresponding assembly found in /src/test/resources/sources/. Notable examples include:

* `test1`: The default program
* `test3`: Demo of variable declarations
* `test5`: Demo of various operators and assignment statements
* `test9`: Example If Syntax
* `test12`: Example If-Else Syntax
* `test14`: Solution to Project Euler Problem 1
* `test21`: An example selection sort

