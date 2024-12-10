package aoc15;

import aoc.Pos
import java.io.File
import kotlin.math.max

fun main() {

    val data = File("aoc15d6.txt").readLines().map {
        val (op, bl, tr) = it.replace("turn ", "turn_").replace("through ", "").split(" ")
        val (b, l) = bl.split(',').map(String::toInt)
        val (t, r) = tr.split(',').map(String::toInt)
        if (b > t) error("no $it")
        if (l > r) error("no $it")
        if (b == t || l == r) println("same $it")
        val op_op: (Int) -> Int = when (op) {
            "turn_on"  -> { s -> s + 1 }
            "turn_off" -> { s -> max(0, s - 1) }
            "toggle"   -> { s -> s + 2 }
            else       -> error("no op: $op")
        }
        Triple(op_op, Pos(b, l), Pos(t, r))
    }

    val grid = (0..<1000).map { _ ->
        (0..<1000).map { _ -> 0 }.toMutableList()
    }

    for ((op, bl, tr) in data) {
        for (y in bl.y..tr.y) {
            val row = grid[y]
            for (x in bl.x..tr.x) {
                (row[x]) = op(grid[y][x])
            }
        }
    }

    println(grid.sumOf { row -> row.sum() })
}
