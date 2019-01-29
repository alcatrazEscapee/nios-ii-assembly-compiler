compile nios-ii de0

main:
    call TestCompareStatements
end

void function TestCompareStatements:
    r3 = 2
    r4 = 3
    r5 = r2 > 2
    r6 = r2 <= 3
    r7 = r3 ?<= r4
    r8 = r4 ?> r3
    r8 = r3 == r4
    r9 = r2 != r4
    r10 = r2 ?< r3
end