compile nios-ii de0

const X = 1234;
int Y = 4321;

main:
    // load word
    r3 = Y;
    // move immediate
    r4 = X;
    // move immediate address
    r5 = &Y;
    // move immediate address
    r6 = &X;
end