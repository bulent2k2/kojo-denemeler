type Nums = List[Int]
type Digits = List[Byte]

val primes: Stream[Int] = 2 #:: Stream.from(3, 2).filter {isPrime}
def isPrime(n: Int) = if (n < 2) false else primeFactors(n) == List(n)
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

def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else ((n%10).toByte::digits(n/10).reverse).reverse
def d2n(ds: Digits): Int = ds.reverse.zip(powers(ds.size)).map{case (d, p) => d*p}.sum
def powers(n: Int) = for(p <- 0 until n) yield(math.pow(10, p).toInt)