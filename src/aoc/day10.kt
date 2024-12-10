package aoc;

import java.io.File

data class Trail(val x: Int, val y: Int, val height: Int, val trail: MutableList<Trail> = mutableListOf()) {
    override fun toString(): String {
        if (trail.isEmpty()) return height.toString()
        return "$height$trail"
    }
}

fun main() {
    val input = File("day10.txt").readText()

    val width = input.indexOf('\n')
    val height = input.count { it == '\n' }
    println(height)
    println(width)
    println(input)

    val heads = mutableListOf<Trail>()

    val grid = mutableListOf<List<Trail>>()
    for (y in 0..<height) {
        val row = mutableListOf<Trail>()
        grid.add(row)
        for (x in 0..<width) {
            val h = input[x + y * (width + 1)].digitToInt()
            val it = Trail(x, y, h)
            row.add(it)
            if (h == 0) heads.add(it)
        }
    }

    fun h_at(x: Int, y: Int): Trail? {
        if (x !in 0..<width) return null
        if (y !in 0..<height) return null
        return grid[y][x]
    }

    val dirs = listOf(Dir(0, -1), Dir(1, 0), Dir(0, 1), Dir(-1, 0))
    for (row in grid) {
        for (t in row) {
            val h = t.height
            for (d in dirs) {
                val th = h_at(t.x + d.dx, t.y + d.dy)
                if (th?.height == h + 1) t.trail.add(th)
            }
        }
    }

    fun find_trails(it: Trail): List<Trail> {
        if (it.height == 9) return listOf(it)
        return it.trail.flatMap { find_trails(it) }
    }

    val trails = heads.map { find_trails(it) }
    println(trails.map { it.distinct() }.sumOf { it.size })
    println(trails.sumOf { it.size })
}
