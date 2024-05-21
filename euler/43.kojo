// sub-string divisibility
type Num = Long
type Nums = List[Num]
type Digits = List[Byte]

// we will guarantee div by 17 by construction
def check(ds: Digits): Boolean =
    d2(ds) && d3(ds) && d5(ds) && d7(ds) && d11(ds) && d13(ds)
def d2(ds: Digits): Boolean = d2n(ds.drop(1).take(3)) % 2 == 0
def d3(ds: Digits): Boolean = d2n(ds.drop(2).take(3)) % 3 == 0
def d5(ds: Digits): Boolean = d2n(ds.drop(3).take(3)) % 5 == 0
def d7(ds: Digits): Boolean = d2n(ds.drop(4).take(3)) % 7 == 0
def d11(ds: Digits): Boolean = d2n(ds.drop(5).take(3)) % 11 == 0
def d13(ds: Digits): Boolean = d2n(ds.drop(6).take(3)) % 13 == 0

// multiples of 17 without duplicate digits
val m17 = (17 to 999 by 17).filter { n =>
    val ds = digits(n)
    ds.size == ds.distinct.size
}
// make sure we have 3 digits for div by 17
val m17d = m17.map { n =>
    val ds = digits(n)
    if (ds.size == 3) ds else 0.toByte :: ds
}

// given some digits, find all the other digits to make up a pandigital num
def rest(ds: Digits): Digits = (all10.toSet -- ds.toSet).toList
val all10 = for (i <- 0 to 9) yield i.toByte

val candidates = for (
    dsTail <- m17d;
    dsHead <- rest(dsTail).permutations
) yield (dsHead ++ dsTail)
val wanted = candidates.filter(ds => check(ds))
wanted.foreach(ds => println(d2n(ds)))
println(wanted.map(d2n(_)).sum) // 16695334890

def digits(n: Num): Digits =
    if (n < 10) List(n.toByte)
    else ((n % 10).toByte :: digits(n / 10).reverse).reverse
def d2n(ds: Digits): Num = ds.reverse.zip(powers(ds.size)).map { case (d, p) => d * p }.sum
def powers(n: Int): IndexedSeq[Num] = for (p <- 0 until n) yield (math.pow(10, p).toLong) // TODO: toNum
