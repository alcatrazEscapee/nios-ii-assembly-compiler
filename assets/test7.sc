compile nios-ii de0

int A = 3;
int B = 5;
int C;

main:
    r2 = A;
    r3 = B;
    call AddValues;
    C = r2;
end

function AddValues:
    r16 = r2 + r3;
    r2 = r16;
end