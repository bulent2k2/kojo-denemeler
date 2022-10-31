
val e = Eşlek("a" -> 10, "b" -> 3)
satıryaz(e)

val d = Dizik(10, 3, 9)
d.foreach(println)

val k = Küme(100, 10, 1)
satıryaz(k)

val k2 = Küme.boş[Yazı]
val k3 = k2 + "Merhaba"
k3 + "Dünya"
