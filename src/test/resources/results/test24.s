# Generated by Assembly Auto-Compiler by Alex O'Neill
# Setup
    .equ            LAST_RAM_WORD, 0x007FFFFC
    .equ            STUFF, 1234
    .global         _start
    .org            0x00000000
    .text

# Entry point
_start:
    movia           sp, LAST_RAM_WORD
    stw             r0, 0(r3)
    stw             r1, 0(r3)
    ldw             r3, 0(r1)
    ldw             r2, 0(r1)
    movia           r0, STUFF
_end:
    br              _end

# End of Assembly Source
    .end