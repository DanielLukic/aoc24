package aoc;

import java.io.File

fun main() {

    val i1 = File("day1.txt").readLines();
    println(i1.size);

    val pairs = i1.map { it.split(" +".toRegex()) }
    val a = pairs.map { it[0].toInt() };
    println(a);
    val b = pairs.map { it[1].toInt() };
    val h = b.groupBy { it }.map { it.key to it.value.size }.toMap();
    println(h);
    val d = a.map { it * (h[it] ?: 0) }
    println(d);
    val s = d.sum();
    println(s);
}
