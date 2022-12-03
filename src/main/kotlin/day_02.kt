import kotlin.system.measureTimeMillis

enum class RockPaperScissors(val references: List<String>) {
    ROCK(listOf("A", "X")),
    PAPER(listOf("B", "Y")),
    SCISSORS(listOf("C", "Z"));

    private val score get() = this.ordinal + 1

    fun beats(other: RockPaperScissors) = this.ordinal == (other.ordinal + 1) % 3

    /**
     * X need to lose
     * Y need to draw
     * Z need to win
     */
    fun computeStrategy(strategy: String) = when (strategy) {
        "X" -> RockPaperScissors.values().first { this.beats(it) }
        "Y" -> this
        "Z" -> RockPaperScissors.values().first { it.beats(this) }
        else -> throw IllegalArgumentException("Invalid strategy")
    }.let {
        this to it
    }

    fun win() = this.score + WIN_BONUS
    fun draw() = this.score + DRAW_BONUS
    fun lose() = this.score

    companion object {
        fun fromReference(reference: String) = values().first { it.references.contains(reference) }
        const val WIN_BONUS = 6
        const val DRAW_BONUS = 3
    }
}

fun Pair<String, String>.fromReferences() =
    RockPaperScissors.fromReference(this.first) to RockPaperScissors.fromReference(this.second)

fun Pair<String, String>.fromReferencesWithStrategy() = RockPaperScissors.fromReference(this.first).computeStrategy(this.second)

fun Pair<RockPaperScissors, RockPaperScissors>.score(): Pair<Int, Int> {
    val (first, second) = this
    return when {
        first.beats(second) -> first.win() to second.lose()
        second.beats(first) -> first.lose() to second.win()
        else -> first.draw() to second.draw()
    }
}

fun main() {
    val elapsed = measureTimeMillis {
        val input = "day_02"
            .getLines()
            .map { it.split(" ") }.map { it[0] to it[1] }

        println("Part 1")
        val scoresPart1 = input.map { it.fromReferences() }.map { it.score() }

        println("Score for other player: ${scoresPart1.sumOf { it.first }}")
        println("Score for me: ${scoresPart1.sumOf { it.second }}")

        println("Part 2")
        val scoresPart2 = input.map { it.fromReferencesWithStrategy() }.map { it.score() }

        println("Score for other player: ${scoresPart2.sumOf { it.first }}")
        println("Score for me: ${scoresPart2.sumOf { it.second }}")
    }

    println("Elapsed: $elapsed ms")
}
