// çember üzerinde rastgele kirişler çizelim.
// Herhangi bir kirişin çemberin üzerindeki eşkenar bir üçgenin kenarından
// daha uzun olma olasılığını bulalım.

type Açı = Kesir
type Nokta = (Kesir, Kesir) // noktanın koordinatları (x, y)
type Noktalar = Dizin[Nokta] // üçgenin üç köşesi
type Kenar = (Nokta, Nokta) // bir kenar ya da kiriş

val yç = 100 * karekökü(3) // çemberin yarıçapı bu olsun

val eşkenarÜçgen = Dizin(0, 120, 240)
def rastgeleAçı() = rastgeleDiziden(Aralık(0, 360).dizin)

def nokta(derece: Açı): Nokta = {
    val radyan = derece * piSayısı / 180.0 // pi radyan 180 dereceye denk gelir
    (yç * kosinüs(radyan), yç * sinüs(radyan))
}
val noktalar = {
    val rastgeleAçılar = rastgeleAçı :: eşkenarÜçgen
    rastgeleAçılar.map { nokta(_) }
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
def çemberVeEşkenarÜçgenÇizimi(içiniBoya: İkil = yanlış) = {
    silVeSakla
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
def kiriş() = (nokta(rastgeleAçı()), nokta(rastgeleAçı()))

val örnek = kiriş()
val doldur = boy(örnek) > boy(kenar)
val kVeyaB = if (doldur) ">" else "<"
yaz("Rastgele kirişin uzunluğu: " + yuvarla(boy(örnek)) + "   ")
yaz(kVeyaB)
satıryaz("   Eşkenar üçgenin kenar uzunluğu: " + yuvarla(boy(kenar)))

def olasılığıBul(kaçTaneDeneme: Sayı = 3000) = {
    val kaçTanesiDahaUzun = (
        for {
            i <- Aralık(1, kaçTaneDeneme).dizin
            if boy(kiriş()) > boy(kenar)
        } yield (1)).size
    satıryaz("\nOlasılık(rastgele bir kiriş daha uzun olsun)=" + yuvarla(kaçTanesiDahaUzun / Kesir(kaçTaneDeneme)))
}

çemberVeEşkenarÜçgenÇizimi(doldur)
kenarÇizimi(örnek, mavi)
for (nokta <- Dizi(örnek._1, örnek._2))
    noktaÇizimi(nokta, mavi, if (doldur) mavi else renksiz)
olasılığıBul()
