// calculate Levenshtein distance
// from Anatolii's talk in youtube.
// #ScalaUA - An argument against functional programming
def distance(s1: String, s2: String): Int = {
    val dist = Array.ofDim[Int](s2.length + 1, s1.length + 1)
    for (j <- 0 to s2.length; i <- 0 to s1.length) dist(j)(i) =
        if (j == 0) i
        else if (i == 0) j
        else if (s2(j - 1) == s1(i - 1)) dist(j - 1)(i - 1)
        else (dist(j - 1)(i) min dist(j)(i - 1) min dist(j - 1)(i - 1)) + 1
    dist(s2.length)(s1.length)
}
for (test <- List("" -> "", "a" -> "a")) {
    assert(0 == distance(test._1, test._2))
    assert(0 == distance(test._2, test._1))
}
for (test <- List("a" -> "", "a" -> "b")) {
    assert(1 == distance(test._1, test._2))
    assert(1 == distance(test._2, test._1))
}
for (test <- List("ab" -> "c", "Sunday" -> "Monday")) {
    assert(2 == distance(test._1, test._2))
    assert(2 == distance(test._2, test._1))
}
for (test <- List("abc" -> "d", "Saturday" -> "Sunday")) {
    assert(3 == distance(test._1, test._2))
    assert(3 == distance(test._2, test._1))
}
// Buggy functional style!
def distOld(n1: Iterable[Char], n2: Iterable[Char]): Int = {
    n1.foldLeft(List.range(0, n2.size)) { (prev, x) =>
        (prev zip prev.tail zip n2).scanLeft(prev.head + 1) {
            case (h, ((d, v), y)) => math.min(
                math.min(h + 1, v + 1),
                if (x == y) d else d + 1
            )
        }
    }.last
}
distOld("Sunday", "Saturday")
distOld("Saturday", "Sunday")
distOld("Sunday", "Sunday")
try {
    // 1 / 0
    distOld("", "")
    1
}
catch {
    case e: java.util.NoSuchElementException => println(s"Got: $e")
    case x: RuntimeException                 => println(s"Exception due to $x")
}
println(distOld("s", "s"))
