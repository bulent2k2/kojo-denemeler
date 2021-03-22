
case class A(val id: Int) {} //> class A
case class B(val a: A, val id: Int) {} //> class B

val a = A(1) //> val a: A = A(1)
val b = B(a, 2) //> val b: B = B(A(1),2)

val f1: B => Double = b => b.id / 3.0 //> val f1: B => Double = $Lambda$2010/0x0000000800ef7840@18d205bf

f1(b) //> val res3: Double = 0.6666666666666666

// can we get f2: A => Double from f1?
// we want to apply f2 on b.a

