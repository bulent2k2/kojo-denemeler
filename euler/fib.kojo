//object Euler2 extends App {
type Num = BigInt

def fibs(a: Num, b: Num): Stream[Num] =
    b #:: fibs(b, a + b)

val Num = BigInt
val res = fibs(1, 1).
    filter(_ % 2 == 0).
    takeWhile(_ < Num("4000000000000")).sum

println(res)
//}
