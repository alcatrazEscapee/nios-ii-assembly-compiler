// This is a badly formatted version of test7 - to test line standards
compile nios-ii de0;;;
// a comment with a line terminator?;int A = 3;int B = 5;
int C

main:;r2=A;
    r3 =            B;
    call AddValues;C = r2
end;;function AddValues:;r16=r2+r3;
    r2 = r16
end