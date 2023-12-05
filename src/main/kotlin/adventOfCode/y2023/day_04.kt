package adventOfCode.y2023

import adventOfCode.getLines
import kotlin.math.pow

data class LotteryTicket(
    val id: Int,
    val winningNumbers: Set<Int>,
    val numbers: Set<Int>,
    val line: String
) {
    val points: Int
        get() {
            return 2.toDouble().pow(this.correctNumbers.count() - 1).toInt()
        }

    val correctNumbers: Set<Int>
        get() {
            return numbers.intersect(winningNumbers)
        }
}

fun List<String>.lotteryTickets(): List<LotteryTicket> {
    // Input example:
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    // Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
    // Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
    // Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
    // Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
    // Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    // Card {id}: {winningNumbers} | {numbers}

    val inputRegex = Regex("Card\\s+(\\d+):\\s*((?:\\d+\\s*)+)\\|\\s*((?:\\d+\\s*)+)")
    return this.map {
        inputRegex.find(it)!!
    }.map { matchResult ->
        val (id, winningNumbers, numbers) = matchResult.destructured
        val numbersSet = numbers.trim().split(" ").filterNot { it.isBlank() }.map { it.toInt() }.toSet()
        val winningNumbersSet = winningNumbers.trim().split(" ").filterNot { it.isBlank() }.map { it.toInt() }.toSet()
        LotteryTicket(id.toInt(), winningNumbersSet, numbersSet, matchResult.value)
    }
}

fun main() {

    val lines = "2023/day_04".getLines()
    val lotteryTickets = lines.lotteryTickets()
    val sumOfLotteryTickets = lotteryTickets.sumOf { it.points }

    println("The sum of the lottery tickets is $sumOfLotteryTickets")

    fun computeNumberOfCardToCopy(cards: List<LotteryTicket>): Int {
        val size = cards.sumOf { card ->
            val index = lotteryTickets.indexOf(card)
            computeNumberOfCardToCopy(lotteryTickets.subList(index + 1, index + card.correctNumbers.count() + 1))
        }

        return size + cards.count()
    }

    lotteryTickets.forEachIndexed { index, card ->
        lotteryTickets.subList(index + 1, index + card.correctNumbers.count() + 1)
    }

    val cardStackSize = computeNumberOfCardToCopy(lotteryTickets)

    println("The sum of cards in the stack is ${cardStackSize}")
}