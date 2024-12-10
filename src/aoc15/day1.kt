package aoc15;

import java.io.File

fun main() {

    val data = File("aoc15d1.txt").readLines().single()
    println(data);

    val up = data.count { it == '(' }
    val down = data.count { it == ')' }
    println(up)
    println(down)
    println(up - down)

    var floor = 0
    for (i in data.indices) {
        if (data[i] == '(') floor++ else floor--
        if (floor == -1) {
            println(i+1)
            break
        }
    }
}
