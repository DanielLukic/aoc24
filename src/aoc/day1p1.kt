package aoc;

import java.io.File
import kotlin.math.abs

fun main() {

    val i1 = File("day1.txt").readLines();
    println(i1.size);

    val pairs = i1.map { it.split(" +".toRegex()) }
    val a = pairs.map { it[0].toInt() }.sorted();
    val b = pairs.map { it[1].toInt() }.sorted();
    val d = a.zip(b).map { abs(it.first - it.second) }
    val s = d.sum();
    println(s);
}
