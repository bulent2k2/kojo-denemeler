// PE 40 Champernowne's constant
// http://ikojo.in/sf/QSNVPoQ/8

// an irrational number, 0.123..91011...99100101...
// we want to find digits 1, 10, 100, .. 1e6
def totaldc(nd: Int) = (1 to nd) // digit count if we go up to nd-digit numbers
  .zip(List(9, 90, 900, 9000, 90000, 900000))
  .map{ case(c, num) => c * num }.sum
assert(totaldc(6) > 1e6)
// for(x <- 1 to 6) println(f"$x: ${math.pow(10,x)}%7s starts at digit ${totaldc(x)+1}%7d")
// All of the above is interesting but 
// the solution needs only the following:
val ds = (0 to 190000).flatMap(digits)
assert(ds.size > 1e6)
val wanted = powers(7).map(ds(_).toInt).product
println(wanted)

type Digits = List[Byte]
def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else ((n%10).toByte::digits(n/10).reverse).reverse
def d2n(ds: Digits): Int = ds.reverse.zip(powers(ds.size)).map{case (d, p) => d*p}.sum
def powers(n: Int) = for(p <- 0 until n) yield(math.pow(10, p).toInt)
