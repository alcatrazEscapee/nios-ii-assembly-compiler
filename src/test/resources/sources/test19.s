compile nios-ii de0

main:
    call DoStuff;
    call DogSrock;
end;

void function DoStuff:
    return;
end;

function DogSrock:
    r10 = 1234;
    if r10 > r0:
        return r10;
    end;
end;