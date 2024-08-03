// scala 3 code that runs on scastie.scala-lang.org

type Nums = List[Int]
type Digits = List[Byte]

def primeFactors(x: Int): Nums =
  // see: https://stackoverflow.com/questions/30280524/scala-way-to-generate-prime-factors-of-a-number
  @annotation.tailrec
  def pf(x: Int, a: Int, list: Nums): Nums = a * a > x match
    case false if x % a == 0 => pf(x / a, a, a :: list)
    case false               => pf(x, a + 1, list)
    case true                => x :: list
  pf(x, 2, Nil)

def isPrime(n: Int) = if (n < 2) false else primeFactors(n) == List(n)
val primes: LazyList[Int] = 2 #:: LazyList.from(3, 2).filter { isPrime }

def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else ((n % 10).toByte :: digits(n / 10).reverse).reverse
def d2n(ds: Digits): Int = // from digits back to number
  ds.reverse.zip(powers(ds.size)).map { case (d, p) => d * p }.sum
def powers(n: Int) = for (p <- 0 until n) yield (math.pow(10, p).toInt)

// replace all of the specified digits (ix) of the number n by the digit d
def replace(n: Int, ix: List[Byte], d: Byte) =
  val ds = digits(n)
  val ixs = ix.toSet
  d2n(
    (for (i <- 0 until ds.size)
      yield if (ixs.contains(i.toByte)) d else ds(i)).toList
  )

// get all possible indices for 3-digit replacements for a d-digit number
// Note: we can't use the least significant digit (div by 2), and optionally use the most significant digit (and then don't replace with 0)
def ix3(d: Byte, msd: Byte = 0) = for (
  i1 <- msd.toInt to d - 2;
  i2 <- i1 + 1 to d - 2;
  i3 <- i2 + 1 to d - 2
) yield List(i1, i2, i3).map(_.toByte)

def enum6(p: Int) = // enumerate all possible replacements for the given 6-digit prime p
  for (ix <- ix3(6, 0)) yield
    val ps = (for (d <- 1 to 9) yield replace(p, ix, d.toByte)).filter(isPrime)
    (ix, ps.size, ps)
// Did a parallel run to show that using 0 as a replacement digit doesn't improve the result

// there are 68906 six digit primes. Only 2142 of them are < 125000
val ps6 = primes
  .dropWhile(_ < 100000)
  .takeWhile(_ < 200000) // full search to 1 million takes about 10 sec
val best = (for (p <- ps6) yield enum6(p).sortBy { case (_, size, _) =>
  size
}.last).sortBy { case (_, size, _) => size }.last
println(best(2).mkString(", "))
