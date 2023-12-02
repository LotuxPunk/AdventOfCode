package adventOfCode.y2023

import adventOfCode.getLines

data class CalibrationValues(
    val firstNumber: Int,
    val firstNumberIndex: Int,
    val secondNumber: Int,
    val secondNumberIndex: Int,
    val line: String
) {
    val couple: Int
        get() {
            return "${firstNumber}${secondNumber}".toInt()
        }
}


fun main() {

    val lines = "2023/day_01".getLines()
    val calibrationValues = lines.calibrationValues()
    val sumOfCalibrationValues = lines.calibrationValues().sumOf { it.couple }

    println("The sum of the calibration values is $sumOfCalibrationValues")

    val wordNumber = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )

    val updatedCalibrationValues = calibrationValues
        .map { section ->
            var updatedCalibrationValue = section
            wordNumber.forEachIndexed { index, s ->
                val regex = Regex(s)
                regex.findAll(section.line).forEach { matchResult ->
                    if (matchResult.range.first < updatedCalibrationValue.firstNumberIndex) {
                        updatedCalibrationValue = updatedCalibrationValue.copy(firstNumber = index + 1, firstNumberIndex = matchResult.range.first)
                    } else if (matchResult.range.first > updatedCalibrationValue.secondNumberIndex) {
                        updatedCalibrationValue = updatedCalibrationValue.copy(secondNumber = index + 1, secondNumberIndex = matchResult.range.first)
                    }
                }
            }
            updatedCalibrationValue
        }

    val sumOfUpdatedSections = updatedCalibrationValues.sumOf { it.couple }

    println("The sum of the updated calibration values is $sumOfUpdatedSections")
}

fun List<String>.calibrationValues(): List<CalibrationValues> {
    return this.map { line ->
        val firstNumberIndex = line.indexOfFirst { it.isDigit() }
        val secondNumberIndex = line.indexOfLast { it.isDigit() }

        CalibrationValues(
            firstNumber = line.substring(firstNumberIndex, firstNumberIndex + 1).toInt(),
            firstNumberIndex = firstNumberIndex,
            secondNumber = line.substring(secondNumberIndex, secondNumberIndex + 1).toInt(),
            secondNumberIndex = secondNumberIndex,
            line = line
        )
    }
}
