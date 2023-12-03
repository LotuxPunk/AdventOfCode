import adventOfCode.getLines

enum class Operation(val regex: Regex) {
    ADD("addx (.*)".toRegex()),
    NOOP("noop".toRegex());

    companion object {
        fun fromString(string: String) = values().first { it.regex.matches(string) }
    }
}

data class Instruction(val operation: Operation, val value: Int)

data class CPUState(val accumulator: Int)

fun main() {
    "day_10".getLines()
        .map {
            Operation.fromString(it).let { operation ->
                when (operation) {
                    Operation.ADD -> Instruction(operation, operation.regex.matchEntire(it)!!.groupValues[1].toInt())
                    Operation.NOOP -> Instruction(operation, 0)
                }
            }
        }.let { instructions ->
            CPUState(1).let { state ->
                instructions.fold(listOf(state)) { states, instruction ->
                    when (instruction.operation) {
                        Operation.ADD -> states.last().copy(accumulator = states.last().accumulator + instruction.value).let { states + states.last() + it }
                        Operation.NOOP -> states.last().let { states + it }
                    }
                }
            }.let { history ->
                val accumulatorAtCycle = { cycle: Int -> history[cycle - 1].accumulator }
                val printAccumulator = { cycle: Int -> println("Accumulator at index $cycle: ${accumulatorAtCycle(cycle)}") }
                val signalStrength = { cycle: Int -> accumulatorAtCycle(cycle) * cycle }

                printAccumulator(20)
                printAccumulator(60)
                printAccumulator(100)
                printAccumulator(140)
                printAccumulator(180)
                printAccumulator(220)

                println("Sum of signal strengths at 20, 60, 100, 140, 180 and 220: ${signalStrength(20) + signalStrength(60) + signalStrength(100) + signalStrength(140) + signalStrength(180) + signalStrength(220)}")
            }

            
        }
}
