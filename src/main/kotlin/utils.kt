fun String.getLines(): List<String> = object {}
    .javaClass
    .getResource("/$this.txt")!!
    .readText()
    .trimEnd()
    .split(System.lineSeparator())

fun String.getFileContent(): String = object {}
    .javaClass
    .getResource("/$this.txt")!!
    .readText()
    .trimEnd()

