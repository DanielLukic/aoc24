package aoc

private class Computer {
    var a = 0
    var b = 0
    var c = 0

    val program = mutableListOf<Int>()
    val output = mutableListOf<Int>()

    fun run(): String {
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
            val literal = operand
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
                    a /= (1 shl combo)
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
                    if (a != 0) ip = literal else ip += 2
                }

                4    -> { // bxc - bitwise XOR of B and C
                    b = b xor c
                    ip += 2
                }

                5    -> { // out - output value
                    output.add(combo % 8)
                    ip += 2
                }

                6    -> { // bdv - division in B register
                    b = a / (1 shl combo)
                    ip += 2
                }

                7    -> { // cdv - division in C register
                    c = a / (1 shl combo)
                    ip += 2
                }

                else -> throw IllegalArgumentException("Invalid opcode: $opcode")
            }
        }

        return output.joinToString(",")
    }
}

// Example usage and test
fun main() {
    println(part1(32916674))
}

private fun part1(a: Int): String {
    val c = Computer()
    c.a = a
    c.program.addAll(listOf(2, 4, 1, 1, 7, 5, 0, 3, 1, 4, 4, 0, 5, 5, 3, 0))
    return c.run()
}
