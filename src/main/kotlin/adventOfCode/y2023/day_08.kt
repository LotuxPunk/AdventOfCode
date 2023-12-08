package adventOfCode.y2023

import adventOfCode.getFileContent
import kotlin.system.measureTimeMillis

enum class Direction(val value: Char) {
    LEFT('L'), RIGHT('R');

    companion object {
        fun fromValue(value: Char) = entries.first { it.value == value }
    }
}

data class Node(val value: String, val left: String, val right: String)

fun Map<String, Node>.turn(direction: Direction, node: Node) = when (direction) {
    Direction.LEFT -> this[node.left]!!
    Direction.RIGHT -> this[node.right]!!
}

/**
 * Greatest common divisor
 */
fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

/**
 * Least common multiple
 */
fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

/**
 * Least common multiple of a list of numbers
 */
fun lcmOfList(numbers: List<Long>): Long {
    return numbers.reduce { acc, i -> lcm(acc, i) }
}

fun main() {
    val input = "2023/day_08".getFileContent().split("\n\n")
    val instructions = input.first().map { Direction.fromValue(it) }
    val nodeRegex = Regex("^(\\w+)\\s*=\\s*\\((\\w+),\\s*(\\w+)\\)\$")
    val nodesInput = input.last().split("\n").map { nodeRegex.matchEntire(it)!!.groupValues }
    val map = nodesInput.map { Node(it[1], it[2], it[3]) }.associateBy { it.value }

    var i : Long = 0
    var currentNode: Node = map["AAA"]!!
    val instructionSize = instructions.size.toLong()
    do {
        currentNode = map.turn(instructions[(i % instructionSize).toInt()], currentNode)
        i++
    } while (currentNode.value != "ZZZ")
    println("Part 1: $i")

    measureTimeMillis {
        val currentNodes = map.values.filter { it.value.endsWith("A") }
        val numberOfTurnsBeforeReachingZ = currentNodes.map { node ->
            var i : Long = 0
            var currentNode: Node = node
            do {
                currentNode = map.turn(instructions[(i % instructionSize).toInt()], currentNode)
                i++
            } while (!currentNode.value.endsWith("Z"))
            i
        }

        println("Part 2: ${lcmOfList(numberOfTurnsBeforeReachingZ)}")

    }.let {
        println("Took: $it ms")
    }

}