package adventOfCode.y2023

import adventOfCode.getLines
import kotlin.system.measureTimeMillis

data class Scan(
    val report: List<Long>
) {

    fun extrapolateNumber(reversed: Boolean = false): Long {
        val computations: MutableList<MutableList<Long>> = mutableListOf(
            if (reversed) {
                report.reversed()
            } else {
                report
            }.toMutableList()
        )

        do {
            val currentRow = computations.last()
            computations.add(
                currentRow.zipWithNext().map { (v1, v2) ->
                    v2 - v1
                }.toMutableList()
            )
        } while (!computations.last().all { it == 0L })

        val reversedComputations = computations.reversed()

        reversedComputations.forEachIndexed { index, longs ->
            if (index == 0) {
                longs.add(0)
            } else {
                val valueToAdd = reversedComputations[index - 1].last()
                longs.add(longs.last() + valueToAdd)
            }
        }

        return computations.first().last()
    }
}

tailrec fun extrapolateNumber(values: List<Long>, extrapolatedValue: Long): Long {
    val nextRow = values.zipWithNext().map { (v1, v2) ->
        v2 - v1
    }

    if (nextRow.all { it == 0L }) {
        return extrapolatedValue
    }

    return extrapolateNumber(
        nextRow,
        extrapolatedValue + nextRow.last()
    )
}

fun main() {
    val scans = "2023/day_09".getLines().map { input ->
        input.split(" ").map { it.toLong() }.let {
            Scan(
                report = it
            )
        }
    }

    measureTimeMillis {
        scans.sumOf {
            it.extrapolateNumber()
        }.let {
            println("Part 1: $it")
        }
    }.let {
        println("Time: $it ms")
    }

    measureTimeMillis {
        scans.sumOf {
            it.extrapolateNumber(reversed = true)
        }.let {
            println("Part 2: $it")
        }
    }.let {
        println("Time: $it ms")
    }

    // Tailrec

    measureTimeMillis {
        scans.sumOf {
            extrapolateNumber(it.report, it.report.last())
        }.let {
            println("Part 1: $it")
        }
    }.let {
        println("Time: $it ms")
    }

    measureTimeMillis {
        scans.sumOf {
            extrapolateNumber(it.report.reversed(), it.report.first())
        }.let {
            println("Part 2: $it")
        }
    }.let {
        println("Time: $it ms")
    }
}
