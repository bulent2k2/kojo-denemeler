// goldbach's other conjecture that all odd composite numbers can be 
// written as the sum of a prime and twice a square.
// Find the first counterexample.

// #include primes

// odd composite (non-prime) numbers
val ocs = (5 to 5800 by 2).filterNot(isPrime)

// sum of a prime and twice a square, e.g.,
//    9 = 7 + 2 * 1
//   21 = 3 + 2 * 9
val rhs = (for (p <- primes.take(800); n <- 1 to 80) yield p+2*n*n)
    .toSet.filter(_ % 2 == 1)

val find = ocs.filter(!rhs.contains(_)).head // 5777
primeFactors(find).sorted.foreach(println)   // 53 * 109