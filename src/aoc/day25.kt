package aoc

import java.io.File

private val input by lazy {
    val it = File("data2024/aoc24d25.txt").readLines().windowed(8, 8, partialWindows = true).map { it.take(7) }
    val locks = it.filter { it.first() == "#####" }.map(::toHeights)
    val keys = it.filter { it.last() == "#####" }.map(::toHeights)
    require(locks.size + keys.size == it.size)
    locks to keys
}

private fun toHeights(rows: List<String>) = rows.first().indices.map { rows.count { row -> row[it] == '#' } - 1 }

fun main() {
    part1(input.first, input.second)
}

private fun part1(locks: List<List<Int>>, keys: List<List<Int>>) {
    val all = locks.flatMap { lock ->
        keys.map { key ->
            lock.zip(key).map { (l, k) -> l + k }
        }
    }
    val ok = all.filter { heights -> heights.all { it <= 5 } }
    println("part 1: ${ok.size}")
}
