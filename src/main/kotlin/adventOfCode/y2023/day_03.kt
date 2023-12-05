package adventOfCode.y2023

import adventOfCode.getLines

data class Part(
    val number: Int,
    val row: Int,
    val columnStart: Int,
    val columnEnd: Int
) {
    fun isPartNumber(matrix: Array<Array<Char>>): Boolean {
        val starRowCheck = (row - 1).coerceAtLeast(0)
        val endRowCheck = (row + 1).coerceAtMost(matrix.lastIndex)
        val startColumCheck = (columnStart - 1).coerceAtLeast(0)
        val endColumnCheck = (columnEnd + 1).coerceAtMost(matrix[row].lastIndex)

        for (i in starRowCheck..endRowCheck){
            for (j in startColumCheck..endColumnCheck){
                val char = matrix[i][j]
                val isPartNumber = !char.isDigit() && char != '.'
                if (isPartNumber) {
                    return true
                }
            }
        }
        return false
    }
}

fun main() {
    val lines = "2023/day_03".getLines()
    val matrix = lines.map {
        it.toCharArray().toTypedArray()
    }.toTypedArray()

    val parts = matrix.flatMapIndexed { rowIndex, row ->
        var number = ""
        row.mapIndexedNotNull { columnIndex, char ->
            if (char.isDigit()) {
                number = "$number$char"
            } else if (number.isNotBlank()) {
                val part = Part(
                    number = number.toInt(),
                    row = rowIndex,
                    columnStart = columnIndex - number.length,
                    columnEnd = columnIndex - 1
                )
                number = ""
                return@mapIndexedNotNull part
            }

            if (row.lastIndex == columnIndex && number.isNotBlank()) {
                return@mapIndexedNotNull Part(
                    number = number.toInt(),
                    row = rowIndex,
                    columnStart = columnIndex - number.length,
                    columnEnd = columnIndex - 1
                )
            }
            null
        }
    }

    parts.filter {
        it.isPartNumber(matrix)
    }.sumOf {
        it.number
    }.let {
        println("Sum of part number is: $it")
    }
}
