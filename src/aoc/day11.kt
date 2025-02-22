package aoc;

import java.io.File

private val blinked = mutableMapOf<Pair<Long, Int>, Long>()

fun blink(stone: Long, blinks: Int): Long {
    if (blinks == 0) return 1L

    val known = blinked[stone to blinks]
    if (known != null) return known

    var num = stone
    blinks.downTo(1).forEach {
        val check = num.toString()
        if (num == 0L) {
            num = 1L
        }
        else if (check.length and 1 == 0) {
            val split = check.windowed(check.length / 2, check.length / 2)
            val a = blink(split.first().toLong(), it - 1)
            val b = blink(split.last().toLong(), it - 1)
            blinked[stone to blinks] = a + b
            return a + b
        }
        else {
            num *= 2024
        }
    }
    blinked[stone to blinks] = 1
    return 1
}

fun main() {
    val input = File("day11.txt").readLines().single().split(" ").map(String::toLong).toMutableList()
    println("part 1: ${input.sumOf { blink(it, 25) }}")
    println("part 2: ${input.sumOf { blink(it, 75) }}")
}
