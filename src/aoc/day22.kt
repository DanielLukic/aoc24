package aoc

import java.io.File
import kotlin.collections.*

val data = File("data2024/aoc24d22.txt").readLines().map(String::toLong)

fun main() {
    part1(data)
    part2(data)
}

private fun part1(nums: List<Long>) {
    val answer = nums.sumOf { seed -> generateSequence(seed, ::next).elementAt(2000) }
    println("part 1: $answer")
}

private fun next(n: Long) = next_c(next_b(next_a(n)))
private fun next_c(n: Long) = (n * 2048L xor n) % 16777216L
private fun next_b(n: Long) = (n / 32L xor n) % 16777216L
private fun next_a(n: Long) = (n * 64L xor n) % 16777216L

private fun part2(data: List<Long>) {
    val sum = mutableMapOf<List<Int>, Int>()

    data
        .map { seed ->
            generateSequence(seed, ::next).take(2000)
                .map { (it % 10L).toInt() }
                .windowed(5).map { seq ->
                    seq.windowed(2).map { (a, b) -> b - a }.toList() to seq.last()
                }
        }
        .forEach { monkey ->
            monkey.toList().reversed().associate { it }.forEach { (k, v) ->
                sum[k] = v + (sum[k] ?: 0)
            }
        }

    val max = sum.values.max()
    val seq = sum.filterValues { it == max }.keys.first()
    println("part 2: $max ($seq)")
}
