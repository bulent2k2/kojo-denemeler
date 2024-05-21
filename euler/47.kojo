// #include primes

// number of unique prime factors
def numpf(n: Int) = primeFactors(n).distinct.size
// find c consecutive numbers with c prime factors
def find(n: Int, c: Int) = (n until n + c).forall(numpf(_) == c)
for ((c, bound) <- List((2, 20), (3, 700), (4, 135000)))
    (4 until bound).filter(find(_, c)).foreach(x => println(s"$c -> $x")) // find and print what we are looking for
println("====== =============")
println("Number Prime Factors")
println("====== =============")
(for (
    ns <- List((0 to 1).map(14 + _), (0 to 2).map(644 + _), (0 to 3).map(134043 + _));
    n <- ns
) yield (n, primeFactors(n))).foreach { case (n, pfs) => println(f"$n%6d ${pfs.mkString("{", " ", "}")}") } // show the prime factors