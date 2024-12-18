package aoc

private class Computer {
    var a = 0L
    var b = 0L
    var c = 0L

    val program = listOf(2, 4, 1, 1, 7, 5, 0, 3, 1, 4, 4, 0, 5, 5, 3, 0)
    val output = mutableListOf<Int>()

    fun run(): MutableList<Int> {
        output.clear()

        // Instruction pointer
        var ip = 0

        while (ip < program.size) {
            // Read opcode
            val opcode = program[ip]

            // Read operand (or null if at end of program)
            val operand = program[ip + 1]

            // There are two types of operands; each instruction specifies the type of its operand. The value of a literal operand is the operand itself. For example, the value of the literal operand 7 is the number 7. The value of a combo operand can be found as follows:
            //
            // Combo operands 0 through 3 represent literal values 0 through 3.
            // Combo operand 4 represents the value of register A.
            // Combo operand 5 represents the value of register B.
            // Combo operand 6 represents the value of register C.
            // Combo operand 7 is reserved and will not appear in valid programs.
            val literal = operand.toLong()
            val combo = when (operand) {
                0, 1, 2, 3 -> literal
                4          -> a
                5          -> b
                6          -> c
                else       -> error("Invalid combo operand: $operand")
            }

            // Execute instruction based on opcode
            when (opcode) {
                0    -> { // adv - division in A register
                    val v = combo.toInt()
                    check(v.toLong() == combo)
                    a /= (1 shl v)
                    ip += 2
                }

                1    -> { // bxl - bitwise XOR in B register with literal
                    b = b xor literal
                    ip += 2
                }

                2    -> { // bst - set B register to operand modulo 8
                    b = combo % 8
                    ip += 2
                }

                3    -> { // jnz - jump if A is not zero
                    val v = literal.toInt()
                    check(v.toLong() == literal)
                    if (a != 0L) ip = v else ip += 2
                }

                4    -> { // bxc - bitwise XOR of B and C
                    b = b xor c
                    ip += 2
                }

                5    -> { // out - output value
                    output.add((combo % 8).toInt())
                    ip += 2
                }

                6    -> { // bdv - division in B register
                    val v = combo.toInt()
                    check(v.toLong() == combo)
                    b = a / (1 shl v)
                    ip += 2
                }

                7    -> { // cdv - division in C register
                    val v = combo.toInt()
                    check(v.toLong() == combo)
                    c = a / (1 shl v)
                    ip += 2
                }

                else -> throw IllegalArgumentException("Invalid opcode: $opcode")
            }
        }

        return output
    }
}

// Example usage and test
fun main() {
    val c = Computer()
    c.a = 32916674
    println(c.run().joinToString(","))

    fun search(value: Long, program_position: Int): Long {
        if (program_position < 0) return value

        val start = value shl 3
        val end = start + 8
        for (i in start..<end) {
            c.a = i
            val check = c.run()
            if (check.first() == c.program[program_position]) {
                val next = search(i, program_position - 1)
                if (next != -1L) return next
            }
        }
        return -1L
    }

    val result = search(0, c.program.size - 1);
    println("part 2: $result")
}
