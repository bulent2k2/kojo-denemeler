// #yükle keyword-swap

// try/catch used in: ~/src/kojo/git/kojo/src/main/resources/mathgames/tr/make-fractions.kojo

val dir = "/Users/ben/src/kojo/git/kojo/src/main/resources/mathgames/tr"
for (
      file <- List(
        "counting", "identify-fractions", "make-fractions"
      )
) {
    val frompath = s"$dir/$file.kojo"
    val topath = s"$dir/swap/$file.kojo"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}
