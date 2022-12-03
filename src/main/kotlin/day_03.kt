
class Rucksack(val cargo: String) {
    private val firstCompartment = cargo.subSequence(0, cargo.length / 2)
    private val secondCompartment = cargo.subSequence(cargo.length / 2, cargo.length - 0)

    fun commonItem(): Char {
        return firstCompartment.toSet().intersect(secondCompartment.toSet()).single()
    }

    companion object {
        val ITEMS = (('a'..'z') + ('A' .. 'Z')).toList()
    }
}

class Group(private val rucksacks: List<Rucksack>) {
    fun commonItem(): Char {
        return rucksacks.map { it.cargo.toSet() }.fold(emptySet<Char>()) { acc, i ->
            if (acc.isEmpty()){
                i
            }
            else {
                acc.intersect(i)
            }
        }.single()
    }
}

fun Char.priority(): Int {
    return Rucksack.ITEMS.indexOf(this) + 1
}

fun main() {
    "day_03".getLines()
        .map { Rucksack(it) }
        .let { rucksacks ->
            println("Sum of priorities: ${rucksacks.sumOf { it.commonItem().priority() }}")
            rucksacks.chunked(3).map { Group(it) }.let { groups ->
                println("Sum of priorities by groups: ${groups.sumOf { it.commonItem().priority() }}")
            }
        }
}
