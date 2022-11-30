// #yükle keyword-swap

val dir = "/Users/ben/src/kojo/git/kojo/src/main/resources/robosim/tr"
for (
      file <- List(
        "cevre", "obstacle-furthest", "obstacle-greedy", "robot"
      )
) {
    val frompath = s"$dir/$file.kojo"
    val topath = s"$dir/swap/$file.kojo"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}
