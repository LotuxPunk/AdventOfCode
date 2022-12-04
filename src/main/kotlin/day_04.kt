class Assignment(private val range: IntRange) {
    fun isCompletelyOverlapping(other: Assignment): Boolean {
        return this.range.first <= other.range.first && this.range.last >= other.range.last
    }

    fun isOverlapping(other: Assignment): Boolean {
        return this.range.intersect(other.range).isNotEmpty()
    }

    companion object {
        fun areBothOverlapping(pair: Pair<Assignment, Assignment>) = pair.first.isOverlapping(pair.second)
        fun isOneOverlappingTheOther(pair: Pair<Assignment, Assignment>) = pair.first.isCompletelyOverlapping(pair.second) || pair.second.isCompletelyOverlapping(pair.first)
    }
}

fun main() {
    "day_04".getLines()
        .map { it.split(",") }
        .map {
            it.map { rangeString ->
                rangeString
                    .split("-")
                    .map { v -> v.toInt() }
                    .let { r -> Assignment(r.first()..r.last()) }
            }
        }
        .map { it.first() to it.last() }
        .let { pairs ->
            println("Count of complete overlapping: ${
                pairs.count {
                    Assignment.isOneOverlappingTheOther(it)
                }
            }")

            println("Count of at least partially overlapping: ${
                pairs.count {
                    Assignment.areBothOverlapping(it)
                }
            }")

        }
}