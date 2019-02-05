compile nios-ii de0

const STUFF = 1234

main:
    *r3 = r0
    &r3 = r1
    r3 = *r1
    r2 = &r1
    r0 = &STUFF
end