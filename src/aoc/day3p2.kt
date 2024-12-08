package aoc;

import java.io.File

private val mul = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)".toRegex()

fun main() {
    val i1 = File("day3.txt").readText()
    val segments = i1.split("do");
    val on = segments.filterNot { it.startsWith("n't") }
    println(on.joinToString("\n"));
    val input = on.joinToString();
    val muls = mul.findAll(input).map { it.groups }
    val r = muls.map { it[1]!!.value.toInt() * it[2]!!.value.toInt() }
    println(muls.toList())
    println(r.toList())
    println(r.sum());
}
