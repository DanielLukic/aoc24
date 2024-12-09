package aoc;

import java.io.File
import java.math.BigInteger

private data class Entry(var pos: Int, val id: Int, var size: Int)

fun main() {
    val input = File("day9.txt").readLines().single()
    var id = 0
    var file = true

    val used = mutableListOf<Entry>()
    val free = mutableListOf<Entry>()

    var pos = 0
    for (i in input.indices) {
        val len = input[i].digitToInt()
        if (file) {
            used.add(Entry(pos, id, len))
        }
        else {
            free.add(Entry(pos, -1, len))
        }
        pos += len
        if (file) id++
        file = !file
    }

    val freed = mutableListOf<Entry>()
    for (f in used.reversed()) {
        println("fit ${f.size}")
        val space = free.firstOrNull { it.size >= f.size }
        if (space == null) continue
        if (space.pos > f.pos) continue
        freed.add(Entry(f.pos, -1, f.size))
        f.pos = space.pos
        space.size -= f.size
        space.pos += f.size
    }

    val combined = (used + free.filter { it.size > 0 } + freed).sortedBy { it.pos }

    val blocks = mutableListOf<Int>()
    for (e in combined) {
        repeat(e.size) { blocks.add(e.id) }
    }

    println(blocks.joinToString("") { if (it == -1) "." else "x" })

    var checksum = BigInteger.valueOf(0)
    for (p in blocks.indices) {
        if (blocks[p] == -1) continue
        checksum += BigInteger.valueOf(blocks[p].toLong() * p.toLong())
    }
    println(checksum)
}
