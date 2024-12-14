package aoc;

import java.io.File

fun main() {
//    println("part 1: ${solve("day14-test.txt", 11, 7)}")
    println("part 1: ${solve1("day14.txt", 101, 103)}")
    println("part 2: ${solve2("day14.txt", 101, 103)}")
}

private data class Robot(var x: Int, var y: Int, var dir: Dir)

private fun solve1(filename: String, width: Int, height: Int): Int {
    val input = load_robots(filename)
    val result = run_robots(input, 100, width, height)

    val mid_x = width / 2
    val mid_y = height / 2
    val q1 = result.filter { it.x < mid_x && it.y < mid_y }.size
    val q2 = result.filter { it.x > mid_x && it.y < mid_y }.size
    val q3 = result.filter { it.x < mid_x && it.y > mid_y }.size
    val q4 = result.filter { it.x > mid_x && it.y > mid_y }.size
    println("q1: $q1, q2: $q2, q3: $q3, q4: $q4")
    return q1 * q2 * q3 * q4
}

private fun solve2(filename: String, width: Int, height: Int): Int {

    val input = load_robots(filename)

    val mid_x = width / 2
    val mid_y = height / 2

    var seconds = 0
    val grid = Array(height) { Array(width) { 0 } }

    while (true) {
        seconds++

        val robots = run_robots(input, seconds, width, height)

        grid.forEach { it.fill(0) }
        for (robot in robots) {
            grid[robot.y][robot.x] = 1
        }
        if (grid.any { it.joinToString("").contains("111111111") }) break;
    }

    grid.forEach { it.fill(0) }
    for (robot in input) {
        grid[robot.y][robot.x]++
    }
    grid.joinToString("\n") { it.joinToString("") }.also { println(it) }

    println("seconds: $seconds")

    return seconds
}

private fun load_robots(filename: String): List<Robot> = File(filename).readLines().map {
    val (p, v) = it.split(" ")
    val (x, y) = p.substringAfter("=").split(',')
    val (dx, dy) = v.substringAfter("=").split(',')
    Robot(x.toInt(), y.toInt(), Dir(dx.toInt(), dy.toInt()))
}

private fun run_robots(input: List<Robot>, seconds: Int, width: Int, height: Int) = input.map {
    val x = (it.x + it.dir.dx * seconds) % width
    val y = (it.y + it.dir.dy * seconds) % height
    Pos(if (x >= 0) x else width + x, if (y >= 0) y else height + y)
}
