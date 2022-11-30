// #yükle keyword-swap

val dir = "/Users/ben/src/kojo/git/kojo/installer/examples/othello"
for (
      file <- List(
        "menu_tr.kojo.installed"
      )
) {
    val frompath = s"$dir/$file"
    val topath = s"$dir/swap_$file"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}
