package aoc;

import java.io.File

private data class PosDir(val x: Int, val y: Int, val dir: Dir)

private operator fun PosDir.plus(dir: Dir): PosDir = PosDir(this.x + dir.dx, this.y + dir.dy, dir)
private fun PosDir.isOutside(width: Int, height: Int): Boolean = x !in 0..<(width - 1) || y !in 0..<(height - 1)
private fun PosDir.isBlocked(input: String, width: Int): Boolean = input[x + y * width] == '#'
private fun PosDir.isLoop(route: List<PosDir>): Boolean = this in route

private val dirs = listOf(Dir(0, -1), Dir(1, 0), Dir(0, 1), Dir(-1, 0))

private fun walk(input: String, width: Int, height: Int, start: PosDir): List<PosDir>? {
    var dir = dirs.indexOf(start.dir)
    var pos = start

    val route = mutableListOf(pos)
    while (true) {
        val step = pos + dirs[dir]
        if (step.isLoop(route)) {
//            println("loop at $pos with next step $step")
            return null
        }
        else if (step.isOutside(width, height)) {
//            println("outside at $pos with next step $step and dir ${dirs[dir]}")
            break
        }
        else if (step.isBlocked(input, width)) {
            val was = dirs[dir]
            dir = (dir + 1) % dirs.size
//            println("change dir at $pos: $was => ${dirs[dir]} => $dir")
        }
        else {
            route.add(step)
//            println(step)
            pos = step
        }
    }

    return route
}

fun main() {
    val input = File("day6.txt").readText()
    val width = input.indexOf('\n') + 1
    val height = input.count { it == '\n' } + 1
    val start = input.indexOf('^')

    val x = start % width
    val y = start / width

    println("width=$width height=$height")
    println("x=$x y=$y")

    val start_pos = PosDir(x, y, dirs[0])
    val route = walk(input, width, height, start_pos)!!

    println(route)
    println(route.map { it.x to it.y }.distinct().size)

    val loopers = mutableSetOf<Pair<Int, Int>>()
    for (candidate in route.indices) {
        val obstacle = route[candidate]
        if (obstacle.x == route[0].x && obstacle.y == route[0].y) continue // next to guard start
        if (obstacle.x == route[1].x && obstacle.y == route[1].y) continue // next to guard start
        if ((obstacle.x to obstacle.y) in loopers) continue
        val grid = input.toCharArray()
        grid[obstacle.x + obstacle.y * width] = '#'
        val mod = String(grid)
        val check = walk(mod, width, height, start_pos)
        if (check == null) loopers.add(obstacle.x to obstacle.y)
    }

    for (y in 0..<(height - 1)) {
        for (x in 0..<(width - 1)) {
            val grid = input[x + y * width]
            val c = when {
                loopers.any { it.first == x && it.second == y } -> 'O'
//                route.any { it.x == x && it.y == y }   -> 'x'
                else                                            -> grid
            }
            print(c)
        }
        println()
    }

    println("loops: ${loopers.size}")
}
