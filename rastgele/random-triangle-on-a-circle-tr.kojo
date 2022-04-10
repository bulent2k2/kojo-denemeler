// çember üzerinde rastgele üç nokta seçerek bir üçgen çizsek
// çemberin merkezini içine alma olasılığı ne olur?

type Nokta = (Kesir, Kesir) // noktanın koordinatları (x, y)
type Noktalar = Dizin[Nokta] // üçgenin üç köşesi

val yç = 100 // çemberin yarıçapı bu olsun

def olasılığıBul(kaçTaneDeneme: Sayı = 3000) = {
    val kaçTanesiMerkeziİçineAlır = (
        for (
            i <- Aralık(1, kaçTaneDeneme).dizin if (merkezÜçgeninİçindeMi(rastgeleNoktalar(3)))
        ) yield (1)).size
    def yuvarla(k: Kesir): Kesir = yakın(k * 100) / 100.0
    satıryaz("Olasılık(merkez üçgenin içinde)=" + yuvarla(kaçTanesiMerkeziİçineAlır / Kesir(kaçTaneDeneme)))
}

def rastgeleNoktalar(tane: Sayı): Noktalar = {
    val rastgeleAçılar = for (i <- Aralık(0, tane).dizin)
        yield rastgeleDiziden(Aralık(0, 360).dizin)
    // dereceleri (x, y) koordinatlarına çevirelim
    rastgeleAçılar.map { derece =>
        // pi radyan 180 dereceye denk gelir
        val radyan = derece * piSayısı / 180.0
        (yç * kosinüs(radyan), yç * sinüs(radyan))
    }
}

object Kesir { // todo to Kojo tr
    def apply(sayı: Sayı) = { sayı.toDouble }
}

def merkezÜçgeninİçindeMi(üçgen: Noktalar): İkil = {
    belirt(üçgen.size == 3, "Sadece üçgen için yazdık şimdilik.")
    val (uzun, kısa1, kısa2) = { // üç kenarın boyunu bulalım ve en uzunu en başa koyalım
        def boy(n1: Nokta, n2: Nokta) = {
            val ((x1, y1), (x2, y2)) = (n1, n2)
            val (yatayBoy, dikeyBoy) = (x2 - x1, y2 - y1)
            karekökü(yatayBoy * yatayBoy + dikeyBoy * dikeyBoy)
        }
        val boylar = (üçgen.zip(üçgen.last :: üçgen).map { case (n1, n2) => boy(n1, n2) }).sorted.reverse
        (boylar(0), boylar(1), boylar(2))
    }
    uzun * uzun < (kısa1 * kısa1 + kısa2 * kısa2)
}
def çizim(noktalar: Noktalar, içiniBoya: İkil = yanlış) = {
    silVeSakla
    val boya = if (içiniBoya) kırmızı else renksiz
    çiz(kalemRengi(mavi) * götür(0, 0) -> Resim.daire(yç)) // çember
    çiz(boyaRengi(boya) * götür(0, 0) -> Resim.daire(4)) // merkez
    noktalar.map(n => çiz(boyaRengi(boya) * götür(n._1, n._2) -> Resim.daire(4))) // üçgenin köşeleri
    noktalar.zip(noktalar.last :: noktalar).map { // üçgenin üç kenarını çizelim
        case (n1, n2) => {
            val (x1, y1, x2, y2) = (n1._1, n1._2, n2._1, n2._2)
            çiz(götür(x1, y1) -> Resim.doğru(x2 - x1, y2 - y1))
        }
    }
}

olasılığıBul()

// bir tane de rastgele üçgen çizelim de keyifli olsun
val noktalar = rastgeleNoktalar(3)
çizim(noktalar, merkezÜçgeninİçindeMi(noktalar))
// çember üzerinde daha çok nokta seçip onları çizgilerle bağlayabiliriz:
// çizim(rastgeleNoktalar(15)) // Ama karışık oldu bazen! Sıraya sokabilir misin?

