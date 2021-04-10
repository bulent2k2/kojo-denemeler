case class Aralık(ilk: Sayı, son: Sayı, adım: Sayı = 1) {
    val r = Range(ilk, son, adım)
    val başı = r.head
    val sonu = r.last
    val uzunluğu = r.size
    def dizin(): Dizin[Sayı] = r.toList
    def yazı() = toString()
    override def toString() = {
        val yazı = if (r.size <= 10) r.mkString("(", ", ", ")")
        else {
            val (başı, sonu) = (r.take(5), r.drop(r.size - 5))
            başı.mkString("(", ", ", " ...") + sonu.mkString(" ", ", ", ")")
        }
        s"Aralık$yazı"
    }
    def map[B](f: Sayı => B) = r.map(f)
    def withFilter(pred: Sayı => İkil) = r.withFilter(pred)
    def flatMap[B](f: Sayı => IterableOnce[B]) = r.flatMap(f)
}

object Aralık {
    def apply(ilk: Sayı, son: Sayı, adım: Sayı = 1) = new Aralık(ilk, son, adım)
    def kapalı(ilk: Sayı, son: Sayı, adım: Sayı = 1) = new Aralık(ilk, son+1, adım)
}


val a = new Aralık(1, 10, 3)
a.ilk == 1
a.son == 10
a.adım == 3
a.dizin == List(1, 4, 7)
a.yazı == "Aralık(1, 4, 7)"
a.map(_ * 2) == Vector(2, 8, 14)
a.flatMap(s => List(s, s*s)) == Vector(1, 1, 4, 16, 7, 49)

val a2 = new Aralık(1, 200, 7)
a2.dizin.size == 29
a2.başı == 1
a2.sonu == 197
a2.uzunluğu == 29

val a3 = Aralık.kapalı(1, 10, 3)
a3.dizin == List(1, 4, 7, 10)
(for (i <- a3 if i % 2 != 0) yield i) == Vector(1, 7)
