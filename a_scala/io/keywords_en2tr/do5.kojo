// #yükle keyword-swap

// try/catch used in: ~/src/kojo/git/kojo/src/main/resources/mathgames/tr/make-fractions.kojo

val dir = "/Users/ben/src/kojo/git/kojo/src/main/resources/challenge/tr"
for (
      file <- List(
        "get-started"
      )
) {
    val frompath = s"$dir/$file.kojo"
    val topath = s"$dir/swap/$file.kojo"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}
