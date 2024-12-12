package aoc;

import java.io.File
import kotlin.math.abs

fun main() {

    val input = File("day1.txt").readLines();
    val pairs = input.map { it.split(" +".toRegex()).map(String::toInt) }
    val a = pairs.map { it.first() }.sorted();
    val b = pairs.map { it.last() }.sorted();
    val p1 = (a zip b).sumOf { abs(it.first - it.second) }
    println("part 1: $p1")

    val h = b.groupingBy { it }.eachCount()
    val p2 = a.sumOf { it * (h[it] ?: 0) }
    println("part 2: $p2")

}
