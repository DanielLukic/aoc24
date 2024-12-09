package aoc;

import java.io.File

fun main() {
    val input = File("day9.txt").readLines().single()
    println(input)
    var id = 0
    var file = true
    val blocks = mutableListOf<Int>()
    for (i in input) {
        val len = i.digitToInt()
        val char = if (file) id else -1
        repeat(len) { blocks.add(char) }
        if (file) id++
        file = !file
    }
    println(blocks.map { if (it == -1) '.' else it }.joinToString(""))

    var fill_at = 0
    while (fill_at < blocks.size) {
        if (blocks[fill_at] == -1) {
            val l = blocks.indexOfLast { it != -1 }
            if (l > fill_at) {
                blocks[fill_at] = blocks[l]
                blocks[l] = -1
            }
        }
        fill_at++
    }
    println(blocks.map { if (it == -1) '.' else it }.joinToString(""))

    var checksum = (0L).toULong()
    for (p in blocks.indices) {
        if (blocks[p] == -1) break
        checksum += blocks[p].toULong() * p.toULong()
    }
    println(checksum)
}
