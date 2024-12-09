package aoc;

import java.io.File

private val ops = listOf<(Long, Long) -> Long>(
    { a, b -> a + b },
    { a, b -> a * b },
    { a, b -> (a.toString() + b.toString()).toLong() },
)

private fun is_valid(data: Pair<Long, List<Long>>): Boolean {
    val numbers = data.second
    var results = numbers.take(1)
    for (n in numbers.drop(1)) {
        val temp = mutableListOf<Long>()
        for (o in ops) {
            for (r in results) {
                temp.add(o(r, n))
            }
        }
        results = temp
    }
    return data.first in results
}

fun main() {
    val input = File("day7.txt").readLines().map {
        val data = it.split("[ :]+".toRegex()).map(String::toLong)
        data.first() to data.drop(1)
    }

    println(input)
    println(input.filter(::is_valid))

    val sum = input.filter { is_valid(it) }.sumOf { it.first }
    println(sum)
}
