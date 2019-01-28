compile nios-ii de0

main:
    r2 = 0;
    call EarlyReturn;
    r2 ++;
    call EarlyReturn;
end;

function EarlyReturn:
    if r2 > r0:
        return;
    end;
    r2--;
end;