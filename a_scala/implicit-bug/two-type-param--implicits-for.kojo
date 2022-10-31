clearOutput()

class Foo[A, B](a: A, b: B) {
    override def toString = s"a:$a b:$b"
}
val f = new Foo(3, true)
implicit class Bar[A, B](f: Foo[A, B]) {
    def p = println(f)
}
f.p
