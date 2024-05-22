// #include primes

// 1487, 4817, 8147 is an arithmetic sequence of primes (because:
//   1487 + 3330 = 4817
//   4817 + 3330 = 8147)

primes.dropWhile(_ < 1000).takeWhile(_ <= 3300).map { p =>
    allTriples(digits(p).permutations.map(d2n).toList.filter(isPrime))
        .filter { case (a, b, c) => a < b && b < c && b - a == c - b }
}.filter(_.size > 0).distinct.foreach(l => println(l.mkString))

def allTriples(ns: Nums): List[(Int, Int, Int)] =
    if (ns.size < 3) Nil
    else for (a <- ns; b <- ns if b != a; c <- ns if c != b)
        yield (a, b, c)

/*
(163,613,1063)
(1487,4817,8147)
(2969,6299,9629)
(379,3709,7039)
 */
// triple: 2969, 6299, 9629
// answer: 296962999629
