package aoc15;

import java.security.MessageDigest

private val digest = MessageDigest.getInstance("MD5")

fun main() {

    val data = "iwrupvqb"
    println(data);

    val numbers = generateSequence(1) { it + 1 }
    val inputs = numbers.mapIndexed { i, it ->
        val input = "$data$it"
        val md5 = to_md5(input)
        "$md5:${i + 1}"
    }
    val zeroed = inputs.filter { it.startsWith("00000") }
    println(zeroed.take(10).toList())

    println(inputs.first { it.startsWith("000000") })

}

@OptIn(ExperimentalStdlibApi::class)
private fun to_md5(it: String): String {
    val result = digest.digest(it.toByteArray())
    digest.reset()
    return result.toHexString()
}
