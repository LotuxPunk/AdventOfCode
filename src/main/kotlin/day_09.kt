data class RopeState(
    val head: EndOfRope,
    val tail: EndOfRope
) {
    fun move(direction: Direction): RopeState {
        return when (direction) {
            Direction.LEFT -> RopeState(head.moveLeft(), tail)
            Direction.RIGHT -> RopeState(head, tail.moveRight())
        }
    }
}

data class EndOfRope(
    val x: Int,
    val y: Int,
)

enum class Direction {
    LEFT, RIGHT, UP, DOWN;

    fun regex() = "${this.name.first().uppercase()} (.*)".toRegex()

    companion object {
        fun fromString(string: String) = values().first { it.regex().matches(string) }
    }
}

fun isHeadTooFarFromTail(head: EndOfRope, tail: EndOfRope, sizeOfRope: Int = 1) = kotlin.math.abs(head.x - tail.x) + kotlin.math.abs(head.y - tail.y) > sizeOfRope + 1

fun main() {
    "day_09".getLines()
        .map { prompt ->
            Direction.fromString(prompt).let {
                prompt to it.regex().matchEntire(prompt)!!.groupValues[1].toInt()
            }
        }.let { moves ->
            RopeState(
                EndOfRope(0, 0),
                EndOfRope(0, 0)
            ).let { state ->
                moves.fold(listOf(state) to state) { (states, state), (prompt, value) ->
                    val newState = when (Move.fromString(prompt)) {
                        Move.LEFT -> state.head.copy(x = state.head.x - value).let { newHeadPosition ->
                            if (isHeadTooFarFromTail(newHeadPosition, state.tail)) {
                                state.copy(head = newHeadPosition, tail = state.head)
                            } else {
                                state.copy(head = newHeadPosition)
                            }
                        }
                        Move.RIGHT -> state.head.copy(x = state.head.x + value).let { head ->
                            if (isHeadTooFarFromTail(head, state.tail)) {
                                state.copy(head = head, tail = state.head)
                            } else {
                                state.copy(head = head)
                            }
                        }
                        Move.UP -> state.head.copy(y = state.head.y + value).let { head ->
                            if (isHeadTooFarFromTail(head, state.tail)) {
                                state.copy(head = head, tail = state.head)
                            } else {
                                state.copy(head = head)
                            }
                        }
                        Move.DOWN -> state.head.copy(y = state.head.y - value).let { head ->
                            if (isHeadTooFarFromTail(head, state.tail)) {
                                state.copy(head = head, tail = state.head)
                            } else {
                                state.copy(head = head)
                            }
                        }
                    }
                    states + newState to newState
                }.first.let { history ->
                    history.map { it.tail }.toSet().let {
                        println("Number of unique locations: ${it.size}")
                    }
                }
            }
        }
}
