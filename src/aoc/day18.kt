package aoc

import java.io.File
import java.util.*

fun shortestPath(grid: Array<CharArray>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    val queue: Queue<Pair<Pair<Int, Int>, Int>> = LinkedList()
    val visited = mutableSetOf<Pair<Int, Int>>()
    queue.offer(start to 0)
    visited.add(start)

    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    while (queue.isNotEmpty()) {
        val (current, distance) = queue.poll()
        val (x, y) = current

        if (current == end) {
            return distance
        }

        for ((dx, dy) in directions) {
            val newX = x + dx
            val newY = y + dy
            if (newY in grid.indices && newX in grid[0].indices && grid[newY][newX] != '#' && (newX to newY) !in visited) {
                visited.add(newX to newY)
                queue.offer((newX to newY) to (distance + 1))
            }
        }
    }

    return -1
}

fun main() {
    val grid_size = 70
    val bytes = File("aoc24d18.txt")
        .readLines()
        .map { it.split(",").map(String::toInt) }

    val found = part1(grid_size, bytes.take(1024))
    println("part 1: $found")

    var check = 1024
    while (++check < bytes.size) {
        val result = part1(grid_size, bytes.take(check))
        println("check $check: $result")
        if (result == -1) {
            println("part 2: ${bytes[check - 1].joinToString(",")}")
            break
        }
    }
}

private fun part1(grid_size: Int, bytes: List<List<Int>>): Int {
    // Assuming you have the input list in a variable called 'bytePositions'
    val grid = Array(grid_size + 1) { CharArray(grid_size + 1) { '.' } }
    for ((x, y) in bytes) grid[y][x] = '#'
    val start = Pair(0, 0)
    val end = Pair(grid_size, grid_size)
    return shortestPath(grid, start, end)
}
