package aoc;

import java.io.File

private val searches = listOf("MAS", "SAM")

private lateinit var dir: List<Int>

fun main() {
    val input = File("day4.txt").readText()
    val ll = input.indexOf('\n')
    val nl = ll + 1;

    dir = listOf(-nl - 1, -nl, -nl + 1, -1, +1, +nl - 1, +nl, +nl + 1)

    var count = 0;
    for (i in input.indices.toList().dropLast(2 * nl + 2)) {
        if (input[i + nl + 1] != 'A') continue
        val a1 = input[i]
        val a2 = input[i + nl * 2 + 2]
        val b1 = input[i + 2]
        val b2 = input[i + nl * 2]
        val a = (a1 == 'M' && a2 == 'S') || (a1 == 'S' && a2 == 'M')
        val b = (b1 == 'M' && b2 == 'S') || (b1 == 'S' && b2 == 'M')
        if (a && b) count++
    }

    println(count)
}
