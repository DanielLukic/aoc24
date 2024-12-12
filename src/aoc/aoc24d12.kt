package aoc;

import java.io.File

private data class Plot(val x: Int, val y: Int, val c: Char) {
    val sides = setOf("${x - 1}X$x|$y", "${x}X${x + 1}|$y", "${y - 1}Y$y|$x", "${y}Y${y + 1}|$x")
}

fun main() {
    solvePart1("aoc24d12t1.txt")
    solvePart1("aoc24d12t2.txt")
    solvePart1("aoc24d12t3.txt")
    solvePart1("aoc24d12.txt")
}

private fun solvePart1(filename: String) {
    println(filename)
    val input = File(filename).readLines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Plot(x, y, c) }
    }

    val regions = mutableMapOf<Plot, MutableSet<Plot>>()
    for (plot in input.flatten()) {
        val existing = regions.values.filter { area ->
            area.first().c == plot.c && area.any { plot touches it }
        }
        if (existing.isEmpty()) {
            regions[plot] = mutableSetOf(plot)
        }
        else if (existing.size == 1) {
            existing.first().add(plot)
        }
        else {
            // new element merges two regions now:
            regions.values.removeAll(existing.toSet())
            regions[plot] = (existing.flatten() + plot).toMutableSet()
        }
    }

    val areas = regions.values.map { it.size }
    println(regions.keys.map { it.c } zip areas)

    val perimeters = regions.values.map {
        val sides = it.flatMap { it.sides }.groupBy { it }
        val outer = sides.filterValues { it.size == 1 }
        outer.size
    }
    println(perimeters)

    val prices = (areas zip perimeters).map { (a, p) -> a * p }
    println(prices)

    println("part 1: ${prices.sum()}")
}

private infix fun Plot.touches(it: Plot): Boolean {
    if (x == it.x - 1 && y == it.y) return true
    if (x == it.x + 1 && y == it.y) return true
    if (x == it.x && y == it.y - 1) return true
    if (x == it.x && y == it.y + 1) return true
    return false
}
