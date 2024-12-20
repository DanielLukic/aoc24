package aoc

import java.io.File

private val input by lazy { File("data2024/aoc24d20.txt").readText() }

private data class Point(val x: Int, val y: Int) {
    fun neighbors() = listOf(
        Point(x + 1, y), Point(x - 1, y),
        Point(x, y + 1), Point(x, y - 1)
    )
}

private data class Cheat(
    val startPos: Point,
    val endPos: Point,
    val timeSaved: Int
)

private class RaceTrackOptimizer(input: String) {
    private val grid = input.trim().lines().map { it.toMutableList() }
    private val height = grid.size
    private val width = grid[0].size
    private val start: Point
    private val end: Point
    private var normalShortestPath = 0

    init {
        start = findPoint('S')
        end = findPoint('E')
        normalShortestPath = findShortestPath(start, end)
        println("Normal shortest path: $normalShortestPath")
    }

    private fun findPoint(char: Char): Point {
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == char) return Point(x, y)
            }
        }
        throw IllegalArgumentException("Character $char not found in grid")
    }

    private fun findShortestPath(from: Point, to: Point): Int {
        val queue = ArrayDeque<Pair<Point, Int>>()
        val seen = mutableSetOf<Point>()
        queue.add(from to 0)
        seen.add(from)

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.removeFirst()

            for (next in current.neighbors()) {
                if (next == to) return steps + 1
                if (next in seen || !isValid(next)) continue

                queue.add(next to steps + 1)
                seen.add(next)
            }
        }
        return Int.MAX_VALUE
    }

    private fun isValid(point: Point) = point.y in 0..<height &&
        point.x in 0..<width &&
        grid[point.y][point.x] != '#'

    private fun findAllCheats(): Set<Cheat> {
        val cheats = mutableSetOf<Cheat>()

        // Try all possible start positions
        for (y in 0..<height) {
            for (x in 0..<width) {
                val row = grid[y]
                if (row[x] != '#') continue

                row[x] = '.'

                val check = findShortestPath(start, end)
                if (check < normalShortestPath) {
                    cheats.add(Cheat(Point(x, y), Point(x, y), normalShortestPath - check))
                }

                row[x] = '#'
            }
        }
        return cheats
    }

    fun countCheatsAboveThreshold(threshold: Int): Int {
        val cheats = findAllCheats().distinct()
        val grouped = cheats.groupBy { it.timeSaved }.toSortedMap()
        grouped.forEach {
            println("${it.value.size} times ${it.key} picoseconds")
        }
        return cheats.count { it.timeSaved >= threshold }
    }
}

fun main() {
//    val input = """
//        ###############
//        #...#...#.....#
//        #.#.#.#.#.###.#
//        #S#...#.#.#...#
//        #######.#.#.###
//        #######.#.#...#
//        #######.#.###.#
//        ###..E#...#...#
//        ###.#######.###
//        #...###...#...#
//        #.#####.#.###.#
//        #.#...#.#.#...#
//        #.#.#.#.#.#.###
//        #...#...#...###
//        ###############
//    """.trimIndent()

    val optimizer = RaceTrackOptimizer(input)
    println("Number of cheats saving >= 100 picoseconds: ${optimizer.countCheatsAboveThreshold(100)}")
}
