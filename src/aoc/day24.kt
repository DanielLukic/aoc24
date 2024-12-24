package aoc

import java.io.File
import kotlin.collections.*

private val input by lazy { File("data2024/aoc24d24.txt").readLines() }

fun main() {
    val (ops, registers) = evaluate(input)
    part1(registers)
    part2(ops)
}

private data class Op(val op: String, val a: String, val b: String, val out: String)

private fun evaluate(input: List<String>): Pair<List<Op>, MutableMap<String, Int>> {
    val ops = input.filter { it.contains(" -> ") }.map {
        val (op, out) = it.split(" -> ")
        val parts = op.split(" ")
        Op(parts[1], parts[0], parts[2], out)
    }

    val registers = buildMap {
        input.filter { it.contains(": ") }.map { it.split(": ") }.forEach { (k, v) -> put(k, v.toInt()) }
    }.toMutableMap()

    fun evaluate(out: String): Int = registers.getOrPut(out) {
        val op = ops.firstOrNull { it.out == out }
        if (op == null) return registers[out] ?: error("Unknown register: $out")

        val a = evaluate(op.a)
        val b = evaluate(op.b)
        when (op.op) {
            "AND" -> a and b
            "OR"  -> a or b
            "XOR" -> a xor b
            else  -> error("Unknown op: ${op.op}")
        }
    }

    ops.reversed().forEach { op -> evaluate(op.out) }
    return ops to registers
}

private fun part1(registers: Map<String, Int>) {
    val result = registers.read("z")
    println("part 1: $result => ${result.toLong(2)}")
}

private fun Map<String, Int>.read(prefix: String) = entries
    .filter { it.key.startsWith(prefix) }
    .sortedBy { it.key }
    .reversed()
    .joinToString("") { if (it.value == 0) "0" else "1" }

private fun part2(data: List<Op>) {
    val swapped = mutableListOf<String>()
    var c0: String? = null

    // had no idea how to solve this. this logic is from reddit solution thread.
    // to clarify: going by bit and handling carry i got. but how to handle the logic i had no idea...

    for (i in 0..44) {
        val n = String.format("%02d", i)

        // Half adder logic
        var m1 = find("x$n", "y$n", "XOR", data)
        var n1 = find("x$n", "y$n", "AND", data)

        var r1: String?
        var z1: String? = null
        var c1: String? = null
        if (c0 != null) {
            r1 = find(c0, m1!!, "AND", data)
            if (r1 == null) {
                val temp = m1
                m1 = n1
                n1 = temp
                swapped.addAll(listOf(m1!!, n1))
                r1 = find(c0, m1, "AND", data)
            }

            z1 = find(c0, m1, "XOR", data)

            if (m1.startsWith("z")) {
                val temp = m1
                m1 = z1
                z1 = temp
                swapped.addAll(listOf(m1!!, z1))
            }

            if (n1?.startsWith("z") == true) {
                val temp = n1
                n1 = z1
                z1 = temp
                swapped.addAll(listOf(n1!!, z1))
            }

            if (r1?.startsWith("z") == true) {
                val temp = r1
                r1 = z1
                z1 = temp
                swapped.addAll(listOf(r1!!, z1))
            }

            c1 = find(r1!!, n1!!, "OR", data)
        }

        if (c1?.startsWith("z") == true && c1 != "z45") {
            val temp = c1
            c1 = z1
            z1 = temp
            swapped.addAll(listOf(c1!!, z1))
        }

        c0 = if (c0 == null) n1 else c1
    }

    val result = swapped.sorted().joinToString(",")
    println("part 2: $result")
}

private fun find(a: String, b: String, operator: String, ops: List<Op>) =
    ops.find { it.op == operator && (it.a == a && it.b == b || it.a == b && it.b == a) }?.out
