// this works with two type params
val f1: PartialFunction[Int, Boolean] = {
    case 0 => false
}
f1.isDefinedAt(0) != f1.isDefinedAt(3)
implicit class Bar1[A, B](f: PartialFunction[A, B]) {
    def not1(x: A) = !f.isDefinedAt(x)
}
f1.not1(1)

// fails when implicit class's constructor's param's type is an alias
type PF[A, B] = PartialFunction[A, B]
implicit class Bar2[A, B](f: PF[A, B]) {
    def not2(x: A) = !f.isDefinedAt(x)
}
f1.not2(1)
// Error: value not2 is not a member of PartialFunction[Int,Boolean]

// But compare to ./two-type-param-foo-with-type-alias-works.kojo
