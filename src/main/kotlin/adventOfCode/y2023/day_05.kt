package adventOfCode.y2023

import adventOfCode.getFileContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

data class SeedMap(
    val destination: Long,
    val source: LongRange,
)

fun String.toSeedMap() = this.split("\n").drop(1).map { input ->
    val (destination, source, range) = input.split(" ").map { it.toLong() }

    SeedMap(
        destination = destination,
        source = source..<source + range,
    )
}.sortedBy { it.source.first }.toTypedArray()


suspend fun main() {
    val values = "2023/day_05".getFileContent().split("\n\n")

    val seedsInput = values[0]
    val seeds = seedsInput.replace("seeds: ", "").split(" ").map { it.toLong() }

    val seedToSoilMap = values[1].toSeedMap()
    val soilToFertilizerMap = values[2].toSeedMap()
    val fertilizerToWaterMap = values[3].toSeedMap()
    val waterToLightMap = values[4].toSeedMap()
    val lightToTemperatureMap = values[5].toSeedMap()
    val temperatureToHumidityMap = values[6].toSeedMap()
    val humidityToLocationMap = values[7].toSeedMap()

    val chain = arrayOf(
        seedToSoilMap,
        soilToFertilizerMap,
        fertilizerToWaterMap,
        waterToLightMap,
        lightToTemperatureMap,
        temperatureToHumidityMap,
        humidityToLocationMap
    )

    fun chainToLocation(seed: Long): Long {
        return chain.fold(seed) { value, seedMaps ->
            seedMaps.firstOrNull { value in it.source }?.let { seedMap ->
                (value - seedMap.source.first) + seedMap.destination
            } ?: value
        }
    }

    val minLocation = seeds.minOf { seed ->
        chainToLocation(seed)
    }

    println("The minimum location is $minLocation")

    // I know, this is brute force, but at least it's parallelized
    withContext(Dispatchers.IO) {
        measureTimeMillis {
            seeds
                .zipWithNext()
                .mapIndexedNotNull { index, pair -> if (index % 2 == 0) pair else null }
                .map { (start, range) ->
                    async {
                        (start..start + range).minOf { seed ->
                            chainToLocation(seed)
                        }
                    }
                }.awaitAll().min().let {
                    println("The minimum location for ranges is $it")
                }
        }.let {
            println("Took $it ms")
        }
    }

}
