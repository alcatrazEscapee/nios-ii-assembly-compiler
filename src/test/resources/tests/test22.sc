compile nios-ii de0

int size = 20
int list = 4, 2, 28, 110, 6, 69, 420, 123, 321, 3, 0, 11, 9, 16, 21, 11, 110, 3, 2, 1

main:
    r2 = &list
    r3 = size
    call SelectionSort
end

void function SelectionSort:

    // r2 = list pointer
    // r3 = size of list
    // r16 = constant 1
    r16 = 1

    // loop while size is > 1
    while r3 > r16:

        // find the max element in the list

        // r4 = local list pointer
        r4 = r2

        // r5 = pointer to the max element
        r5 = r4
        // r6 = the max element (starts at the first element)
        r6 = &r4

        // counter for loop iteration
        r7 = 0
        while r7 < r3:

            // r8 = word at list pointer (temp)
            r8 = &r4
            if r8 > r6:
                // save the new max and index
                r5 = r4
                r6 = r8
            end

            // Increment counter and pointer index
            r7 ++
            r4 += 4
        end

        // swap the two values (index size-1 and address r5)
        // r8 = memory address of size-1 index
        r8 = r3 - 1
        r8 *= 4
        r8 += r2
        // r9 = memory address of r5
        r9 = r5

        // load values into r10, r11
        r10 = &r8
        r11 = &r9

        // save values in opposite order
        *r8 = r11
        *r9 = r10

        // decrement the size register and sort again
        r3 --
    end

end