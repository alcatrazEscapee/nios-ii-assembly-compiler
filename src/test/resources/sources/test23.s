// ELEC 274 Lab 3
compile nios-ii de0

const JTAG_UART_BASE = 0x10001000
const DATA_OFFSET = 0
const STATUS_OFFSET = 4
const WSPACE_MASK = 0xFFFF

string promptText = "ELEC 274 Lab 3\nType two decimal digits: "
string resultText = "\nYou typed: "

main:
    // Print prompt
    r2 = &promptText
    call PrintString

    // Get decimal digits
    call GetDecimal99
    // Move temp copy to r3
    r3 = r2

    // Print result
    r2 = &resultText
    call PrintString

    // Print the decimal digits
    r2 = r3
    call PrintDecimal99

    r2 = '\n'
    call PrintChar

end


void function PrintString:
    // arguments: r2 = string pointer
    r3 = r2
    r2 = (byte) &r3
    while r2 != r0:
        call PrintChar
        r3 += 1
        r2 = (byte) &r3
    end
end

void function PrintChar:
    // arguments: r2 = character value
    r3 = &JTAG_UART_BASE
    while r4 == r0:
        r4 = (io) &r3[STATUS_OFFSET]
        r4 ?&= WSPACE_MASK
    end
    *r3[DATA_OFFSET] = (io) r2
end

function GetDecimal99:
    // Gets two decimal values as input from the user
    // r2 = character
    // r3 = result
    // r4 = constant '0'
    r4 = '0'
    // r5 = constant '9'
    r5 = '9'
    while r2 < r4:
        while r2 > r5:
            call GetChar
        end
    end
    call PrintChar
    // Calculate tens digit
    r3 = r2 - '0'
    r3 *= 10

    while r2 < r4:
        while r2 > r5:
            call GetChar
        end
    end
    call PrintChar
    r2 -= '0'
    r3 += r2
    // return result
    r2 = r3
end

function GetChar:
    // Gets a single character as input
    // Returns in r2
    r3 = &JTAG_UART_BASE
    while r4 == r0:
        // Read JTAG data register
        r2 = (io) &r3[DATA_OFFSET]
        r4 = r2 & 0x8000
    end
    // return data & 0xFF
    r2 &= 0xFF
end

void function PrintDecimal99:
    // Prints two decimal values to the output
    // arguments: r2 = value in [0, 99]
    // local copy of value
    r3 = r2
    // tens digit
    // no divi :(
    r10 = 10
    r2 = r3 / r10
    r2 += '0'
    call PrintChar

    // ones digit
    r2 = r3 / r10
    r2 *= 10
    r2 = r3 - r2
    r2 += '0'
    call PrintChar
end