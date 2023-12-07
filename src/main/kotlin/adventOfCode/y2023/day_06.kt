package adventOfCode.y2023

import adventOfCode.getLines

fun computePossiblePushDurations(maxTime: Long, distanceToReach: Long): List<Pair<Long, Long>> = (0..maxTime).map { timeAndSpeed ->
    timeAndSpeed to timeAndSpeed * (maxTime - timeAndSpeed)
}.filter { (_, distanceReached) -> distanceReached > distanceToReach }

fun main() {
    val (timeInput, distanceInput) = "2023/day_06".getLines()
    val times = timeInput.replace("Time: ", "").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val distance = distanceInput.replace("Distance: ", "").split(" ").filter { it.isNotBlank() }.map { it.toLong() }

    val races = times.zip(distance)
    val possibilities = races.map { (time, distance) ->
        computePossiblePushDurations(time, distance)
    }
    val numberOfWayToWin = possibilities.map { it.count() }.reduce { acc, it -> acc * it }

    println(numberOfWayToWin)

    val time = timeInput.replace("Time: ", "").replace(" ", "").toLong()
    val distanceToReach = distanceInput.replace("Distance: ", "").replace(" ", "").toLong()
    val possiblePushDurations = computePossiblePushDurations(time, distanceToReach)
    val numberOfWayToWinSingleRace = possiblePushDurations.count()

    println(numberOfWayToWinSingleRace)
}