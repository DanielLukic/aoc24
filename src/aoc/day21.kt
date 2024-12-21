// converted from https://github.com/surgi1/adventofcode/blob/main/2024/day21/script.js
// found via aoc reddit

package aoc

import java.io.File

val DIRS = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
val DIRS2 = listOf('^', '>', 'v', '<')
val numpad = listOf(
    listOf('#', '#', '#', '#', '#'),
    listOf('#', '7', '8', '9', '#'),
    listOf('#', '4', '5', '6', '#'),
    listOf('#', '1', '2', '3', '#'),
    listOf('#', '#', '0', 'A', '#'),
    listOf('#', '#', '#', '#', '#')
)
val dirpad = listOf(
    listOf('#', '#', '#', '#', '#'),
    listOf('#', '#', '^', 'A', '#'),
    listOf('#', '<', 'v', '>', '#'),
    listOf('#', '#', '#', '#', '#')
)

data class PathState(
    val p: List<Int>,
    val path: MutableList<Char> = mutableListOf(),
    val dirId: Int? = null,
    val cost: Int = 0
)

fun findAllMinPaths(map: List<List<Char>>, startVal: Char, endVal: Char): List<String> {
    var start = listOf(0, 0)
    map.forEachIndexed { y, row ->
        row.forEachIndexed { x, v ->
            if (v == startVal) start = listOf(x, y)
        }
    }

    val stack = mutableListOf(PathState(start))
    val seen = mutableMapOf<String, Int>()
    val paths = mutableListOf<List<Char>>()
    var minCost = Int.MAX_VALUE

    if (map[start[1]][start[0]] == endVal) return listOf("A")

    while (stack.isNotEmpty()) {
        val cur = stack.removeFirst()
        if (cur.dirId != null) cur.path.add(DIRS2[cur.dirId])

        if (map[cur.p[1]][cur.p[0]] == endVal) {
            if (cur.cost < minCost) {
                paths.clear()
                minCost = cur.cost
            }
            if (cur.cost == minCost) paths.add(cur.path.toList())
            continue
        }

        val k = cur.p.joinToString("_")
        if (seen.getOrDefault(k, Int.MAX_VALUE) < cur.cost) continue
        seen[k] = cur.cost
        if (cur.cost > minCost) continue

        DIRS.forEachIndexed { dirId, d ->
            val p = listOf(cur.p[0] + d.first, cur.p[1] + d.second)
            if (map[p[1]][p[0]] == '#') return@forEachIndexed
            stack.add(
                PathState(
                    p = p,
                    path = cur.path.toMutableList(),
                    dirId = dirId,
                    cost = cur.cost + 1
                )
            )
        }
    }

    return paths.map { it.joinToString("") + "A" }
}

fun execute(map: List<List<Char>>, code: String, depth: Int, memo: MutableMap<String, Long> = mutableMapOf()): Long {
    val k = "${code}_$depth"
    memo[k]?.let { return it }

    var curPos = 'A'
    var length = 0L

    code.forEach { nextPos ->
        val paths = findAllMinPaths(map, curPos, nextPos)
        length += (if (depth == 0) {
            paths[0].length
        }
        else {
            paths.minOfOrNull { path -> execute(dirpad, path, depth - 1, memo) } ?: 0
        }).toLong()
        curPos = nextPos
    }

    memo[k] = length
    return length
}

fun main() {
    val inputs = File("data2024/aoc24d21.txt").readLines()
    println(inputs)

    val part1 = inputs.sumOf {
        val size = execute(numpad, it, 2)
        println(size)
        val code = it.filter(Char::isDigit).toInt()
        size * code
    }
    println("part 1: $part1")

    val part2 = inputs.sumOf {
        val size = execute(numpad, it, 25)
        println(size)
        val code = it.filter(Char::isDigit).toInt()
        size * code
    }
    println("part 2: $part2")
}
