// The translation is much slower :-(
val translation = false
def main() {
    if (!translation) {
        def dikdörtgendenResim(r: Dikdörtgen) = {
            penColor(siyah) * fillColor(r.renk) * penWidth(10) * trans(r.x, r.y) ->
                Picture.rectangle(r.en, r.boy)
        }
        draw(dörtgenler.map(dikdörtgendenResim))
    }
    else {
        def dikdörtgendenResim(r: Dikdörtgen) = {
            kalemRengi(siyah) * boyaRengi(r.renk) * kalemBoyu(10) * götür(r.x, r.y) ->
                Resim.dikdörtgen(r.en, r.boy)
        }
        çiz(dörtgenler.map(dikdörtgendenResim))
    }
}

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
val ta = tuvalAlanı

val beyaz2 = Renk(0xF2F5F1)
val çokRenkli = doğru // çok renkli olsun ister misin?
val renkler =
    if (çokRenkli) Dizi(Renk(0xD40920), Renk(0x1356A2), Renk(0xF7D842), pembe, kahverengi, morumsu, camgöbeği, mor, turuncu, mavi, yeşil, gri, açıkGri, koyuGri, siyah)
    else Dizi(Renk(0xD40920), Renk(0x1356A2), Renk(0xF7D842))

case class Dikdörtgen(x: Kesir, y: Kesir, en: Kesir,
                      boy: Kesir, var renk: Renk = beyaz2)

// yatay böleceksek: z -> y, yoksa dikey böleceksek: z -> x
def dikdörtgenleriBöl(dörtgenler: Vector[Dikdörtgen], z: Kesir, yatay: İkil) = {
    var çıktı = dörtgenler
    yineleİçin(dörtgenler.length - 1 to 0 by -1) { i =>
        val dörtgen = dörtgenler(i)
        val (ufak, iri) = if (yatay) (dörtgen.y, dörtgen.y + dörtgen.boy) else (dörtgen.x, dörtgen.x + dörtgen.en)
        if (z > ufak && z < iri) {
            if (rastgeleİkil) {
                çıktı = çıktı.slice(0, i) ++ çıktı.slice(i + 1, çıktı.length)
                çıktı = çıktı ++ böl(dörtgen, z, yatay)
            }
        }
    }
    çıktı
}

def böl(dörtgen: Dikdörtgen, z: Kesir, yatay: İkil) = {
    val (x, y, en, boy) = (dörtgen.x, dörtgen.y, dörtgen.en, dörtgen.boy)
    val gen = z - (if (yatay) y else x)
    val dörtgenA = if (yatay) Dikdörtgen(x, y, en, gen) else Dikdörtgen(x, y, gen, boy)
    val dörtgenB = if (yatay) Dikdörtgen(x, z, en, boy - gen) else Dikdörtgen(z, y, en - gen, boy)
    Vector(dörtgenA, dörtgenB)
}

var dörtgenler = Vector(Dikdörtgen(ta.x, ta.y, ta.eni, ta.boyu))
val n = 20
val adımx = ta.eni / (n + 1)
val adımy = ta.boyu / (n + 1)

yineleİçin(1 to n) { i =>
    dörtgenler = dikdörtgenleriBöl(dörtgenler, ta.x + i * adımx, yanlış) // dikey böl
    dörtgenler = dikdörtgenleriBöl(dörtgenler, ta.y + i * adımy, doğru) // yatay böl
}

/* bu yöntem pek iyi değil: kazara aynı indeks rastgelebilir!
renkler.foreach { renk =>
    val idx = taban(rastgeleKesir(1) * dörtgenler.length).toInt
    dörtgenler(idx).renk = renk
} */
dörtgenler = rastgeleKarıştır(dörtgenler)
renkler.zipWithIndex.map {
    case (renk, idx) => if (idx < dörtgenler.size)
        dörtgenler(idx).renk = renk
}

main