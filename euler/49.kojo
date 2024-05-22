// #include primes

// 1487, 4817, 8147 is an arithmetic sequence of primes (because:
//   1487 + 3330 = 4817
//   4817 + 3330 = 8147)

// see earlier solutions in 49a and 49b
clearOutput()

// three primes for testing: List(1009, 1487, 2969)
primes.dropWhile(_ < 1000).takeWhile(_ <= 3300)
    .map { p => digits(p).permutations.map(d2n).filter(n => n > 1000 && isPrime(n)).toList }
    .filter(_.size > 2).flatMap { _.sorted.combinations(3).filter(t => t(1) - t(0) == t(2) - t(1)).toList }
    .filter(_.size > 0).distinct.foreach(println)

/*
    List(1487, 4817, 8147)
    List(2969, 6299, 9629)
 */
// triple: 2969, 6299, 9629
// answer: 296962999629