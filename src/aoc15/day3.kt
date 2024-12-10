package aoc15;

import aoc.Dir
import aoc.Pos
import java.io.File

private val dirs = mapOf('^' to Dir(0, -1), '>' to Dir(1, 0), 'v' to Dir(0, 1), '<' to Dir(-1, 0))

fun main() {

    val data = File("aoc15d3.txt").readLines().single()
    println(data);

    val visited = visit(data)
    println(visited.distinct().size)

    val santa = data.filterIndexed { index, _ -> (index and 1) == 0 }
    val robo = data.filterIndexed { index, _ -> (index and 1) == 1 }
    val all = visit(santa) + visit(robo)
    println(all.distinct().size)
}

private fun visit(data: String): MutableList<Pos> {
    val visited = mutableListOf<Pos>()

    var pos = Pos(0, 0)
    visited.add(pos)

    for (d in data) {
        val dir = dirs[d]!!
        pos = Pos(pos.x + dir.dx, pos.y + dir.dy)
        visited.add(pos)
    }
    return visited
}
