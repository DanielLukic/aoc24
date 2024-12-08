package aoc;

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sign

private fun isSafe(report: List<Int>): Boolean {
    val pairs = report.windowed(2);
    val diffs = pairs.map { it.last() - it.first() }
    val sign = diffs.first().sign;
    for (i in diffs) {
        if (i == 0) return false;
        if (i.absoluteValue > 3) return false;
        if (i.sign != sign) return false;
    }
    return true;
}

fun main() {
    val i1 = File("day2.txt").readLines();
    val reports = i1.map { it.split(" ").map(String::toInt) }
    val safe = reports.filter { isSafe(it) };
    println(safe.count());
}
