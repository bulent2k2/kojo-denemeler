// #include primes

// 1487, 4817, 8147 is an arithmetic sequence of primes (because:
//   1487 + 3330 = 4817
//   4817 + 3330 = 8147)

for (p <- primes.dropWhile(_ < 1000).takeWhile(_ <= 9999)) {
    val primesOfP = digits(p).permutations.toList.filter {
        perm => val num = d2n(perm); num > 1000 && isPrime(num)
    }.map(d2n).sorted
    if (primesOfP.size > 2) { 
        val ps = primesOfP.toList
        val gs = gaps(ps)
        if (seq(gs) || p == 1487) println(s"$p $ps $gs")
    }
}

def seq(gs: Nums): Boolean = // is there a consecutive repeat?
    if (gs.size < 2) false
    else gs(0) == gs(1) || seq(gs.tail)

def gaps(ns: Nums): Nums = // delta between consecutive numbers
    if (ns.size < 2) List() else ns(1) - ns(0) :: gaps(ns.tail)

/* Finds the answer, but misses the other solution!
 2699 List(2699, 2969, 6299, 9629) List(270, 3330, 3330)
 2969 List(2699, 2969, 6299, 9629) List(270, 3330, 3330)
 6299 List(2699, 2969, 6299, 9629) List(270, 3330, 3330)
 9629 List(2699, 2969, 6299, 9629) List(270, 3330, 3330)
 */
// triple: 2969, 6299, 9629
// answer: 296962999629

/* Note: 
 1487 List(1487, 1847, 4817, 4871, 7481, 7841, 8147, 8741) List(360, 2970, 54, 2610, 360, 306, 594)
*/
assert(360 + 2970 == 54 + 2610 + 360 + 306, "foo")
