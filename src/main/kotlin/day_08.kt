import kotlin.system.measureTimeMillis

enum class CardinalDirection {
    NORTH, EAST, SOUTH, WEST
}

class Tree(
    private val height: Short,
    private val lineOfSight: Map<CardinalDirection, List<Short>>
) {
    fun isVisibleFromOutside(): Boolean {
        return lineOfSight.any { (_, line) ->
            line.takeIf { it.isNotEmpty() }?.let {
                it.all { otherTreeHeight ->
                    otherTreeHeight < height
                }
            } ?: true
        }
    }

    private tailrec fun viewCount(otherTreeHeights: List<Short>, count: Int = 0): Int = when {
        otherTreeHeights.isEmpty() -> count + 0
        otherTreeHeights.first() < height -> viewCount(otherTreeHeights.drop(1), count + 1)
        else -> {
            count + 1
        }
    }

    private fun viewDistances() = lineOfSight.map { (_, line) ->
        viewCount(line)
    }

    fun computeTreeScenicScore(): Int {
        return viewDistances().reduce { acc, i -> acc * i }
    }
}

fun main() {
    val elapsed = measureTimeMillis {
        "day_08".getLines().map { it.chunked(1).map { treeHeightString -> treeHeightString.toShort() } }
            .let { forestMatrix ->
                forestMatrix.mapIndexed { y, row ->
                    row.mapIndexed { x, height ->
                        Tree(height, CardinalDirection.values().associateWith { direction ->
                            when (direction) {
                                CardinalDirection.NORTH -> forestMatrix.subList(0, y).map { it[x] }.reversed()
                                CardinalDirection.EAST -> forestMatrix[y].drop(x + 1)
                                CardinalDirection.SOUTH -> forestMatrix.drop(y + 1).map { it[x] }
                                CardinalDirection.WEST -> forestMatrix[y].subList(0, x).reversed()
                            }
                        })
                    }
                }.flatten().let { trees ->
                    println("Number of visible trees: ${trees.count { tree -> tree.isVisibleFromOutside() }}")
                    println("Scenic score: ${trees.maxOf { tree -> tree.computeTreeScenicScore() }}")
                }
            }
    }
    println("Elapsed: $elapsed ms")
}

