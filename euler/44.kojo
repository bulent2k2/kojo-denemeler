// pentagonal numbers
def p(n: Int): Long = n * (3 * n - 1) / 2
val ps = (1 to 2200).map(p)

def n(p: Long) = math.round((1 + math.sqrt(24 * p + 1))/6).toInt
def isp(p: Long) = {
    /* use quadratic formula
    0 == 3 * n * n - n - 2 * p 
      => a = 3, b = -1 c = -2p
      => n = (-b +- sqrt(b*b - 4ac)) / 2a 
             (1 + sqrt(1 + 24 * p))/6      */ 
    val x = n(p)
    x * (3 * x - 1) / 2 == p
}
assert(ps.forall(isp(_)))
val nps = (1 to 100).map(_.toLong).toSet -- ps.toSet
assert(nps.forall(!isp(_)))

val candidates = for (p1 <- ps; 
    p2 <- ps if p2 > p1 && isp(p2 - p1) && isp(p1 + p2)
    ) yield (p1, p2, p2 - p1)
println(candidates) // p1=1560090(n=1020) p2=7042750(n=2167) d=5482660
println(candidates.map{ case (p1, p2, d) => d }.min)