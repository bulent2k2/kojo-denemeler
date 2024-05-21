// Project Euler 36 Double-base palindromes 
// http://ikojo.in/sf/IPRTcDN/4

// Find palindrome numbers in decimal and binary
type Bits = List[Boolean]
def binary(n: Int): Bits =
  if (n < 2) List(n == 1)
  else ((n%2 == 1)::binary(n/2).reverse).reverse
type Digits = List[Byte]
def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else ((n%10).toByte::digits(n/10).reverse).reverse
def palin(n: Int) = {
  val ds = digits(n)
  if (ds != ds.reverse) false
  else {
    val bin = binary(n)
    bin == bin.reverse
  }
}
println(binary(585).map(b => if (b) 1 else 0).mkString(""))
val ps = for (n <- 1 until 1000000 if palin(n)) yield(n)
println(ps.sum, ps)
