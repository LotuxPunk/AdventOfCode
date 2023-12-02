package adventOfCode.y2022

import adventOfCode.getFileContent
import kotlin.system.measureTimeMillis

fun streamMarker(stream: String, numberOfCharacters: Int) {
    stream.foldIndexed(emptyList<Char>()) { index, previousChars, c ->
        if (previousChars.size < numberOfCharacters) {
            previousChars + c
        }
        else {
            (previousChars.drop(1) + c).takeUnless {
                it.toSet().size == numberOfCharacters
            } ?: run { println("numberOfCharacters: $numberOfCharacters - Marker at position ${index + 1} "); return }
        }
    }
}

fun main() {
    val elapsed = measureTimeMillis {
        "2022/day_06".getFileContent().let {
            streamMarker(it, 4)
            streamMarker(it, 14)
        }
    }

    println("Elapsed: $elapsed ms")
}
