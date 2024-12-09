package aoc;

import java.io.File

fun main() {
    val input = File("day8.txt").readLines()
    val width = input[0].length
    val height = input.size

    val antinodes = mutableSetOf<Pos>()

    fun find_antinodes(x1: Int, y1: Int) {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (x == x1 && y == y1) continue
                if (input[y][x] == '.') continue
                if (input[y][x] != input[y1][x1]) continue
                println("pair at $x $y")
                val dx = x - x1
                val dy = y - y1
                antinodes.add(Pos(x1 - dx, y1 - dy))
                antinodes.add(Pos(x + dx, y + dy))
            }
        }
    }

    for (y in 0..<height) {
        for (x in 0..<width) {
            if (input[y][x] == '.') continue
            println("antenna at $x $y")
            find_antinodes(x, y)
        }
    }

    val result = antinodes.filter { it.x in 0..<width && it.y in 0..<height }

    for (y in 0..<height) {
        for (x in 0..<width) {
            if (result.any { it.x == x && it.y == y }) {
                print("#")
            } else {
                print(input[y][x])
            }
        }
        println()
    }

    println(result)
    println(result.size)
}
