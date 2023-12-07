package adventOfCode.y2023

import adventOfCode.getLines

enum class Card(val value: Char) {
    JOKER('J'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun highestCard(cards: List<Card>): Card {
            return cards.maxByOrNull { it.ordinal }!!
        }
    }
}

enum class Strength {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIRS,
    ONE_PAIR,
    HIGH_CARD,
}

data class Hand(
    val cards: List<Card>,
    val cardsWithJokersComputed: List<Card>,
    val bidAmount: Int
) {
    val isFiveOfAKind: Boolean
        get() = cardsWithJokersComputed.groupBy { it }.values.any { it.size == 5 }
    val isFourOfAKind: Boolean
        get() = cardsWithJokersComputed.groupBy { it }.values.any { it.size == 4 }
    val isFullHouse: Boolean
        get() = isThreeOfAKind && cardsWithJokersComputed.groupBy { it }.values.any { it.size == 2 }
    val isThreeOfAKind: Boolean
        get() = cardsWithJokersComputed.groupBy { it }.values.any { it.size == 3 }

    val isTwoPairs: Boolean
        get() = cardsWithJokersComputed.groupBy { it }.values.count { it.size == 2 } == 2
    val isOnePair: Boolean
        get() = cardsWithJokersComputed.groupBy { it }.values.count { it.size == 2 } == 1

    val strength: Strength
        get() = when {
            isFiveOfAKind -> Strength.FIVE_OF_A_KIND
            isFourOfAKind -> Strength.FOUR_OF_A_KIND
            isFullHouse -> Strength.FULL_HOUSE
            isThreeOfAKind -> Strength.THREE_OF_A_KIND
            isTwoPairs -> Strength.TWO_PAIRS
            isOnePair -> Strength.ONE_PAIR
            else -> Strength.HIGH_CARD
        }

    fun compare(other: Hand): Int {
        return when {
            strength > other.strength -> 1
            strength < other.strength -> -1
            else -> {
                cards.zip(other.cards).map { (card, otherCard) ->
                    otherCard.compareTo(card)
                }.first { it != 0 }
            }
        }
    }

    companion object {
        fun fromString(input: String, jokerEnabled: Boolean = false): Hand {
            val cards = input.split(" ").first().map { char ->
                if (jokerEnabled) {
                    Card.entries.first { it.value == char }
                } else {
                    Card.entries.last { it.value == char }
                }
            }
            val bidAmount = input.split(" ").last().toInt()

            return Hand(
                cards,
                cards.map { card ->
                    if (card == Card.JOKER) {
                        cards.filterNot { it == Card.JOKER }.groupBy { it }.values.maxByOrNull { it.count() }?.first() ?: Card.highestCard(cards)
                    } else {
                        card
                    }
                },
                bidAmount
            )
        }
    }
}


fun main() {
    val sortedHands = "2023/day_07".getLines().map {
        Hand.fromString(it)
    }.sortedWith { hand, otherHand ->
        hand.compare(otherHand)
    }.reversed()

    sortedHands.foldIndexed(0) { index, acc, hand ->
        acc + hand.bidAmount * (index + 1)
    }.let(::println)

    val sortedHandsJoker = "2023/day_07".getLines().map {
        Hand.fromString(it, true)
    }.sortedWith { hand, otherHand ->
        hand.compare(otherHand)
    }.reversed()

    sortedHandsJoker.foldIndexed(0) { index, acc, hand ->
        acc + hand.bidAmount * (index + 1)
    }.let(::println)
}