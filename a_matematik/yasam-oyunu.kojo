çıktıyıSil; silVeSakla(); kalemRenginiKur(mavi)
// bu oyunun dünyası yani tahtası büyük bir kare. Kenarı KU uzunluğunda olsun
// Nasıl satranç tahtası 8x8, bu tahta da 128x128 kare.
val KU = 128
// karenın kenarı kaplumbanın on adımına denk

// ilk önce, bütün kareler cansız olmalı
var dünya = (0 |- KU * KU).soldanKatla(Sayılar())((x, y) => x :+ 0)
satıryaz(s"Dünyamızda $KU'in karesi yani ${dünya.size} tane hane var.")
yaz(s"Ekranımız ${(tuvalAlanı.eni / 10).sayıya} kare eninde ")
satıryaz(s"ve ${(tuvalAlanı.boyu / 10).sayıya} kare boyunda.")

val oran = 5 // canlandırmayı yavaşlatmak için bunu arttır.
// En hızlısı 1. 40'a eşitlersen saniyede bir nesil ilerliyor yaklaşık olarak.
// Nasıl mı? Aşağıdaki canlandır adlı döngü komutu saniyede yaklaşık 40 kere yineleniyor.

val gösterVeDur = yanlış // bunu doğru yaparsan deseni gösterip dururuz
val sonundaDur = doğru // her desenin bir durağı var. Ondan sonra fazla bir şey değişmiyor.
// Ama, yine de çalışmaya devam etsin isterse, bunu yanlışa çevir.

// deseni seçelim:
val seç = 1
// blok1 ve blok2 bir kaç füze yolluyor ve sonra 1000. nesil civarı gibi duruyor.
val (desen, adı, durak) = seç match {
    case 0 => (üçlüler, "üçlüler", 20)
    case 1 => (kayGit, "kayGit", 500) /* makineli tüfek gibi */
    case 2 => (esaslı, "esaslı", 1111) /* Yaklaşık 1000 nesil canlı sonra peryodik */
    case 3 => (dokuzcanlı, "dokuzcanlı", 130) /* 131 nesil sonra can kalmıyor */
    case 4 => (blok1, "blok1", 1200) //
    case 5 => (blok2, "blok2", 1200) //
    case 6 => (küçücük, "küçücük", 700) //
    case 7 => (ü2a, "ü2a", 60) // üçlülere ek
    case 8 => (ü2b, "ü2b", 60) // benzeri
    case 9 => (dörtlü, "dörtlü", 30) // üçlü üretiyor
    case _ => (tohum, "tohum", 2200) // ne muhteşem bir meşe palamudu!
}

dünya = başlangıç(dünya, desen)

yaz(s"$seç. desende ${desen.size} tane canlı kare var. Adı $adı.\nNesilleri sayalım: ")

var zaman = 0
val z0 = buSaniye // şimdiki zamanı (geçmişte bir ana göre) anımsayalım
canlandır {
    val nesil = zaman / oran + 1
    if (zaman % oran == 0) {
        Resim.sil()
        çizim(dünya)
        dünya = (0 |- KU * KU).soldanKatla(Sayılar())((x, y) => x :+ yeniNesil(dünya, y))
        yaz(s"$nesil ")
        if (gösterVeDur) durdur
    }
    zaman += 1
    if (sonundaDur && nesil == durak) {
        val z1 = buSaniye - z0
        satıryaz(s"\n${yuvarla(z1, 2)} saniye geçti. Durduk.")
        durdur()
    }
}

// deseni kuralım
def başlangıç(v: Sayılar, desen: Dizin[(Sayı, Sayı)]) = desen.
    soldanKatla(v) {
        (x, y) => x.değiştir((y._1 + KU / 2) * KU + y._2 + KU / 2, 1)
    }

// yeni nesli bulalım
def yeniNesil(v: Sayılar, ix: Sayı) = {
    val kural = Yöney(0, 0, 0, 1, 1, 0, 0, 0, 0, 0) // oyunun kuralları
    val x = ix / KU; val y = ix % KU
    val t = (0 |- 3).soldanKatla(0)((st, i) => {
        st + (0 |- 3).soldanKatla(0)((s, j) => {
            val xt = x + i - 1; val yt = y + j - 1
            s + (if ((xt < 0) || (xt >= KU) || (yt < 0) || (yt >= KU)) 0 else v(xt * KU + yt))
        })
    })
    if (v(ix) == 1) kural(t) else { if (t == 3) 1 else 0 }
}
// canlı kareleri çizelim. Can mavi çember içi kırmızı daire. Yarıçapı 5
val yarıçap = 5
def çizim(v: Sayılar) = for (i <- 0 |- KU * KU)
    if (v(i) == 1) çiz(götür(
        (i / KU) * 2 * yarıçap - KU * yarıçap,
        (i % KU) * 2 * yarıçap - KU * yarıçap
    ) * kalemRengi(mavi) * boyaRengi(kırmızı) -> Resim.daire(yarıçap))

// Meşhur olmuş desenlerden birkaçı
def esaslı = Dizin((0, 1), (1, 0), (1, 1), (1, 2), (2, 2)) // orijinal adı: fpent
// İki küçücük grup var ve kolay kolay ölmüyor
def dokuzcanlı = Dizin((0, 1), (1, 0), (1, 1), (5, 0), (6, 0), (7, 0), (6, 2)) // diehard
def tohum = Dizin((0, 0), (1, 0), (1, 2), (3, 1), (4, 0), (5, 0), (6, 0))
// glider adlı meşhur üretken desen
def kayGit = Dizin((-18, 3), (-18, 4), (-17, 3), (-17, 4), (-8, 2), (-8, 3), (-8, 4), (-7, 1), (-7, 5),
    (-6, 0), (-6, 6), (-5, 0), (-5, 6), (-4, 3), (-3, 1), (-3, 5), (-2, 2), (-2, 3), (-2, 4),
    (-1, 3), (2, 4), (2, 5), (2, 6), (3, 4), (3, 5), (3, 6), (4, 3), (4, 7),
    (6, 2), (6, 3), (6, 7), (6, 8), (16, 5), (16, 6), (17, 5), (17, 6))
def blok1 = Dizin((0, 0), (2, 0), (2, 1), (4, 2), (4, 3), (4, 4), (6, 3), (6, 4), (6, 5), (7, 4))
def blok2 = Dizin((0, 0), (0, 3), (0, 4), (1, 1), (1, 4), (2, 0), (2, 1), (2, 4), (3, 2), (4, 0),
    (4, 1), (4, 2), (4, 4))
def küçücük = Dizin((-18, 0), (-17, 0), (-16, 0), (-15, 0), (-14, 0), (-13, 0), (-12, 0), (-11, 0), (-9, 0), (-8, 0),
    (-7, 0), (-6, 0), (-5, 0), (-1, 0), (0, 0), (1, 0), (8, 0), (9, 0), (10, 0),
    (11, 0), (12, 0), (13, 0), (14, 0), (16, 0), (17, 0), (18, 0), (19, 0), (20, 0))
def üçlüler = Dizin((0, 2), (0, 3), (0, 4), (0, -2), (0, -3), (0, -4),
    (-2, 0), (-3, 0), (-4, 0), (2, 0), (3, 0), (4, 0))
// üçlülerden dikey olanları bağlayalım
def ü2a = Dizin((0, 0), (0, 1), (0, -1)) ++ üçlüler
// öbür türlü, yani yatay olanları bağlayalım
def ü2b = Dizin((0, 0), (1, 0), (-1, 0)) ++ üçlüler
def dörtlü = Dizin((0, 0), (1, 0), (-1, 0), (0, 2)) // dokuzcanlı'nın altkümesi

// sepet sepet yumurta
// sakın beni unutma
// şimdilik bu kadar
// yaşamın tadını çıkar
