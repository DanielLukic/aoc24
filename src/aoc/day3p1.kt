package aoc;

import java.io.File

private val mul = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)".toRegex()

fun main() {
    val i1 = File("day3.txt").readText()
    println(i1)
    val muls = mul.findAll(i1).map { it.groups }
    val r = muls.map { it[1]!!.value.toInt() * it[2]!!.value.toInt() }
    println(muls.toList())
    println(r.toList())
    println(r.sum());
}
