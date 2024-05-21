// PE 41 Pandigital Primes
// http://ikojo.in/sf/BKpZImD/7

// find the largest pandigital prime
// Note: it can't have 3 or fewer digits. Why?
// Also, 8 and 9 digit pandigitals can't be prime, either!
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
val ds = (1 to 7).map(_.toByte).toList.permutations
// finding the size consumes the iterator!
// println(ds.size) 7! = 5040
val solution = ds.map(d2n).filter(isPrime).max
println(solution)

// this solution is slower and takes a few seconds:
//primes.dropWhile(_<7123456).takeWhile(_<7654322).filter(ispd).foreach(println)

def ispdp(n: Int) = ispd(n)&&isPrime(n)
assert(ispdp(2143)==true)
assert(ispdp(23)==false)
def ispd(n: Int) = { // is n pan-digital?
  val ds0=digits(n)
  val ds = ds0.sorted.distinct
  ds0.size == ds.size && ds.head == 1 && ds.size == ds.reverse.head
}
assert(ispd(2413)==true)
assert(ispd(112)==false)
assert(ispd(103)==false)
/*1423 2143 2341 4231
1234657 1245763 1246537 1246573 1247563 1254367 ..
2136457 ..
..
7652413
*/
def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else digits(n/10) ++ List((n%10).toByte) 
def d2n(ds: Digits): Int = ds.reverse.zip(powers(ds.size)).map{case (d, p) => d*p}.sum
def powers(n: Int) = for(p <- 0 until n) yield(math.pow(10, p).toInt)
