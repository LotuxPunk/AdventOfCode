package adventOfCode.y2022

import adventOfCode.getFileContent

data class Stack(val crates: MutableList<Char>) {
    fun push(c: Char) {
        crates.add(c)
    }

    fun pushAll(cs: List<Char>) {
        crates.addAll(cs)
    }

    fun pop(): Char {
        return crates.removeAt(crates.lastIndex)
    }

    fun pop(n: Int): List<Char> {
        return (crates.lastIndex - n until crates.lastIndex).map { pop() }.reversed()
    }

    fun peek(): Char {
        return crates.last()
    }

    fun copy(): Stack {
        return Stack(crates.toMutableList())
    }
}

fun main() {
    "2022/day_05".getFileContent().split("\n\n").let { (cargo, moves) ->
            moves to (cargo.split(System.lineSeparator()).let {
                    it.subList(0, it.size - 1)
                }.map { it.chunked(4) }.flatMap { strings -> strings.mapIndexed { index, s -> index to s[1] } }
                .groupBy { it.first }
                .map { it.value.filterNot { (_, letter) -> letter.isWhitespace() }.map { (_, letter) -> letter } }
                .map { it.reversed() }.map { Stack(it.toMutableList()) })
        }.let { (moves, stacks) ->

            moves.trimEnd().split(System.lineSeparator()).map { it.chunked(7) }.map { chunks ->
                    listOf(
                        chunks[0].replace("move", ""), chunks[1].replace("from", ""), chunks[2].replace("to", "")
                    ).map { it.trim().toInt() }
                }.let { moves ->
                    stacks.map { it.copy() }.let { stacksPart1 ->
                        moves.map { (count, from, to) ->
                            for (i in 0 until count) {
                                stacksPart1[to - 1].push(stacksPart1[from - 1].pop())
                            }
                        }.let {
                            println("Message part 1: ${stacksPart1.map { it.peek() }.joinToString("")}")
                        }
                    }

                    stacks.map { it.copy() }.let { stacksPart2 ->
                        moves.map { (count, from, to) ->
                            stacksPart2[from - 1].pop(count).let {
                                stacksPart2[to - 1].pushAll(it)
                            }
                        }.let {
                            println("Message part 2: ${stacksPart2.map { it.peek() }.joinToString("")}")
                        }
                    }
                }
        }

}
