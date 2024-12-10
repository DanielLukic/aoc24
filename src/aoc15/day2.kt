package aoc15;

import java.io.File

fun main() {

    val data = File("aoc15d2.txt").readLines().map { it.split('x').map(String::toInt) }
    println(data);

    val paper = data.map {
        val (l, w, h) = it
        val wrap = 2 * l * w + 2 * w * h + 2 * h * l
        val (a, b) = it.sorted()
        val extra = a * b
        wrap + extra
    }

    println(paper.sum())

    val ribbon = data.map {
        val (a, b) = it.sorted()
        val base = a + a + b + b
        val (l, w, h) = it
        val bow = l * w * h
        base + bow
    }

    println(ribbon.sum())
}
