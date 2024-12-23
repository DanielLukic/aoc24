package aoc

import java.io.File
import kotlin.collections.*

private val connections by lazy { File("data2024/aoc24d23.txt").readLines() }

fun main() {
    part1(connections)
    part2(connections)
}

private fun part1(input: List<String>) {
    val connections = input.map {
        val (a, b) = it.split("-")
        if (a < b) a to b else b to a
    }
    val all = connections.flatMap { f ->
        connections.filter { it.first == f.second }.map { Triple(f.first, it.first, it.second) }
    }
    val ok = all.filter {
        val a = (it.first to it.second) in connections || (it.second to it.first) in connections
        val b = (it.first to it.third) in connections || (it.third to it.first) in connections
        val c = (it.second to it.third) in connections || (it.third to it.second) in connections
        a && b && c
    }.toSet()
    val ts = ok.filter { it.first.startsWith("t") || it.second.startsWith("t") || it.third.startsWith("t") }
    println("part 1: ${ts.size}")
}

private fun part2(connections: List<String>) {
    // Parse connections to build the adjacency list
    val graph = mutableMapOf<String, MutableSet<String>>()
    for (connection in connections) {
        val (a, b) = connection.split("-")
        graph.computeIfAbsent(a) { mutableSetOf() }.add(b)
        graph.computeIfAbsent(b) { mutableSetOf() }.add(a)
    }

    // Function to check if a set of nodes forms a clique
    fun isClique(nodes: Set<String>) = nodes.all { graph[it]?.containsAll(nodes - it) == true }

    // Function to find the largest clique using backtracking
    fun findLargestClique(nodes: List<String>): Set<String> {
        var maxClique = emptySet<String>()

        // Helper function for backtracking
        fun backtrack(currentClique: Set<String>, start: Int) {
            if (currentClique.size > maxClique.size) {
                maxClique = currentClique
            }

            for (i in start..<nodes.size) {
                val newClique = currentClique + nodes[i]
                if (isClique(newClique)) {
                    backtrack(newClique, i + 1)
                }
            }
        }

        backtrack(emptySet(), 0)
        return maxClique
    }

    val allNodes = graph.keys.toList()
    val largestClique = findLargestClique(allNodes)

    // Generate the password: sorted names joined by commas
    val password = largestClique.sorted().joinToString(",")
    println("Part 2: $password")
}
