// ELEC 274 Lab 2
compile nios-ii de0

const JTAG_UART_BASE = 0x10001000;
const DATA_OFFSET = 0;
const STATUS_OFFSET = 4;
const WSPACE_MASK = 0xFFFF;

string message = "Hello World!\n";
int list = 0, 1, 4, 9, 13, 15;
int size = 6;


main:
    r2 = &message;
    call PrintString;

    r2 = &list;
    r3 = size;
    call PrintHexList;
end

void function PrintString:
    r3 = r2;
    r2 = (byte) &r3;
    while r2 != r0:
        call PrintChar;
        r3 += 1;
        r2 = (byte) &r3;
    end
end

void function PrintChar:
    r3 = &JTAG_UART_BASE;
    while r4 == r0:
        r4 = (io) &r3[STATUS_OFFSET];
        r4 ?&= WSPACE_MASK;
    end
    *r3[DATA_OFFSET] = (io) r2;
end

void function PrintHexDigit:
    // Takes r3 as an argument
    r4 = r3 - 10;
    if r4 < r0:
        r2 = r3 + '0';
    else:
        r2 = r3 + 'A';
    end
    
    call PrintChar;
end

void function PrintHexList:
    // r2 = the list pointer
    // r3 = the size of the list
    r4 = r2;
    while r3 > r0:
        r2 = &r4;
        call PrintHexDigit;
        
        r2 = ',';
        call PrintChar;
        
        r3 -= 1;
        r4 += 4;
    end
end

