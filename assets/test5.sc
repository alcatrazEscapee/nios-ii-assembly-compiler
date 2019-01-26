compile nios-ii de0

int x = 31234;
int y = 4;

main:
    r2 = x;
    r2 = r2 + 3;
    r2 = r2 - 6;
    r2 = r2 * 132;
    r3 = y;
    r3 += r0;
    r3 -= 234;
    r3 *= 24;
    r3 /= r2;
end