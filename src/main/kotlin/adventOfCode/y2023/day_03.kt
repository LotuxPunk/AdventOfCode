package adventOfCode.y2023

import adventOfCode.getLines

data class Part(
    val number: Int,
    val isPartNumber: Boolean,
)

fun main() {
    val lines = "2023/day_03".getLines()
    val parts = mutableListOf<Part>()

    lines.forEachIndexed { lineIndex, line ->

        var charIndex = 0
        var number = ""
        var isPartNumber = false
        do {
            val char = line[charIndex]
            when {
                char =='.' -> {
                    if (number.length > 0) {
                        parts.add(
                            Part(
                                number = number.toInt(),
                                isPartNumber = isPartNumber
                            )
                        )

                        number = ""
                        isPartNumber = false
                    }

                    charIndex++
                }
                char.isDigit() -> {
                    number = "$number$char"

                    if (!isPartNumber) {
                        for (i in -1..1) {
                            for (j in -1..1) {
                                val checkLineIndex = lineIndex + i
                                val checkCharIndex = charIndex + j

                                if (checkLineIndex in lines.indices){
                                    if (checkCharIndex in line.indices) {
                                        if (!isPartNumber){
                                            val checkedChar = lines[checkLineIndex][checkCharIndex]
                                            isPartNumber = checkedChar != '.' && !checkedChar.isDigit()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    charIndex++
                }
                else -> {
                    charIndex++
                }
            }
        } while (charIndex < line.length)
    }

    parts.filter { it.isPartNumber }.sumOf { it.number }.let {
        println("Sum of the parts: $it")
    }
}
