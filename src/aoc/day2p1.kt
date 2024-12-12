package aoc;

import java.io.File

fun main() {
    val input = File("day2.txt").readLines();
    val reports = input.map { it.split(" ").map(String::toInt) }
    println("part 1: ${reports.count { isSafe(it) }}")
}

private fun isSafe(report: List<Int>): Boolean {
    val diffs = report.zipWithNext { a, b -> b - a }
    return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
}
