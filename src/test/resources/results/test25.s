# Generated by Assembly Auto-Compiler by Alex O'Neill
# Setup
    .equ            LAST_RAM_WORD, 0x007FFFFC
    .global         _start
    .org            0x00000000
    .text

# Entry point
_start:
    movia           sp, LAST_RAM_WORD
    movi            r3, -234
    muli            r3, r3, -1
    addi            r3, r2, -14
main_while1:
    movi            r3, 1
    ble             r0, r1, main_if1
    movi            r2, 1
main_if1:
    movi            r1, 1
    br              main_while1
_end:
    br              _end

# Word-Aligned Variables
    .org            0x00001000

x:
    .word           1, 2, 3
a:
    .skip           40
b:
    .skip           12

# Random Variables

s:
    .asciz          "1234"
c:
    .skip           2
d:
    .skip           10
e:
    .skip           3

# End of Assembly Source
    .end