// First done in ikojo: http://ikojo.in/sf/MyPvfnp/1

def fac(n: Int): BigInt = if (n < 2) 1 else n * fac(n - 1)
def seqUnq(n: Int, k: Int): BigInt = if (k < 1) 1 else if (k < 2) n else n * seqUnq(n - 1, k - 1)
def colUnq(n: Int, k: Int): BigInt = seqUnq(n, k) / fac(k)
val choose = colUnq _  // choose k from n -- equivalently choosing the other n-k, that's why it is known as binomial
val bc = colUnq _ // binomial coefficients -- the kth coefficient in the expansion of (a+b)^n, for all k=0 to n
val count_pe53 = (for (n <- 1 to 100; 
                       r <- 1 to n if bc(n, r) > 1000000) yield 1).size
println(count_pe53)