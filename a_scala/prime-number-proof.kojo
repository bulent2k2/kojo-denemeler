// from: https://scastie.scala-lang.org/bulent2k2/B8mDPwDaQAO3vDmrS32Zww/45

// here is the proof that there are infinitely many primes!
import scala.collection.immutable.LazyList // I love lazy lists..

// this is the easiest way to find all the primes :-)
val primes: LazyList[BigInt] = BigInt(2) #:: LazyList.from(3, 2).map(BigInt(_)).filter { isPrime }
def isPrime(n: BigInt) = primes.takeWhile { i => i * i <= n }.forall { p => n % p != 0 }
// If you are puzzled, here are the first 20 of them (sorry, we can't print them all. You know why!):
val max = 18 // 19 takes too long!
println(primes.take(max).toList)

// here are the sets of the first few primes in increasing number of primes
val sets = for (countOfFirstPrimes <- 1 to max) yield primes.take(countOfFirstPrimes).toList
var i = 1; sets.foreach { t =>
    println(s"$i $t")
    i += 1
}
// let's see what happens when we multiply all the primes in each set and add 1.
// we know that the result is not divisible by any of the primes. Does that prove that we have a larger prime?
// If that's the case, we could say that there are infinitely many primes.
// But, is that the case?
// Let's get the computer to do the hard work of multiplying all those numbers
i = 1; sets.foreach { setOfPrimes =>
    val productPlusOne = setOfPrimes.reduce(_ * _) + 1
    println(s"$i $productPlusOne"); i += 1
}
// Well, 3 and 7 are definitely primes. You might know that 31 is also a prime. How about 211?
// The computer is our friend (how did the ancients do this? By hand of course!)
// Let's ask the computer to check:
val candidates = for (t <- sets) yield t.reduce(_ * _) + 1
println(candidates)
i = 1; candidates.map(isPrime).foreach(x => { println(s"$i $x"); i += 1 })
// They are all primes except the last one! That's why we did this for the first six sets of primes.
// The last one shows that things are not as simple as we originally thought. But, despair not!
// Let's see what's going on with 30031, which, pray remember, is the product of 2, 3, 5, 7, 11 and 13 plus 1!
println(candidates.last)
// We could ask the computer to find the prime factors of 30031, but
// to get to our proof, we don't even need to! There must be at least two prime numbers greater than 13
// that is a factor of 30031. So, we do know that there are prime numbers larger than 13! And,
// because we can do this for any set of consecutive primes and get the same results, meaning
// their product plus one would either be a prime, or its prime factors would give us even larger
// primes! And that completes our proof :-)
// If you are curious, just ask google: "prime factors of 30031." It tells me that they are 59 and 509.
println(isPrime(59).toString + " and " + isPrime(509).toString)
// Yep!
// To make sure:
println(2 * 3 * 5 * 7 * 11 * 13)
println(isPrime(BigInt("200560490131")))
val notPrimeTooBig = BigInt("117288381359406970983287")
isPrime(notPrimeTooBig)
val count = 12
val notPrime = primes.take(count).reduce(_ * _) + 1
val delta = 60
val biggerPrime = notPrime + delta
println(s"The biggest prime I found so far: $biggerPrime \nwhich is the product of the first $count primes + ${delta + 1}")
/* takes 6 seconds!
timeit { println(s"Is it really prime? ${isPrime(biggerPrime)}") }
*/
// it is: 7_420_738_134_871   7.42e12 more than 7 trillion
def findNextPrime(start: BigInt, rangeToSearch: Int): List[BigInt] = {
    require(start % 2 == 1)
    for (
        x <- Range(2, rangeToSearch, 2).toList if isPrime(start + x)
    ) yield x
}
// found it using:
// timeit { findNextPrime(notPrime, 100).foreach(println) }
// The next line finds 11 primes! 
// timeit { println(findNextPrime(biggerPrime, 200)) }
// The deltas: List(40, 42, 66, 70, 88, 96, 106, 112, 168, 178, 196)
val bP2 = biggerPrime + 196
def showIt(p: BigInt) = println(s"An even bigger one: $p. Is it prime: ${isPrime(p)}")
// Even more (in .3 seconds!): List(50, 56, 74, 102, 116, 126, 174, 182)
//timeit { println(findNextPrime(bP2, 200)) }
val bP3 = bP2 + 182
showIt(bP3)
val notP = primes.take(13).reduce(_ * _ ) + 1
// took 72 seconds:
// timeit { println(findNextPrime(notP, 200)) }
/* 
The biggest prime I found so far: 7420738134871 
which is the product of the first 12 primes + 61
An even bigger one: 7420738135249. Is it prime: true
List(70, 106, 150, 190)
Timed code took 72.197 seconds
*/ 
// takes 71 seconds:
//   timeit { showIt(notP + 190) }
// but found it: 
// An even bigger one: 304250263527401. Is it prime: true
// 304_250_263_527_401  more than 304 trillion 3e14 :-)
// Gotta check out Mersenne numbers :-)
