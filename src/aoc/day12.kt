package aoc;

import java.io.File
import kotlin.math.sign

private data class Plot(val x: Int, val y: Int, val c: Char) {
    val sides = setOf("${x - 1}X$x|$y", "${x}X${x + 1}|$y", "${y - 1}Y$y|$x", "${y}Y${y + 1}|$x")
}

fun main() {
    solvePart1("aoc24d12t1.txt")
    solvePart1("aoc24d12t2.txt")
    solvePart1("aoc24d12t3.txt")
    solvePart1("aoc24d12.txt")

    solvePart2("aoc24d12t1.txt")
    solvePart2("aoc24d12t2.txt")
    solvePart2("aoc24d12t4.txt")
    solvePart2("aoc24d12.txt")
}

private fun solvePart1(filename: String) {
    val regions = makeRegions(filename)

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

private fun solvePart2(filename: String) {
    val regions = makeRegions(filename)
    val areas = regions.values.map { it.size }
    println(regions.keys.map { it.c } zip areas)

    val sides = regions.values.map { plots ->
        val edges = mutableSetOf<Pair<Pos, Pos>>()
        for (plot in plots) {
            if (plots.none { plot.x == it.x && plot.y - 1 == it.y }) {
                edges.add(Pos(plot.x, plot.y) to Pos(plot.x + 1, plot.y))
            }
            if (plots.none { plot.x == it.x && plot.y + 1 == it.y }) {
                edges.add(Pos(plot.x + 1, plot.y + 1) to Pos(plot.x, plot.y + 1))
            }
            if (plots.none { plot.x - 1 == it.x && plot.y == it.y }) {
                edges.add(Pos(plot.x, plot.y + 1) to Pos(plot.x, plot.y))
            }
            if (plots.none { plot.x + 1 == it.x && plot.y == it.y }) {
                edges.add(Pos(plot.x + 1, plot.y) to Pos(plot.x + 1, plot.y + 1))
            }
        }
//        println("edges: $edges")

        var sides = 0
        while (edges.isNotEmpty()) {
            val picked = mutableListOf<Pair<Pos, Pos>>()
            picked.add(edges.first())
            edges.remove(picked.single())
//            println("start picking with $picked and $edges")

            fun dir(edge: Pair<Pos, Pos>) =
                Dir((edge.second.x - edge.first.x).sign, (edge.second.y - edge.first.y).sign)

            while (true) {
                val anchor = picked.last()
                val anchor_dir = dir(anchor)
                val next = edges.filter { dir(it) == anchor_dir }
                    .filter { it.first == anchor.second || it.second == anchor.first }

                if (next.isEmpty()) {
//                    println("done picking: $picked remaining: $edges")
                    break
                }
                if (next.size > 1) {
                    println("edges: $edges")
                    println("picked: $picked")
                    println("next: $next")
                    error("no no")
                }
                picked.add(next.single())
                edges.remove(next.single())
            }

//            println(picked)

            sides++
        }

//        println("sides: $sides")

        sides
    }

    println("sides: $sides")

    val prices = (areas zip sides).map { (a, p) -> a * p }
    println(prices)

    println("part 1: ${prices.sum()}")
}

private fun makeRegions(filename: String): MutableMap<Plot, MutableSet<Plot>> {
    println(filename)

    val input = File(filename).readLines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Plot(x, y, c) }
    }

    val regions = mutableMapOf<Plot, MutableSet<Plot>>()
    for (plot in input.flatten()) {
        val existing = regions.values.filter { area ->
            area.first().c == plot.c && area.any { plot touches it }
        }

        // with every new plot, we need to potentially merge two areas that are now combined. hence this construct:

        regions.values.removeAll(existing.toSet())
        regions[plot] = (existing.flatten() + plot).toMutableSet()
    }
    return regions
}

private infix fun Plot.touches(it: Plot): Boolean {
    if (x == it.x - 1 && y == it.y) return true
    if (x == it.x + 1 && y == it.y) return true
    if (x == it.x && y == it.y - 1) return true
    if (x == it.x && y == it.y + 1) return true
    return false
}
