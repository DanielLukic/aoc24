package aoc;

import java.io.File

private val searches = listOf("XMAS", "SAMX")

private lateinit var dir: List<Int>

fun main() {
    val input = File("day4.txt").readText()
    val ll = input.indexOf('\n')
    val nl = ll + 1;

    dir = listOf(-nl - 1, -nl, -nl + 1, -1, +1, +nl - 1, +nl, +nl + 1)

    var count = 0;
    for (i in input.indices) {
        for (d in dir) {
            for (s in searches) {
                val match = s.indices.all {
                    val lookup = input.getOrNull(i + d * it)
                    s[it] == lookup
                }
                if (match) count++
            }
        }
    }
    println(count / 2)
}
