// Project Euler 35 Circular Primes
// http://ikojo.in/sf/lQ2ty5F/12

// Find circular primes like 13 31, 17 71, 113 131 311, or 197 971 719
// There are only 55 of them that are less than 1 million
val primes: Stream[Int] = 2 #:: Stream.from(3, 2).filter {isPrime}

def isPrime(n: Int) = if (n < 2) false else primeFactors(n) == List(n)
type Digits = List[Byte]
type Nums = List[Int]
def primeFactors(x: Int): Nums = {
  // see: https://stackoverflow.com/questions/30280524/scala-way-to-generate-prime-factors-of-a-number
  @annotation.tailrec
  def pf(x: Int, a: Int = 2, list: Nums = Nil): Nums = a*a > x match {
    case false if x % a == 0 => pf(x / a, a    , a :: list)
    case false               => pf(x    , a + 1, list)
    case true                => x :: list
  }
  pf(x)
}

def iscp(n: Int) = isPrime(n) && !bros(n).exists(!isPrime(_))
assert(iscp(13) && iscp(17) && iscp(113) && iscp(197))

// abc -> bca cab
def bros(n: Int): Nums = {
  var ds = digits(n)
  val bros: ArrayBuffer[Int] = ArrayBuffer()
  for (i <- 1 until ds.size) {
    ds = rotate(ds)
    bros.append(d2n(ds))
  }
  bros.toList
}
def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else digits(n/10) ++ List((n%10).toByte)
def d2n(ds: Digits): Int =
  ds.reverse.zip(powers(ds.size)).map {
    case (d, p) => d*p
  }.sum
def powers(n: Int) =
  for(p <- 0 until n) yield(math.pow(10, p).toInt)
def rotate(ds: Digits): Digits = ds.tail ++ List(ds.head)
val cps = primes.takeWhile(_<1000000).filter(iscp)
println(cps.size, cps) // 55
