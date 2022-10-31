// do three digits first! And generalize with random search!
// From tiktok channel @mathletters viewed on 2022.04.30
case class Number(a: Int, b: Int, c: Int, d: Int, e: Int) {
    val list0 = List(a, b, c, d, e)
    for (d <- list0) require(d >= 0 && d <= 9)
    val list = list0.sorted.reverse
    val org = list0(0) * 10000 + list0(1) * 1000 + list0(2) * 100 + 10 * list0(3) + list0(4)
    val max = list(0) * 10000 + list(1) * 1000 + list(2) * 100 + 10 * list(3) + list(4)
    val min = list(4) * 10000 + list(3) * 1000 + list(2) * 100 + 10 * list(1) + list(0)
    val next = max - min // this is the iteration
    override def toString = f"$org%5d $max%5d $min%5d $next%5d"
}
object Number {
    def apply(n: Int) = {
        require(n >= 0 && n < 100000)
        val e = n % 10; val r1 = n - e
        val d = (r1 / 10) % 10; val r2 = r1 - d * 10
        val c = (r2 / 100) % 10; val r3 = r2 - c * 100
        val b = (r3 / 1000) % 10; val r4 = r3 - b * 1000
        val a = r4 / 10000
        new Number(a, b, c, d, e)
    }
}
val r = Range(0, 10).toList
def check(a: Int, b: Int, c: Int, d: Int, e: Int) =
    if (a == b && b == c && c == d && d == e) false else true
val all = for (a <- r; b <- r; c <- r; d <- r; e <- r if check(a, b, c, d, e))
    yield Number(a, b, c, d, e)

def getNextNums(n: Number, term: Set[Int], print: Boolean = false): Set[Int] = {
    def pp(x: Number) = if (print) println("   " + x)
    var i = n; pp(i)
    var next = Number(i.next)
    var result = Set(i.next)
    var max = 100
    while (i != next && !term.contains(i.next) && max > 0) {
        i = next; pp(i)
        result = result ++ Set(i.next)
        next = Number(i.next)
        max -= 1
    }
    if (term.contains(i.next)) Set() else result
}
clearOutput()
val next1 = 74943
val set1 = Set(74943)
println(getNextNums(all(0), set1, true))

var search = for (
    l <- all.take(4000) if Set() != getNextNums(l, set1)
) yield l
println(search.size)
var nextSet = set1 ++ Set(53955)
search = for (l <- search if Set() != getNextNums(l, nextSet)) yield l
nextSet = nextSet ++ Set(63954)
search = for (l <- search if Set() != getNextNums(l, nextSet)) yield l
println(search.size)
//println(getNextNums(search(0), nextSet, true))
/*
def filterOut(term: List[Int]) = {
    search = for (l <- search if !term.exists(_ == l.next)) yield l
    val left = search.size
    println(s"Filter out ${leftCnt - left} numbers whose first iter is not in $term. ")
    print(s"Left with $left numbers.")
    leftCnt = left
    if (search.size > 0) {
        println(s" E.g. ${search(0)}")
        iter(search(0), term, false)
    } else println("")
}
filterOut(List(next1))
iter(Number(1), List(next1, 19998), true)
filterOut(List(next1, 9999))
*/
/*
val counts = for (n <- all) yield (n, iter(n))
val longestIter = counts.maxBy(p => p._2)
println(s"Longest iteration count is ${longestIter._2} for:")
iter(longestIter._1, true)
println(f"Average iteration count is ${counts.map(_._2).sum * 1.0 / counts.size}%1.2f")
val sortedIter = counts.sortBy(p => p._2).reverse
val shortest = sortedIter.dropWhile(p => p._2 > 1)
println(s"${shortest.size} numbers require 1 or 0 iterations! E.g. ${shortest(0)._1}")
val kaprekar = 6174
val search1 = for (s <- shortest if s._1.next != kaprekar) yield s._1
assert(search1.size == 0)
println(s"There are ${shortest.size} numbers with iterCount < 2. And they all get the Kaprekar number $kaprekar in the first iter.")
val longest = sortedIter.takeWhile(p => p._2 == 7)
println(s"${longest.size} numbers require 7 iterations! E.g. ${longest(0)._1}")

val count1 = longest.size - leftCnt
println(s"$count1 of those get $next1 in the first iter. \nFilter those out and we get ${search2.size}: E.g. ${search2(0)._1}")
iter(search2(0)._1, false)

filterOut(5085) // removing numbers that get 5085 in first iter, 1272 remains
filterOut(6084) // 792 remains
filterOut(5175) // 432 remains
filterOut(9351) // 312 remains
filterOut(8442) // 168 remains
filterOut(9441) // 72 remains
filterOut(9531) // 0 remains
println("Here is how we got to zero:\n2184 - 432 - 480 - 480 - 360 - 120 - 144 - 96 - 72")
println(s"with first next numbers: $next1 5085 6084 5175 9351 8442 9441 9531")
val i6 = sortedIter.filter(p => p._2 == 6)
println(s"${i6.size} numbers require 6 iterations. E.g. ${i6(0)._1}")
search2 = i6; leftCnt = search2.size
for(next <- List(2997, 4995, 5994, 7992, 2178, 8172, 3267, 7263, 7623, 8712)) filterOut(next)
search2 = sortedIter.filter(p => p._2 == 5); leftCnt = search2.size
println(s"${search2.size} numbers require 5 iterations. E.g., ${search2(0)._1}")
for(next <- List(999, 3177, 7173, 5265, 8262, 5355, 5445, 6444, 7443, 8622)) filterOut(next)
search2 = sortedIter.filter(p => p._2 == 4); leftCnt = search2.size
println(s"${search2.size} numbers require 4 iterations. E.g., ${search2(0)._1}")
for(next <- List(1998, 3996, 6993, 8991, 1089, 9081, 4356, 6354, 6534, 9801)) filterOut(next)
var numIter = 3
search2 = sortedIter.filter(p => p._2 == numIter); leftCnt = search2.size
println(s"${search2.size} numbers require $numIter iterations. E.g., ${search2(0)._1}")
for(next <- List(2088, 8082, 3087, 7083, 9171, 9711, 4266, 6264, 9261, 9621, 7353, 7533)) filterOut(next)
numIter = 2
search2 = sortedIter.filter(p => p._2 == numIter); leftCnt = search2.size
println(s"${search2.size} numbers require $numIter iterations. E.g., ${search2(0)._1}")
for(next <- List(4176, 8352, 8532)) filterOut(next)
numIter = 1
search2 = sortedIter.filter(p => p._2 == numIter); leftCnt = search2.size
println(s"${search2.size} numbers require $numIter iterations. E.g., ${search2(0)._1}")
for(next <- List(kaprekar)) filterOut(next)
*/
