import kotlin.math.abs

data class RopeState(
    val nodes: List<Knot>
) {

    private fun isHeadTooFar(head: Knot, index: Int) =
        abs(head.x - nodes[index].x) > 1 || abs(head.y - nodes[index].y) > 1

    fun moveTo(direction: Direction): RopeState {
        return nodes.foldIndexed(emptyList<Knot>()) { index, newNodes, node ->
            if (index == 0) {
                newNodes + node.moveTo(direction)
            } else {
                newNodes + node.moveTo(newNodes[index - 1])
            }
        }.let {
            RopeState(it)
        }
    }
}

data class Knot(
    val x: Int,
    val y: Int,
) {
    fun moveTo(direction: Direction): Knot {
        return when (direction) {
            Direction.UP -> copy(y = y + 1)
            Direction.DOWN -> copy(y = y - 1)
            Direction.LEFT -> copy(x = x - 1)
            Direction.RIGHT -> copy(x = x + 1)
        }
    }

    fun moveTo(previousNode: Knot): Knot {
        return when {
            abs(x - previousNode.x) <= 1 && abs(y - previousNode.y) <= 1 -> this
            y == previousNode.y -> copy(x = previousNode.x + (x - previousNode.x).coerceAtMost(1).coerceAtLeast(-1))
            x == previousNode.x -> copy(y = previousNode.y + (y - previousNode.y).coerceAtMost(1).coerceAtLeast(-1))
            else -> copy(
                x = x + (previousNode.x - x).coerceAtLeast(-1).coerceAtMost(1),
                y = y + (previousNode.y - y ).coerceAtLeast(-1).coerceAtMost(1)
            )
        }
    }
}

enum class Direction {
    LEFT, RIGHT, UP, DOWN;

    fun regex() = "${this.name.first().uppercase()} (.*)".toRegex()

    companion object {
        fun fromString(string: String) = values().first { it.regex().matches(string) }
    }
}

fun computeRopeStateHistory(numberOfKnots: Int, moves: List<Direction>): List<RopeState> {
    return RopeState(List(numberOfKnots) { Knot(0, 0) }).let { state ->
        moves.fold(listOf(state) to state) { (states, state), move ->
            state.moveTo(move).let { newState ->
                (states + newState) to newState
            }
        }.first
    }
}


@OptIn(ExperimentalStdlibApi::class)
fun main() {
    "day_09".getLines()
        .map { prompt ->
            Direction.fromString(prompt).let {
                prompt to it.regex().matchEntire(prompt)!!.groupValues[1].toInt()
            }
        }.map { (move, count) ->
            List(count) { Direction.fromString(move) }
        }.flatten()
        .let { moves ->
            computeRopeStateHistory(2, moves).let { history ->
                history.map { it.nodes.last() }.toSet().let {
                    println("Number of unique locations: ${it.size}")
                }
            }
            computeRopeStateHistory(10, moves).let { history ->
                history.map { it.nodes.last() }.toSet().let {
                    println("Number of unique locations: ${it.size}")
                }
            }
        }
}
