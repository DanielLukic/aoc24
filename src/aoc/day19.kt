package aoc

import java.io.File

fun main() {
    val input = File("data2024/aoc24d19.txt").readLines()
    val towels = input[0].split(",").map(String::trim)
    val designs = input.drop(2)
    println("part 1: " + countPossibleDesigns(towels, designs))
    println("part 2: " + totalArrangements(towels, designs))
}

private fun countPossibleDesigns(towels: List<String>, designs: List<String>): Int {
    val dp = Array(designs.size) { BooleanArray(designs[it].length + 1) }

    // Base case: An empty design can always be formed
    for (i in designs.indices) dp[i][0] = true

    for (i in designs.indices) {
        for (j in 1..designs[i].length) {
            for (pattern in towels) {
                if (j >= pattern.length && designs[i].substring(j - pattern.length, j) == pattern) {
                    dp[i][j] = dp[i][j - pattern.length] || dp[i][j]
                }
            }
        }
    }

    var count = 0
    for (i in designs.indices) if (dp[i][designs[i].length]) count++
    return count
}

private fun totalArrangements(towels: List<String>, designs: List<String>) =
    designs.sumOf { countArrangements(towels, it) }

private fun countArrangements(towels: List<String>, design: String): Long {
    val dp = LongArray(design.length + 1)
    dp[0] = 1 // Empty string can be formed in one way

    for (i in 1..design.length) {
        for (pattern in towels) {
            val patternLength = pattern.length
            if (i >= patternLength && design.substring(i - patternLength, i) == pattern) {
                dp[i] += dp[i - patternLength]
            }
        }
    }

    return dp[design.length]
}
