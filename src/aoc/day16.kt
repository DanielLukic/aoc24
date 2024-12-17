package aoc;

import java.io.File

private val dirs = listOf(Dir(1, 0), Dir(0, -1), Dir(-1, 0), Dir(0, 1))

private val _cost = mutableMapOf<Pair<Pos, Int>, Int>()
private fun cost(pos: Pos, dir: Int) = _cost[pos to dir] ?: Int.MAX_VALUE
private fun cost(path: Path) = _cost[path.pos to path.dir] ?: Int.MAX_VALUE

private lateinit var maze: Maze

fun main() {
    maze = load_maze("aoc24d16.txt")

    println(maze)
    println(maze.start)
    println(maze.end)

    val path = solve_part1()

    val seats = solve_part2(path)

    maze.insert(path)
    println(maze)
    println(path)

    maze.insert(tiles)
    println(maze)
    println(seats)
}

private val tiles = mutableSetOf<Pos>()

private fun backtrack(pos: Pos, dir: Int) {
    tiles.add(pos)

    if (pos == maze.start) return

    for (i in listOf(-1, 1)) {
        val n_dir = (dir + i + 4) % 4
        if (cost(pos, dir) - 1000 == cost(pos, n_dir)) {
            backtrack(pos, n_dir)
        }
    }

    val n_pos = pos + dirs[(dir + 2) % 4]
    if (cost(pos, dir) - 1 == cost(n_pos, dir)) {
        backtrack(n_pos, dir)
    }
}

private fun solve_part2(end: Path): Int {
    backtrack(end.pos, end.dir)
    return tiles.size
}

private fun solve_part1(): Path {

    val heap = mutableListOf<Path>()
    heap.add(Path(maze.start, dir = 0, cost = 0))

    val found = mutableSetOf<Path>()

    val turns = listOf(-1, 1)

    do {
        val path = heap.removeFirst()
        if (path.pos == maze.end) {
            found.add(path)
            continue
        }
        if (path.cost > cost(path)) {
            continue
        }

        val n_pos = path.pos + dirs[path.dir]
        if (n_pos !in maze.walls && path.cost + 1 < cost(n_pos, path.dir)) {
            val n_path = Path(n_pos, path.dir, path.cost + 1)
            n_path.parent = path
            _cost[n_pos to path.dir] = n_path.cost
            heap.add(n_path)
        }

        for (turn in turns) {
            val test_dir = (path.dir + turn + 4) % 4
            val new = Path(path.pos, test_dir, path.cost + 1000)
            if (new.cost >= cost(new)) continue
            _cost[new.pos to new.dir] = new.cost
            new.parent = path
            heap.add(new)
        }
    }
    while (heap.isNotEmpty())

    println(found)

    return found.minBy { it.cost }
}

private operator fun Pos.plus(dir: Dir) = Pos(x + dir.dx, y + dir.dy)

private data class Path(val pos: Pos, val dir: Int, val cost: Int) {
    var parent: Path? = null
    val trail: Sequence<Path> get() = generateSequence(parent) { it.parent }
}

private fun load_maze(filename: String): Maze {
    val input = File(filename).readLines()
    return Maze(input.map { it.toMutableList() })
}

private class Maze(val grid: List<MutableList<Char>>) {
    val start = _locate('S')
    val end = _locate('E')

    private fun _locate(element: Char): Pos {
        val y = grid.withIndex().first { it.value.contains(element) }.index
        val x = grid[y].indexOf(element)
        return Pos(x, y)
    }

    val walls = grid.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, c -> if (c == '#') Pos(x, y) else null }
    }.toSet()

    override fun toString() = grid.joinToString("\n") { it.joinToString("") }

    fun insert(path: Path) {
        var p: Path? = path
        while (p != null) {
            val row = grid[p.pos.y]
            row[p.pos.x] = dump_dir[p.dir] ?: error("invalid dir: ${p.dir}")
            p = p.parent
        }
    }

    fun insert(tiles: Collection<Pos>) {
        for (tile in tiles) {
            val row = grid[tile.y]
            row[tile.x] = 'O'
        }
    }
}

private val dump_dir = mapOf(0 to '>', 1 to '^', 2 to '<', 3 to 'v')
