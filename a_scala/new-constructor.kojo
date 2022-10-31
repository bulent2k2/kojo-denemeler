class Foo(var x: Int) {
    def incr = { x += 1; this }
    def show = println(x)
}

implicit class Bar(f: Foo) {
    def incr2 = f.incr.incr
}

val f = new Foo(3) {
    show
    incr
    show
}
f.incr2
f.show
