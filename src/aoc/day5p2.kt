package aoc;

import java.io.File

fun main() {
    val input = File("day5.txt").readLines()
    val separator = input.indexOf("")
    val rules = input.subList(0, separator).map { it.split("|").map(String::toInt) }
    val updates = input.subList(separator + 1, input.size).map { it.split(",").map(String::toInt) }

    fun corrected(update: List<Int>): MutableList<Int>? {
        val result = mutableListOf<Int>()
        result.addAll(update)

        // :-D

        for (r in rules) {
            val f = result.indexOf(r.first())
            if (f == -1) continue
            val l = result.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            println("$r: $result")
            val m = result.removeAt(l)
            result.add(f, m)
            println("=> $result")
        }
        for (r in rules) {
            val f = result.indexOf(r.first())
            if (f == -1) continue
            val l = result.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            println("$r: $result")
            val m = result.removeAt(l)
            result.add(f, m)
            println("=> $result")
        }
        for (r in rules) {
            val f = result.indexOf(r.first())
            if (f == -1) continue
            val l = result.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            println("$r: $result")
            val m = result.removeAt(l)
            result.add(f, m)
            println("=> $result")
        }
        for (r in rules) {
            val f = result.indexOf(r.first())
            if (f == -1) continue
            val l = result.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            println("$r: $result")
            val m = result.removeAt(l)
            result.add(f, m)
            println("=> $result")
        }
        for (r in rules) {
            val f = result.indexOf(r.first())
            if (f == -1) continue
            val l = result.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            println("$r: $result")
            val m = result.removeAt(l)
            result.add(f, m)
            println("=> $result")
        }
        for (r in rules) {
            val f = result.indexOf(r.first())
            if (f == -1) continue
            val l = result.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            println("$r: $result")
            val m = result.removeAt(l)
            result.add(f, m)
            println("=> $result")
        }
        if (result.toString() == update.toString()) return null
        println("$update => $result")
        return result
    }

    val ok = updates.mapNotNull(::corrected)

    val mids = ok.map { it[it.size / 2] }

    println(mids)

    println(mids.sum())
}
