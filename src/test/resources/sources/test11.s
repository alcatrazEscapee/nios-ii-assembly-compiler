compile nios-ii de0

main:
    r2 = 1;
    // This is true
    if r2 != r0:
        r2 = 2;
        // This is also true
        if r2 > r0:
            r2 = 3;
        end
        r3 = 1;
    end
    r4 = 2;
end