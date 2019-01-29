compile nios-ii de0

int x = 10;

main:
    r2 = 1
    r3 = x
    call Factorial
end

function Factorial:
    if r3 == r0:
        return
    end
    r2 *= r3
    r3--
    call Factorial
end