// copied from primes.kojo
type Digits = List[Byte]
def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else ((n%10).toByte::digits(n/10).reverse).reverse

def permuted(n1: Int, n2: Int) = digits(n1).sorted == digits(n2).sorted

assert(permuted(123, 231))

val test = 125874

assert(permuted(test, 2*test))

def multiPermuted(n: Int) = 
    permuted(n, 2*n) &&
    permuted(n, 3*n) &&
    permuted(n, 4*n) &&
    permuted(n, 5*n) &&
    permuted(n, 6*n)

val search = for(n <- 1 to 1000000 if multiPermuted(n)) yield n
// println(search)

// Yöney(142857)

// Note: has no multiple of 3 including 0!
val answer = 142857
(2 to 6).map(_ * answer).foreach(println) // seems to work :-)
