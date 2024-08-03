// #include primes

clearOutput()

// replace all of the specified digits (ix) of the number n by the digit d
def replace(n: Int, ix: List[Byte], d: Byte) = {
    val ds = digits(n)
    val ixs = ix.toSet
    val nds = for (i <- 0 until ds.size) yield if (ixs.contains(i.toByte)) d else ds(i)
    d2n(nds.toList)
}

// get all possible indices for 3-digit replacements for a d-digit number
// Note: we can't use the least significant digit (div by 2), and optionally use the most significant digit (and then don't replace with 0)
def ix3(d: Byte, msd: Byte = 0) = for (
    i1 <- msd until d - 1;
    i2 <- i1 + 1 until d - 1;
    i3 <- i2 + 1 until d - 1
) yield List(i1, i2, i3).map(_.toByte)

def enum6(p: Int) = (for (ix <- ix3(6, 0)) yield {
    val ps = (for (d <- 1 to 9) yield replace(p, ix, d.toByte)).filter(isPrime)
    (ix, ps.size, ps)
})

if (true) {
    // there are 68906 six digit primes. Only 2142 of them are < 125000
    val ps6 = primes.dropWhile(_ < 100000).takeWhile(_ < 125000) // full search to 1 million takes about 10 sec
    val pick6 = (for (p <- ps6) yield enum6(p).sortBy { case (_, size, _) => size }.last).sortBy { case (_, size, _) => size }.last
    println(s"answer:\n $pick6")
}

val answer = 121313
enum6(answer).foreach { case (ix, size, ps) => println(s"replacing digits ${ix.mkString(",")} gives us $size prime${if (size > 1) "s" else " "}: ${ps.mkString(", ")}") }
// Got it! This time we needed to replace the most-significant bit!
//   (List(0, 2, 4),8,Vector(121313, 222323, 323333, 424343, 525353, 626363, 828383, 929393))
// By the way, without replacing the first digit, the best we get is: 
//   (List(1, 2, 3),7,Vector(900061, 911161, 922261, 966661, 977761, 988861, 999961))