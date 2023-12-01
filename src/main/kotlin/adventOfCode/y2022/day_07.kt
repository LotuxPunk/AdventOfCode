package adventOfCode.y2022

import adventOfCode.getLines

interface Node {
    fun size(): Int
}

data class File(private val size: Int, val name: String): Node {
    override fun size(): Int {
        return size
    }
}

data class Directory(private val name: String, private val parent: Directory? = null, private val root: Directory? = null, val children: MutableList<Node> = mutableListOf()): Node {
    override fun size(): Int {
        return children.sumOf { it.size() }
    }

    fun addChild(child: Node): Directory {
        if (child is Directory) {
            children.add(
                child.copy(
                    parent = this,
                    root = root()
                )
            )
        }
        else {
            children.add(child)
        }
        return this
    }

    private fun parent() = parent ?: this

    private fun root() = root ?: this

    fun moveTo(name: String) = when(name) {
        "/" -> root()
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

fun getDirs(directory: Directory): List<Directory> {
    return listOf(directory) + directory.children.filterIsInstance<Directory>().flatMap { getDirs(it) }
}

fun main() {
    "2022/day_07"
        .getLines()
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
                                    currentDir.addChild(Directory(it.groupValues[1]))
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