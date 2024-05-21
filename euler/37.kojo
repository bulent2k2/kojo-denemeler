// PE 37 Truncatable Primes
// http://ikojo.in/sf/S9TRYoK/7

// Find truncatable primes, from both sides, like 3797: 797 97 7, 379 37 3

val primes: Stream[Int] = 2 #:: Stream.from(3, 2).filter {isPrime}
def isPrime(n: Int) = if (n < 2) false else primeFactors(n) == List(n)

type Nums = List[Int]
def primeFactors(x: Int): Nums = {
  // see: https://stackoverflow.com/questions/30280524/scala-way-to-generate-prime-factors-of-a-number
  @annotation.tailrec
  def pf(x: Int, a: Int, list: Nums): Nums = a*a > x match {
    case false if x % a == 0 => pf(x / a, a    , a :: list)
    case false               => pf(x    , a + 1, list)
    case true                => x :: list
  }
  pf(x, 2, Nil)
}

def istp(n: Int) = n>7 && isPrime(n) && bros(n).forall(isPrime(_))
assert(istp(3797))

// abc -> bc c ab a
def bros(n: Int): Nums = {
  val ds = digits(n)
  (for(n <- 1 until ds.size) yield ds take n).map(d2n).toList ++
  (for(n <- 1 until ds.size) yield ds.reverse take n).map(x=>d2n(x.reverse)).toList
}

type Digits = List[Byte]
def digits(n: Int): Digits = if (n < 10) List(n.toByte)
  else digits(n/10) ++ List((n%10).toByte)
def d2n(ds: Digits): Int = ds.reverse.zip(powers(ds.size)).map {
    case (d, p) => d*p
  }.sum
def powers(n: Int) = for(p <- 0 until n) yield(math.pow(10, p).toInt)
val ts =primes.takeWhile(_<740123).filter(istp) // used 1 million originally
println(ts.sum, ts)
