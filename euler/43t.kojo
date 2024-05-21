// unit tests for p43
// #include 43

val test = 1406357289l
val t2 = 1405678923l // fails for all div

assert(check(digits(test)))
assert(!check(digits(t2)))

for (
    (f, tag) <- List(d2 _, d3 _, d5 _, d7 _, d11 _, d13 _)
        .zip(List(2, 3, 5, 7, 11, 13))
) {
    assert(f(digits(test)), tag.toString)
    assert(!f(digits(t2)), s"$tag t2")
}

println(m17)
println(m17.size)

val m17dpp = m17d.map(_.mkString(""))
println(m17dpp)
assert(rest(digits(1230450)).toSet == Set(6, 7, 8, 9))

val t3 = m17d(10)
println(t3)
println(rest(t3))

println(candidates.size) // 221760

//
// not needed in solution:
//
def hasZero(n: Num) = digits(n).contains(0)
def addZero(n: Num): Digits = 0 :: digits(n)
val all9 = for (i <- 1 to 9) yield i.toByte
def ispd(n: Num) = {
    val ds = digits(n).sorted
    ds == all10 || ds == all9
}

assert(candidates.forall(ds => ispd(d2n(ds))) == true, "all are pandigital")

assert(ispd(9230456781l))
assert(ispd(923456781l)) // implied 0 as the first/most-significant digit

assert(!hasZero(123))
assert(hasZero(103))

assert(addZero(123456789l).head == 0)
