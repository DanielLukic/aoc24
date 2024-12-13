package aoc;

import java.io.File

fun main() {
    println("part 1: ${solve(0)}")
    println("part 2: ${solve(10000000000000)}")
}

private fun solve(diff: Long): ULong {

    var tokens = 0UL

    val input = File("day13.txt").readLines().windowed(4, 4, partialWindows = true)
    for (machine in input) {
        val a = machine[0].split("+").let {
            listOf(it[1].substringBefore(",").toInt(), it[2].toInt())
        }
        val b = machine[1].split("+").let {
            listOf(it[1].substringBefore(",").toInt(), it[2].toInt())
        }
        val price = machine[2].split("=").let {
            listOf(it[1].substringBefore(",").toDouble(), it[2].toDouble())
        }.map { it + diff }

        val x = (price[0] * b[1] - price[1] * b[0]) / (a[0] * b[1] - a[1] * b[0])
        val y = (price[1] * a[0] - price[0] * a[1]) / (a[0] * b[1] - a[1] * b[0])
        if ((x % 1) == 0.0 && (y % 1) == 0.0) tokens += x.toULong() * 3UL + y.toULong()
    }

    return tokens
}
