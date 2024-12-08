package aoc;

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sign

private fun isSafe(report: List<Int>): Boolean {
    val pairs = report.windowed(2)
    val diffs = pairs.map { it.last() - it.first() }
    val sign = diffs.first().sign
    for (i in diffs) {
        if (i == 0) return false
        if (i.absoluteValue > 3) return false
        if (i.sign != sign) return false
    }
    return true
}

private fun isAnySafe(report: List<Int>): Boolean {
    if (isSafe(report)) return true
    val head = report.toMutableList();
    val tail = mutableListOf<Int>()
    while (head.isNotEmpty()) {
        val drop = head.removeLast();
        if (isSafe(head + tail)) return true;
        tail.add(0, drop);
    }
    return false;
}

fun main() {
    val i1 = File("day2.txt").readLines()
    val reports = i1.map { it.split(" ").map(String::toInt) }
    val safe = reports.filter { isAnySafe(it) }
    println(safe.count())
}
