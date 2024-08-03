// #include primes

val (sp2, snp2) = (List(13, 23, 43, 53, 73, 83), List(33, 63, 93)) // 2-digit sibling primes
val (sp5, snp5) = (List(56003, 56113, 56333, 56443, 56663, 56773, 56993), List(56223, 56553, 56883)) // 5-digit sibling primes
// 13 has 5 siblings, 56003 has 6 siblings
/* find the smallest n-digit prime which has 7 siblings when part of
   the number (not necessarily adjacent digits) is replaced by the same digit */

for (p <- sp2) assert(isPrime(p), p); for (np <- snp2) assert(!isPrime(np), np); assert(sp2.size + snp2.size == 9, "size2")
for (p <- sp5) assert(isPrime(p), p); for (np <- snp5) assert(!isPrime(np), np); assert(sp5.size + snp5.size == 10, "size5")

clearOutput()

def replace(n: Int, ix: Digits, d: Byte) = {
    val ds = digits(n)
    val ixs = ix.toSet
    val nds = for (i <- 0 until ds.size) yield if (ixs.contains(i.toByte)) d else ds(i)
    d2n(nds.toList)
}

assert(replace(135, List(1, 2), 0) == 100)
assert(replace(135, List(0, 2), 9) == 939)

// get all possible indices for 3-digit replacements for an d-digit number
// Note: we can't use the least significant digit (div by 2), and optionally use the most significant digit (and then don't replace with 0)
def ix3(d: Byte, msd: Byte = 0) = for (
    i1 <- msd until d - 1;
    i2 <- i1 + 1 until d - 1;
    i3 <- i2 + 1 until d - 1
) yield List(i1, i2, i3).map(_.toByte)

def ix2(d: Byte, msd: Byte = 0) = for (
    i1 <- msd until d - 1;
    i2 <- i1 + 1 to d - 1
) yield List(i1, i2).map(_.toByte)

println(ix2(5, 1))
println(ix3(5, 0))
println(ix3(5, 1))

val ps5 = primes.dropWhile(_ < 10000).takeWhile(_ < 100000)
ps5.size // there are 1061 4 digit primes
val p0 = 56003
val p1 = ps5.head
val p2 = ps5.last
println(p1, p2, ps5.size)

def enum5(p: Int) = (for (ix <- ix2(5, 1)) yield { // without replacing the most-significant digit
    val ps = (for (d <- 0 to 9) yield replace(p, ix, d.toByte)).filter(isPrime)
    (ix, ps.size, ps)
})
def enum5b(p: Int) = (for (ix <- ix2(5, 0)) yield { // replacing the most-significant digit
    val ps = (for (d <- 1 to 9) yield replace(p, ix, d.toByte)).filter(isPrime)
    (ix, ps.size, ps)
})
println(enum5(p0).sortBy{ case (_, size, _) => size }.last)
enum5(p0).foreach { case (ix, size, ps) => println(s"replacing digits ${ix.mkString(",")} gives us $size prime${if(size > 1) "s" else " "}: ${ps.mkString(", ")}") }
val pick = (for (p <- ps5) yield enum5(p).sortBy{ case (_, size, _) => size }.last).sortBy{ case (_, size, _) => size }.last
println(pick)
val pickb = (for (p <- ps5) yield enum5b(p).sortBy{ case (_, size, _) => size }.last).sortBy{ case (_, size, _) => size }.last
println(pickb)

val ps6 = primes.dropWhile(_ < 100000).takeWhile(_ < 1000000)
println(ps6.size)
val (p3, p4) = (ps6.head, ps6.last)
def enum6(p: Int) = (for (ix <- ix3(6, 1)) yield {
    val ps = (for (d <- 0 to 9) yield replace(p, ix, d.toByte)).filter(isPrime)
    (ix, ps.size, ps)
})
def enum6b(p: Int) = (for (ix <- ix3(6, 0)) yield {
    val ps = (for (d <- 1 to 9) yield replace(p, ix, d.toByte)).filter(isPrime)
    (ix, ps.size, ps)
})
enum6(p3).foreach { case (ix, size, ps) => println(s"replacing digits ${ix.mkString(",")} gives us $size prime${if(size > 1) "s" else " "}: ${ps.mkString(", ")}") }
val pick6 = (for (p <- ps6) yield enum6b(p).sortBy{ case (_, size, _) => size }.last).sortBy{ case (_, size, _) => size }.last
println(pick6) // (List(1, 2, 3),7,Vector(900061, 911161, 922261, 966661, 977761, 988861, 999961))
// Got it! This time we needed to replace the most-significant bit!
// (List(0, 2, 4),8,Vector(121313, 222323, 323333, 424343, 525353, 626363, 828383, 929393))
