package aoc;

import java.io.File

private data class Entry(var pos: Int, val id: Int, var size: Int)

fun main() {
    val input = File("day9.txt").readLines().single()
    println(input)
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
        val space = free.firstOrNull { it.size >= f.size }
        if (space == null) continue
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

    println(blocks.joinToString("") { if (it == -1) "." else it.toString() })

    var checksum = (0L).toULong()
    for (p in blocks.indices) {
        if (blocks[p] == -1) continue
        println("add pos $p times id ${blocks[p]}")
        checksum += blocks[p].toULong() * p.toULong()
    }
    println(checksum)
}
