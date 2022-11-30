// #yükle keyword-swap

val dir = "/Users/ben/src/kojo/git/kojo/src/main/resources/ka-bridge/tr"
for (
      file <- List(
        "sample"
      )
) {
    val frompath = s"$dir/$file.kojo"
    val topath = s"$dir/swap_$file.kojo"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}
