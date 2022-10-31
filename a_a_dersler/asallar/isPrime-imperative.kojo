// used in jnotebook: ~/src/scala/almond/Cheatsheet.ipynb
def isPrime(n: Int): Boolean = n match {
    case 2 => true
    case _ =>
        if (n <= 1 || n % 2 == 0) false
        else {
            var c = 3
            while (c <= math.sqrt(n).toInt + 1)
                if (n % c == 0) {
                    println(c)
                    return false
                }
                else {
                    c = c + 2
                }
            true
        }
} //> def isPrime(n: Int): Boolean
isPrime(119) //> val res10: Boolean = false
isPrime(7 * 17 * 2 + 1) //> val res11: Boolean = true
2 * 7 * 17 + 1 //> val res12: Int = 239