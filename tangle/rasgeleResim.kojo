// https://docs.kogics.net/reference/scala.html#data
// Esin kaynağı: https://generativeartistry.com/tutorials/tiled-lines/

// tuvali satranç tahtası gibi sütunSayısı x satırSayısı tane haneye bölelim 
// ve her haneye rastgele bir köşegen çizelim
// #include trTerimler
tümEkran
val ta = tuvalAlanı
val (satırSayısı, sütunSayısı) = (20, 30)
val (satırBoyu, sütunEni) = (ta.boy / satırSayısı, ta.en / sütunSayısı)

case class Köşegen(x: Kesir, y: Kesir, en: Kesir, boy: Kesir)
def birKöşegen(x: Kesir, y: Kesir, e: Kesir, b: Kesir) =
    if (rastgeleSeçim) Köşegen(x, y, e, b) // sol alt köşeden sağ üst köşeye
    else Köşegen(x, y + b, e, -b) //          sol üst köşeden sağ alt köşeye

// çizim için bellekte bir veri yapısı oluşturalım ve bütün köşegenleri hesaplayalım
val çizim = ArrayBuffer[Köşegen]()
yineleİçin(0 until sütunSayısı) { sütun =>
    val x = ta.x + sütun * sütunEni
    yineleİçin(0 until satırSayısı) { satır =>
        val y = ta.y + satır * satırBoyu
        çizim += birKöşegen(x, y, sütunEni, satırBoyu)
    }
}
// artık tuvale çizmeye hazırız
silVeSakla
arkaplanıKur(Renk(219, 198, 101))
yineleİçin(çizim) { kg =>
    val resim = Resim.köşegen(kg.en, kg.boy)
    resim.kondur(kg.x, kg.y)
    resim.kalemKalınlığınıKur(12)
    resim.kalemRenginiKur(rastgeleRenk)
    çiz(resim) 
}
