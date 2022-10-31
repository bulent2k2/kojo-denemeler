// From tiktok channel @mathletters viewed on 2022.04.30
case class Number(a: Int, b: Int, c: Int, d: Int) {
    val list0 = List(a, b, c, d)
    for (d <- list0) require(d >= 0 && d <= 9)
    val list = list0.sorted.reverse
    val org = list0(0) * 1000 + list0(1) * 100 + list0(2) * 10 + list0(3)
    val max = list(0) * 1000 + list(1) * 100 + list(2) * 10 + list(3)
    val min = list(3) * 1000 + list(2) * 100 + list(1) * 10 + list(0)
    val next = max - min // this is the iteration
    override def toString = f"$org%4d $max%4d $min%4d $next%4d"
}
object Number {
    def apply(n: Int) = {
        require(n >= 0 && n < 10000)
        val d = n % 10; val r1 = n-d
        val c = (r1/10) % 10; val r2 = r1-c*10
        val b = (r2/100) % 10; val r3 = r2-b*100
        val a = r3/1000
        new Number(a, b, c, d)
    }
}
val r = Range(0, 10).toList
def check(a: Int, b: Int, c: Int, d: Int) =
    if (a == b && b == c && c == d) false else true
val all = for (a <- r; b <- r; c <- r; d <- r if check(a, b, c, d))
    yield Number(a, b, c, d)

def iter(n: Number, print: Boolean = false) = {
    def pp(x: Number) = if (print) println("   " + x)
    var i = n; pp(i)
    var next = Number(i.next)
    var max = 10000
    while(i != next && max > 0) {
        i = next; pp(i)
        next = Number(i.next)
        max -= 1
    }
    10000 - max
}
clearOutput()
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
val next1 = 4086
var search2 = for (l <- longest if l._1.next != next1) yield l
var leftCnt = search2.size
val count1 = longest.size - leftCnt
println(s"$count1 of those get $next1 in the first iter. \nFilter those out and we get ${search2.size}: E.g. ${search2(0)._1}")
iter(search2(0)._1, false)
def filterOut(next: Int) = {
    search2 = for (l <- search2 if l._1.next != next) yield l
    val left = search2.size
    println(s"Filter out ${leftCnt - left} numbers whose first iter is not $next. ")
    print(s"Left with $left numbers.")
    leftCnt = left
    if (search2.size > 0) {
        println(s" E.g. ${search2(0)._1}")
        iter(search2(0)._1, false) 
    } else println("")
}
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