type PFDI = PartialFunction[Double, Int]
val f: PFDI = {
    case 1.0 => 1
    case 2.0 => 2
    case 3.0 => 3
}
f.isDefinedAt(2.0) != f.isDefinedAt(4.0)
implicit class Foo(f: PFDI) {
    def notDefinedAt(x: Double) = !f.isDefinedAt(x)
}
f.notDefinedAt(4.0) != f.notDefinedAt(2.0)

val f2: PartialFunction[Int, Boolean] = {
    case 0 => false
    case 1 => false
    case 2 => true
}
f2.isDefinedAt(0) != f2.isDefinedAt(3)
implicit class Bar[A, B](f: PartialFunction[A, B]) {
    def notDefinedAt(x: A) = !f.isDefinedAt(x)
}
f2.notDefinedAt(3)