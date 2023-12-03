import adventOfCode.getLines

fun main() {
    val lines = "2023/day_02".getLines()

    val games = lines.map { line ->
        val gameSplit = line.split(":")
        val gameId = gameSplit.first().trim().substring(5).toInt()

        val sets = gameSplit.last().split(";")

        Game(
            id = gameId,
            bags = sets.map { set ->
                set.split(",").let { cubes ->
                    Bag(
                        blue = cubes
                            .filter { cube -> cube.contains("blue") }
                            .takeIf { it.isNotEmpty() }?.first()?.replace("blue", "")?.trim()?.toInt() ?: 0,
                        red = cubes
                            .filter { cube -> cube.contains("red") }
                            .takeIf { it.isNotEmpty() }?.first()?.replace("red", "")?.trim()?.toInt() ?: 0,
                        green = cubes
                            .filter { cube -> cube.contains("green") }
                            .takeIf { it.isNotEmpty() }?.first()?.replace("green", "")?.trim()?.toInt() ?: 0,
                    )
                }
            }.toList()
        )
    }

    games.sumOf {
        if (it.part1()) {
            it.id
        } else {
            0
        }
    }.let {
        println("The sum of the ids is $it")
    }

    games.sumOf {
        it.part2()
    }.let {
        println("The power of a set of cubes is $it")
    }

}

data class Bag(
    val blue: Int = 0,
    val red: Int = 0,
    val green: Int = 0,
)

data class Game(
    val id: Int,
    val bags: List<Bag> = emptyList()
) {

    val maxRed = bags.maxOf { it.red }
    val maxGreen = bags.maxOf { it.green }
    val maxBlue = bags.maxOf { it.blue }

    fun part1(): Boolean {
        return bags.all {
             it.red <= 12 && it.green <= 13 && it.blue <= 14
        }
    }

    fun part2(): Int {
        return maxRed * maxGreen * maxBlue
    }

}
