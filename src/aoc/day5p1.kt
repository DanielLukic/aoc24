package aoc;

import java.io.File

fun main() {
    val input = File("day5.txt").readLines()
    val separator = input.indexOf("")
    val rules = input.subList(0, separator).map { it.split("|") }
    val updates = input.subList(separator + 1, input.size)
    println(rules)
    println(updates)

    fun isCorrect(update: String): Boolean {
        for (r in rules) {
            val f = update.indexOf(r.first())
            if (f == -1) continue
            val l = update.indexOf(r.last())
            if (l == -1) continue
            if (f < l) continue
            return false
        }
        return true
    }

    val ok = updates.filter { isCorrect(it) }

    val mids = ok.map {
        val pages = it.split(",")
        pages[pages.size / 2].toInt()
    }

    println(mids)

    println(mids.sum())
}
