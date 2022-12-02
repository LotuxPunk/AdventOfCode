
fun main() {
    val calories = object {}
        .javaClass
        .getResource("/day_01.txt")
        .readText()
        .split(System.lineSeparator())
        .map {
            if (it.isBlank()) {
                0
            } else {
                it.toInt()
            }
        }.fold(emptyList<List<Int>>() to emptyList<Int>()) { (acc, currentList), i ->
            if (i == 0) {
                listOf(*acc.toTypedArray(), currentList) to emptyList()
            } else {
                acc to currentList + i
            }
        }.first

    println("Max calories: ${calories.maxOfOrNull { it.sum() }}")
    println("Sum of top 3: ${calories.map { it.sum() }.sortedDescending().take(3).sum()}")
}
