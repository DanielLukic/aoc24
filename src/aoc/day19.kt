package aoc

import java.io.File

private val input by lazy { File("data2024/aoc24d19.txt").readLines() }
private val available by lazy { input[0].split(",").map(String::trim) }
private val desired by lazy { input.drop(2) }
private val memo by lazy { mutableMapOf<String, Long>() }

fun main() {
    val result = desired.map { it to combinations(it) }
    println("part 1: ${result.count { it.second > 0 }}")
    println("part 2: ${result.sumOf { it.second }}")
}

private fun combinations(t: String): Long = memo[t] ?: run {
    val result = if (t.isEmpty()) {
        1L
    }
    else {
        available.filter(t::startsWith).map(t::removePrefix).sumOf(::combinations)
    }
    memo[t] = result
    return result
}
