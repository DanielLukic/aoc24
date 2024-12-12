package aoc;

import java.io.File

private val mul = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()

fun main() {
    val input = File("day3.txt").readText()
    println("part 1: ${summedUp(input)}")

    val segments = input.split("do")
    val on = segments.filterNot { it.startsWith("n't") }
    println("part 2: ${on.sumOf { summedUp(it) }}")
}

private fun summedUp(input: String) = mul.findAll(input).sumOf {
    val (a, b) = it.destructured
    a.toInt() * b.toInt()
}
