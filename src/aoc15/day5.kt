package aoc15;

import java.io.File
import java.security.MessageDigest

private val digest = MessageDigest.getInstance("MD5")

fun main() {

    val data = File("aoc15d5.txt").readLines()
    println(data);

    val nice = data.filter { is_nice(it) }
    println(nice.size)

    val really = data.filter { is_really_nice(it) }
    println(really)
    println(really.size)
}

private const val vowels = "aeiou"
private val naughty = listOf("ab", "cd", "pq", "xy")

private fun is_nice(line: String): Boolean {
    val v_ok = line.count { it in vowels } >= 3
    val t_ok = line.windowed(2).any { it.first() == it.last() }
    val n_ok = naughty.none { it in line }
    return v_ok && t_ok && n_ok
}

private fun is_really_nice(line: String): Boolean {
    val v_ok = line.windowed(2).any {
        val first = line.indexOf(it)
        val other = line.indexOf(it, first + 2)
        if (other > first) println(it)
        other > first
    }
    val t_ok = line.windowed(3).any {
        if(it.first() == it.last()) println(it)
        it.first() == it.last()
    }
    println(line)
    println(v_ok)
    println(t_ok)
    return v_ok && t_ok
}
