package aoc;

import java.io.File

data class Dir(val dx: Int, val dy: Int)
data class Pos(val x: Int, val y: Int)

private val dirs = listOf(Dir(0, -1), Dir(1, 0), Dir(0, 1), Dir(-1, 0))

fun main() {
    val input = File("day6.txt").readText()
    val width = input.indexOf('\n') + 1
    val height = input.count { it == '\n' } + 1
    val start = input.indexOf('^')

    val x = start % width
    val y = start / width
    var pos = Pos(x, y)
    var dir = 0
    val route = mutableListOf(pos)

    println("width=$width height=$height")
    println("x=$x y=$y")

    operator fun Pos.plus(dir: Dir): Pos = Pos(this.x + dir.dx, this.y + dir.dy)
    fun Pos.isOutside(): Boolean = this.x !in 0..<(width - 1) || this.y !in 0..<(height - 1)
    fun Pos.isBlocked(): Boolean {
        val index = this.x + this.y * width
        return input[index] == '#'
    }

    while (true) {
        val step = pos + dirs[dir]
        if (step.isOutside()) {
            println("outside at $pos with next step $step and dir ${dirs[dir]}")
            break
        }
        else if (step.isBlocked()) {
            val was = dirs[dir]
            dir = (dir + 1) % dirs.size
            println("change dir at $pos: $was => ${dirs[dir]} => $dir")
        }
        else {
            route.add(step)
            println(step)
            pos = step
        }
    }

    println(route.distinct().size)

    for (y in 0..<(height - 1)) {
        for (x in 0..<(width - 1)) {
            val grid = input[x + y * width]
            val c = if (Pos(x, y) in route) 'X' else grid
            if (c == 'X' && grid == '#') error("no no")
            print(c)
        }
        println()
    }
}
