
interface Node {
    fun size(): Int
}

data class File(private val size: Int, val name: String): Node {
    override fun size(): Int {
        return size
    }
}

class Directory(private val name: String, private val parent: Directory? = null, val children: MutableList<Node> = mutableListOf()): Node {
    override fun size(): Int {
        return children.sumOf { it.size() }
    }

    fun addChild(child: Node): Directory {
        children.add(child)
        return this
    }

    private fun parent() = parent ?: this

    fun moveTo(name: String) = when(name) {
        ".." -> parent()
        else -> children.filterIsInstance<Directory>().firstOrNull { it.name == name } ?: throw IllegalStateException("Not found children $name")
    }
}

enum class Command(val prompt: Regex) {
    LS("\\\$ ls".toRegex()),
    CD("\\\$ cd (.*)".toRegex()),
    DIR("dir (.*)".toRegex()),
    FILE("^([0-9]*) (.*)".toRegex());

    companion object {
        fun from(value: String): Command {
            return values().first { it.prompt.matches(value) }
        }
    }
}

fun getDirs(root: Directory): List<Directory> {
    return listOf(root) + root.children.filterIsInstance<Directory>().flatMap { getDirs(it) }
}

fun main() {
    "day_07"
        .getLines()
        .drop(1) // skipping root
        .let { lines ->
            Directory("/").let { root ->
                lines.fold(root) { currentDir, cmd ->
                    Command.from(cmd).let { command ->
                        when(command) {
                            Command.CD -> {
                                command.prompt.matchEntire(cmd)!!.let {
                                    currentDir.moveTo(it.groupValues[1])
                                }
                            }
                            Command.DIR -> {
                                command.prompt.matchEntire(cmd)!!.let {
                                    currentDir.addChild(Directory(it.groupValues[1], currentDir))
                                }
                            }
                            Command.FILE -> {
                                command.prompt.matchEntire(cmd)!!.let {
                                    currentDir.addChild(File(it.groupValues[1].toInt(), it.groupValues[2]))
                                }
                            }
                            else -> currentDir
                        }
                    }
                }

                getDirs(root).let { directories ->
                    directories.map { it.size() }.let { sizes ->
                        println("Small directories total size: ${sizes.filter { it <= 100000 }.sum()}")

                        val updateSize = 30000000
                        val diskSpace = 70000000

                        (diskSpace - sizes[0]).let { remainingSpace ->
                            (updateSize - remainingSpace).let { spaceToRecover ->
                                println("Closest directory size: ${sizes.filter { it >= spaceToRecover }.min()}")
                            }
                        }
                    }

                }
            }
        }
}