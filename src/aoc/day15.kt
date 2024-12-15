package aoc;

import java.io.File

fun main() {
    solve_part1()
    solve_part2()
}

private fun solve_part1() {
    val warehouse = load_warehouse("day15.txt")
    println(warehouse)
    warehouse.movements.forEach(warehouse::move)
    println(warehouse)
    println(warehouse.gps())
}

private fun solve_part2() {
    val warehouse = load_warehouse("day15.txt").expand()
    warehouse.movements.forEach(warehouse::move)
    println(warehouse)
    println(warehouse.gps())
}

private fun load_warehouse(filename: String): Warehouse {
    val input = File(filename).readLines()
    val split = input.indexOf("").takeUnless { it == -1 } ?: input.size
    val data = input.take(split).map { it.toMutableList() }
    return Warehouse(data, input.drop(split + 1).joinToString(""))
}

private data class Warehouse(
    val data: List<MutableList<Char>>,
    val movements: String,
) {
    private val move = mapOf(
        '<' to Dir(-1, 0),
        '^' to Dir(0, -1),
        '>' to Dir(1, 0),
        'v' to Dir(0, 1),
    )

    private val mapping = mapOf(
        '.' to "..",
        'O' to "[]",
        '@' to "@.",
        '#' to "##",
    )

    override fun toString() = data.joinToString("\n") { it.joinToString("") }

    fun expand(): Warehouse2 {
        val data = data.map { row ->
            row.flatMap { mapping[it]!!.toList() }.toMutableList()
        }
        return Warehouse2(data, movements)
    }

    fun gps() = data.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, char -> if (char == 'O') x + y * 100 else null }
    }.sum()

    fun move(movement: Char) {
        val m = move[movement] ?: error("illegal move: $movement")
        val ry = data.indexOfFirst { it.contains('@') }
        val rx = data[ry].indexOf('@')
        try_push(rx, ry, m, '@')
    }

    fun try_push(x: Int, y: Int, m: Dir, it: Char): Boolean {
        val tx = x + m.dx
        val ty = y + m.dy
        if (is_empty(tx, ty)) {
            data[y][x] = '.'
            data[ty][tx] = it
            return true
        }
        else if (is_box(tx, ty)) {
            if (try_push(tx, ty, m, 'O')) {
                data[y][x] = '.'
                data[ty][tx] = it
                return true
            }
            else {
                return false
            }
        }
        else if (is_wall(tx, ty)) {
            return false
        }
        else {
            error("unknown tile: ${data[ty][tx]}")
        }
    }

    fun is_empty(x: Int, y: Int) = data[y][x] == '.'

    fun is_wall(x: Int, y: Int) = data[y][x] == '#'

    fun is_box(x: Int, y: Int) = data[y][x] == 'O'
}

private data class Warehouse2(
    val data: List<MutableList<Char>>,
    val movements: String,
) {
    private val move = mapOf(
        '<' to Dir(-1, 0),
        '^' to Dir(0, -1),
        '>' to Dir(1, 0),
        'v' to Dir(0, 1),
    )

    override fun toString() = data.joinToString("\n") { it.joinToString("") }

    fun gps() = data.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, char -> if (char == '[') x + y * 100 else null }
    }.sum()

    fun move(movement: Char) {
        snapshot()

        val m = move[movement] ?: error("illegal move: $movement")
        val ry = data.indexOfFirst { it.contains('@') }
        val rx = data[ry].indexOf('@')
        val done = robot_push(rx, ry, m)
        if (!done) restore()
    }

    private var _snapshot: List<List<Char>>? = null

    fun snapshot() {
        _snapshot = data.map { it.toList() }
    }

    fun restore() {
        require(_snapshot != null)
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, _ ->
                data[y][x] = _snapshot!![y][x]
            }
        }
        _snapshot = null
    }

    fun robot_push(x: Int, y: Int, m: Dir): Boolean {
        val tx = x + m.dx
        val ty = y + m.dy
        if (is_empty(tx, ty)) {
            data[y][x] = '.'
            data[ty][tx] = '@'
            return true
        }
        else if (is_box(tx, ty)) {
            if (box_push(tx, ty, m)) {
                data[y][x] = '.'
                data[ty][tx] = '@'
                return true
            }
            else {
                return false
            }
        }
        else if (is_wall(tx, ty)) {
            return false
        }
        else {
            error("unknown tile: ${data[ty][tx]}")
        }
    }

    fun box_push(x: Int, y: Int, m: Dir): Boolean {
        if (is_box_right(x, y)) return box_push(x - 1, y, m)

        require(is_box_left(x, y))
        require(is_box_right(x + 1, y))

        data[y][x] = '.'
        data[y][x + 1] = '.'

        val tx = x + m.dx
        val ty = y + m.dy

        if (is_empty(tx, ty) && is_empty(tx + 1, ty)) {
            data[ty][tx] = '['
            data[ty][tx + 1] = ']'
            return true
        }
        else if (is_wall(tx, ty) || is_wall(tx + 1, ty)) {
            data[y][x] = '['
            data[y][x + 1] = ']'
            return false
        }
        else {
            if (is_box(tx, ty)) {
                if (!box_push(tx, ty, m)) {
                    data[y][x] = '['
                    data[y][x + 1] = ']'
                    return false
                }
            }
            if (is_box(tx + 1, ty)) {
                if (!box_push(tx + 1, ty, m)) {
                    data[y][x] = '['
                    data[y][x + 1] = ']'
                    return false
                }
            }
            data[ty][tx] = '['
            data[ty][tx + 1] = ']'
            return true
        }
    }

    fun is_empty(x: Int, y: Int) = data[y][x] == '.'

    fun is_wall(x: Int, y: Int) = data[y][x] == '#'

    fun is_box(x: Int, y: Int) = data[y][x] == '[' || data[y][x] == ']'
    fun is_box_left(x: Int, y: Int) = data[y][x] == '['
    fun is_box_right(x: Int, y: Int) = data[y][x] == ']'
}
