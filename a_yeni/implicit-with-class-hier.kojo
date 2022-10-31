implicit class SeqMethods[T](c: Seq[T]) {
    def test1 = c.tail
    def change1[S >: T](index: Int, value: S) = c.updated(index, value)
}
implicit class VectorMethods[A](c: Vector[A]) {
    def test2 = c.tail
    def change2[B >: A](index: Int, value: B) = c.updated(index, value)
}

val v = Vector(0, 1)
v.test1 // val res: Seq[Int] = Vector(1)

Vector(0, 0, 1, 1).
    foldLeft(Vector(0, 1, 2)) {
        (vec, s) => vec.change1(s, 99)
    }