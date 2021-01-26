// #include trLeftOvers.kojo
tümEkran
val ta = tuvalAlanı
val (satırSayısı, sütunSayısı) = (20, 30)
val (satırBoyu, sütunEni) = (ta.boy / satırSayısı, ta.en / sütunSayısı)

case class Köşegen(x: Kesir, y: Kesir, en: Kesir, boy: Kesir)
def birKöşegen(x: Kesir, y: Kesir, e: Kesir, b: Kesir) =
    if (rastgeleSeçim) Köşegen(x, y, e, b)
    else Köşegen(x, y + b, e, -b)

val çizim = ArrayBuffer[Köşegen]()
yineleKere(0 until sütunSayısı) { sütun =>
    val x = ta.x + sütun * sütunEni
    yineleKere(0 until satırSayısı) { satır =>
        val y = ta.y + satır * satırBoyu
        çizim += birKöşegen(x, y, sütunEni, satırBoyu)
    }
}
silVeSakla
arkaplanıKur(Renk(219, 198, 101))
yineleKere(çizim) { kg =>
    val resim = Resim.köşegen(kg.en, kg.boy)
    resim.kondur(kg.x, kg.y)
    resim.kalemKalınlığınıKur(12)
    resim.kalemRenginiKur(rastgeleRenk)
    çiz(resim) 
}
