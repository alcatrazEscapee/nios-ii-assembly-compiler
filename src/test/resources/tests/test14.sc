compile nios-ii de0

main:
    r5 = 1000;
    r3 = 0;
    r4 = 1;
    r6 = 0;

    // Loop until back to zero
    while r5 > r0:
        r5 -= 1;

        // decrement r3 and reset to 5
        if r3 == r0:
            r3 = 5;
        end
        r3 -= 1;

        // decrement r4 and reset to 3
        if r4 == r0:
            r4 = 3;
        end
        r4 -= 1;

        // if either counter is zero, add the result
        r7 = r3 * r4;
        if r7 == r0:
            r6 += r5;
        end
    end
end