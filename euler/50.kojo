// #include primes

// longest series of consecutive primes that add up to a prime less than x = 100 has six terms: 2 + 3 + 5 + 7 + 11 + 13 = p = 41
// For x = 1000, p = 953 with 21 terms
// What is p for x = one million

clearOutput()

def findIt(max: Int) = {
    def psGTE(n: Int) = primes.dropWhile(_ < n).takeWhile(_ < max / 5)  // primes greater than or equal to n

    (for (
        p <- primes.takeWhile(_ < 20);
        c <- 1 until (math.min(psGTE(p).size, 600)) if isPrime(psGTE(p).take(c).sum)
    ) yield (psGTE(p).take(c).sum, c))
        .filter { case (sum, size) => sum < max }
        .sortBy { case (sum, size) => size }.last._1
}

for (max <- List(100, 1000, 1000000)) println(f"$max%7d ${findIt(max)}%7d")