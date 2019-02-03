compile nios-ii de0

main:
    if r0 >= r1 and (r2 == r3 or r4 != r5):
        call TestLoops
    else
        call TestLoops2
    end
end

void function TestLoops:
    while (r0 < r1 and r1 < r2) or (r3 <= r4 and r4 <= r5):
        r0 = r0
    end
end

void function TestLoops2:
    r3 = '0'
    r4 = '9'
    while r2 < r3 and r2 > r4:
        r0 = r0
    end
end

