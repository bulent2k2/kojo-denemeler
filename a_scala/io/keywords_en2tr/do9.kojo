// #yükle keyword-swap

val dir = "/Users/ben/src/kojo/git/kojo/installer/examples/othello/tr"
for (
      file <- List(
        "alfabeta",
        "anaTanimlar",
        "araYuz",
        "bellek",
        "durum",
        "eTahta",
        "otello",
        "tahta"
      )
) {
    val frompath = s"$dir/$file.kojo"
    val topath = s"$dir/swap/$file.kojo"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}
