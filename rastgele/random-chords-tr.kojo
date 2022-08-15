// Bir çember üzerinde rastgele kirişler çizelim.
// Herhangi bir kirişin çemberin üzerindeki eşkenar üçgenin
// kenarından daha uzun olma olasılığını bulalım. Esin kaynağı:
//   Bertrand's Paradox (with 3blue1brown) - Numberphile
//   https://youtu.be/mZBwsm6B280

type Açı = Kesir
type Nokta = (Kesir, Kesir) // noktanın koordinatları (x, y)
type Noktalar = Dizin[Nokta] // üçgenin üç köşesi
type Kenar = (Nokta, Nokta) // bir kenar ya da kiriş
type N2 = (Nokta, Açı)

val yç = 100 * karekökü(3) // çemberin yarıçapı bu olsun
val salla = 12345

val eşkenarÜçgen = Dizin(0, 120, 240)
def rastgeleAçı() = rastgele * 360 // rastgeleDiziden(Aralık(0, 360).dizin)

def nokta(derece: Açı): Nokta = {
    val radyan = derece * piSayısı / 180.0 // pi radyan 180 dereceye denk gelir
    (yç * kosinüs(radyan), yç * sinüs(radyan))
}

object Açı {
    def apply(sayı: Sayı) = { sayı.toDouble }
}
object Kesir { // todo to Kojo tr
    def apply(sayı: Sayı) = { sayı.toDouble }
}

def noktaÇizimi(n: Nokta, renk: Renk = kırmızı, boya: Renk = renksiz) = çiz {
    boyaRengi(boya) * kalemRengi(renk) * götür(n._1, n._2) -> Resim.daire(4)
}
def kenarÇizimi(kenar: Kenar, renk: Renk = kırmızı) = {
    val (n1, n2) = kenar
    val (x1, y1, x2, y2) = (n1._1, n1._2, n2._1, n2._2)
    çiz(götür(x1, y1) * kalemRengi(renk) -> Resim.doğru(x2 - x1, y2 - y1))
}
def kenarÇizimi2(n2: N2, renk: Renk) = {
    val (n, a2) = n2
    val a = yuvarla(a2 * 180.0 / piSayısı)
    val n3 = (yuvarla(n._1), yuvarla(n._2))
    satıryaz("Nokta ve açı: " + n3 + ", " + a)
    atla(n._1, n._2)
    açıyaDön(a)
    kalemRenginiKur(renk)
    sağ()
    val x = 100
    ileri(x)
    geri(2*x)
}
def çemberVeEşkenarÜçgenÇizimi(içiniBoya: İkil = yanlış) = {
    val boya = if (içiniBoya) kırmızı else renksiz
    çiz(kalemRengi(mavi) * götür(0, 0) -> Resim.daire(yç)) // çember
    çiz(boyaRengi(boya) * götür(0, 0) -> Resim.daire(4)) // merkez
    val üçgen = eşkenarÜçgen.map(nokta(_))
    üçgen.map(noktaÇizimi(_, kırmızı, boya))
    üçgen.zip(üçgen.last :: üçgen).map(kenarÇizimi(_)) // kenarlar
}

def boy(k: Kenar): Kesir = {
    val (yatayBoy, dikeyBoy) = (k._2._1 - k._1._1, k._2._2 - k._1._2)
    karekökü(yatayBoy * yatayBoy + dikeyBoy * dikeyBoy)
}
def yuvarla(k: Kesir): Kesir = yakın(k * 100) / 100.0

val kenar = (nokta(eşkenarÜçgen(0)), nokta(eşkenarÜçgen(1)))
// üç farklı şekilde üretebiliriz rastgele kirişleri (Olasılık)
// 1- çember üstünde iki rastgele nokta seç. (1/3)
def kiriş1() = (nokta(rastgeleAçı()), nokta(rastgeleAçı()))
// 2- çemberin içinde rastgele bir nokta (N) seç. (1/4)
// Orta noktası N olan yegane bir kiriş var.
def kiriş2(): Nokta = {
    var x = 1.0; var y = 1.0
    while (x * x + y * y > 1) {
        x = rastgele
        y = rastgele
    }
    if (rastgeleİkil) x = -x
    if (rastgeleİkil) y = -y
    (x * yç, y * yç) // orta nokta
}
// 3- rastgele bir yarıçap (R) üzerinde rastgele bir nokta (N) seç. (1/2)
// Aynı ikinci yöntemde olduğu gibi, orta noktası N olan tek bir kiriş var.
def kiriş3(): N2 = {
    val x = rastgele
    val (r, a) = (x * yç, rastgeleAçı() * piSayısı / 180)
    ((r * kosinüs(a), r * sinüs(a)), a)  // orta nokta ve açı
}
def kiriş() = kiriş1()

val örnek = kiriş()
val doldur = boy(örnek) > boy(kenar)
val kVeyaB = if (doldur) ">" else "<"
yaz("Rastgele kirişin uzunluğu: " + yuvarla(boy(örnek)))
yaz("   " + kVeyaB + "   ")
satıryaz("Eşkenar üçgenin kenar uzunluğu: " + yuvarla(boy(kenar)))
val örnek3 = kiriş3()
val doldur2v3 = boy((örnek3._1, (0, 0))) < yç / 2
val msj = if (doldur2v3) "daha uzun" else "daha kısa"
satıryaz("İkinci ve üçüncü yöntemle bulduğumuz kiriş: " + msj)

def olasılığıBul(kaçTaneDeneme: Sayı = salla) = { // todo: DRY
    val kaçTanesiDahaUzun = (
        for {
            i <- Aralık(1, kaçTaneDeneme).dizin
            if boy(kiriş()) > boy(kenar)
        } yield (1)
    ).size
    satıryaz("Olasılık(rastgele kirişin daha uzun olması)=" + yuvarla(kaçTanesiDahaUzun / Kesir(kaçTaneDeneme)))
}
def olasılığıBul2v3(ikinciYöntem: İkil, kaçTaneDeneme: Sayı = salla) = {
    val kaçTanesiDahaUzun = (
        for {
            i <- Aralık(1, kaçTaneDeneme).dizin
            if boy((if (ikinciYöntem) kiriş2 else kiriş3._1), (0, 0)) < yç / 2
        } yield (1)
    ).size
    satıryaz("Olasılık(rastgele kirişin daha uzun olması)=" + yuvarla(kaçTanesiDahaUzun / Kesir(kaçTaneDeneme)))
}

silVeSakla; hızıKur(hızlı)
kiriş3()
çemberVeEşkenarÜçgenÇizimi(doldur)
kenarÇizimi(örnek, mavi)
for (nokta <- Dizi(örnek._1, örnek._2))
    noktaÇizimi(nokta, mavi, if (doldur) mavi else renksiz)
noktaÇizimi(örnek3._1, yeşil, if (doldur2v3) yeşil else renksiz)
kenarÇizimi((örnek3._1, (0, 0)), yeşil)
kenarÇizimi2(örnek3, yeşil)
olasılığıBul()
olasılığıBul2v3(doğru)
olasılığıBul2v3(yanlış)
