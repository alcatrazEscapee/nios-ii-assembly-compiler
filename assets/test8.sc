compile nios-ii de0

int x;

main:
    r3 = 4;
    r4 = r3;
    r5 = x;
    x = r3;
    r5 += r3;
end