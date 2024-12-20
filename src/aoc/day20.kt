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
    private val grid = input.trim().lines().map { it.toList() }
    private val height = grid.size
    private val width = grid[0].size
    private val start: Point
    private val end: Point
    private val normalShortestPath: Int
    private val distanceToEnd: Array<IntArray>  // Cache distances to end
    private val distanceFromStart: Array<IntArray>  // Cache distances from start

    var maxCheatDistance = 20  // Maximum cheat duration

    init {
        start = findPoint('S')
        end = findPoint('E')

        // Precompute all distances
        distanceToEnd = Array(height) { IntArray(width) { Int.MAX_VALUE } }
        distanceFromStart = Array(height) { IntArray(width) { Int.MAX_VALUE } }
        computeDistances()

        normalShortestPath = distanceFromStart[end.y][end.x]
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

    private fun isInBounds(point: Point) = point.y in 0..<height && point.x in 0..<width

    private fun isTrack(point: Point) = grid[point.y][point.x] != '#'

    // Precompute all distances using BFS
    private fun computeDistances() {
        // Compute distances from start
        bfs(start, distanceFromStart)

        // Compute distances to end (from end)
        bfs(end, distanceToEnd)
    }

    private fun bfs(source: Point, distances: Array<IntArray>) {
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(source to 0)
        distances[source.y][source.x] = 0

        while (queue.isNotEmpty()) {
            val (pos, dist) = queue.removeFirst()

            for (next in pos.neighbors()) {
                if (!isInBounds(next) || !isTrack(next) ||
                    distances[next.y][next.x] != Int.MAX_VALUE
                ) continue

                distances[next.y][next.x] = dist + 1
                queue.add(next to dist + 1)
            }
        }
    }

    private fun findCheats(): List<Cheat> {
        val cheats = mutableMapOf<Pair<Point, Point>, Int>()

        // For each possible start position that we can reach
        for (y1 in 0..<height) {
            for (x1 in 0..<width) {
                val start = Point(x1, y1)
                if (!isTrack(start)) continue

                val distToCheatStart = distanceFromStart[y1][x1]
                if (distToCheatStart == Int.MAX_VALUE) continue

                // For each possible end position within maxCheatDistance Manhattan distance
                for (y2 in maxOf(0, y1 - maxCheatDistance)..minOf(height - 1, y1 + maxCheatDistance)) {
                    for (x2 in maxOf(0, x1 - maxCheatDistance)..minOf(width - 1, x1 + maxCheatDistance)) {
                        val end = Point(x2, y2)
                        if (!isTrack(end)) continue

                        val distFromCheatEnd = distanceToEnd[y2][x2]
                        if (distFromCheatEnd == Int.MAX_VALUE) continue

                        // Calculate Manhattan distance for the cheat
                        val cheatDist = kotlin.math.abs(x2 - x1) + kotlin.math.abs(y2 - y1)
                        if (cheatDist > maxCheatDistance) continue

                        // Calculate total path length with this cheat
                        val totalDist = distToCheatStart + cheatDist + distFromCheatEnd
                        val timeSaved = normalShortestPath - totalDist

                        if (timeSaved > 0) {
                            val key = start to end
                            cheats[key] = maxOf(cheats.getOrDefault(key, 0), timeSaved)
                        }
                    }
                }
            }
        }

        return cheats.map { (positions, timeSaved) ->
            Cheat(positions.first, positions.second, timeSaved)
        }
    }

    fun countCheatsAboveThreshold(threshold: Int): Int {
        val cheats = findCheats().filter { it.timeSaved >= 50 }
        println("Total cheats found: ${cheats.size}")
        cheats.groupBy { it.timeSaved }.toSortedMap().forEach { (saved, list) ->
            println("$saved picoseconds saved: ${list.size} cheats")
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
    optimizer.maxCheatDistance = 2
    println("Number of cheats saving >= 100 picoseconds: ${optimizer.countCheatsAboveThreshold(100)}")
    optimizer.maxCheatDistance = 20
    println("Number of cheats saving >= 100 picoseconds: ${optimizer.countCheatsAboveThreshold(100)}")
}
