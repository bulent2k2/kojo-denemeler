
trait Taş
case object Beyaz extends Taş { override def toString() = "B" }
case object Siyah extends Taş { override def toString() = "S" }
case object Boş extends Taş { override def toString() = "." }

case class Hane(str: Sayı, stn: Sayı)
case class Hamle(oyuncu: Taş, hane: Hane)

val tahta = Dizim.doldur[Taş](8, 8)(Boş)
tahta(3)(3) = Beyaz
tahta(4)(4) = Beyaz
tahta(3)(4) = Siyah
tahta(4)(3) = Siyah
def yasalHane(h: Hane) = 0 <= h.str && h.str < 8 && 0 <= h.stn && h.stn < 8

val kare2taş = Eşlem.boş[Resim, Taş]
val kare2hane = Eşlem.boş[Resim, Hane]
val hane2kare = Eşlem.boş[Hane, Resim]

var oyuncu: Taş = Beyaz // Beyaz ya da Siyah başlayabilir. Seç :-)

var hamleSayısı = 0
def tahtayıYaz = {
    for (y <- 7 to 0 by -1) satıryaz(tahta(y).mkString(" "))
    hamleSayısı += 1
    satıryaz(s"Hamle: $hamleSayısı. Sıra $oyuncu'nin")
}

val koyuYeşil = Renk(10, 111, 23)

def tahtayıKur = {
    val offset = -256
    val ipucu = EsnekDizim.boş[Resim]
    val içKöşeKalemRengi = Renk(255, 215, 85, 101)
    for (x <- 0 to 7; y <- 0 to 7) {
        val renk = tahta(y)(x) match {
            case Boş   => koyuYeşil
            case Siyah => siyah
            case Beyaz => beyaz
        }
        val kRenk = if ((x == 2 && (y == 2 || y == 5)) ||
            (x == 5 && (y == 2 || y == 5))) içKöşeKalemRengi else mor
        val r = kalemRengi(kRenk) * boyaRengi(renk) * götür(offset + x * 64, offset + y * 64) -> kare
        kare2taş += (r -> tahta(y)(x))
        kare2hane += (r -> Hane(y, x))
        hane2kare += (Hane(y, x) -> r)
        çiz(r)
        if (kRenk == içKöşeKalemRengi) ipucu += r
        kareyiTanımla(r)
    }
    ipucu.dizi.map(_.öneAl())
}

val boy = 64
def kare = Resim.dikdörtgen(boy, boy)
def kareyiTanımla(k: Resim) = {
    k.fareyeTıklayınca { (_, _) =>
        var oynadıMı = yanlış
        val hane = kare2hane(k)
        val (str, stn) = (hane.str, hane.stn)
        tahta(str)(stn) match {
            case Boş =>
                val hamle = Hamle(oyuncu, hane)
                val yasal = hamleyiYapmayıDene(hamle)
                if (yasal.size > 0) {
                    oynadıMı = doğru
                    kare2taş += (k -> oyuncu)
                    hamleyiYap(hamle, yasal)
                    tahta(str)(stn) = oyuncu
                }
            case _ =>
        }
        if (oynadıMı) {
            sırayıÖbürOyuncuyaGeçir
            tahtayıYaz
            if (hamleYoksa) {
                sırayıÖbürOyuncuyaGeçir
                if (hamleYoksa) bittiKaçKaç
                else satıryaz(s"Yasal hamle yok. Sıra yine $oyuncu'nin")
            }
        }
    }
    k.fareGirince { (_, _) =>
        kare2taş(k) match {
            case Boş => if (hamleyiYapmayıDene(Hamle(oyuncu, kare2hane(k))).size > 0)
                k.boyamaRenginiKur(taş2renk(oyuncu))
            case _ => 
        }
    }
    k.fareÇıkınca { (_, _) =>
        k.boyamaRenginiKur(taş2renk(kare2taş(k)))
    }
    def taş2renk(t: Taş) = t match {
        case Boş   => koyuYeşil
        case Beyaz => beyaz
        case Siyah => siyah
    }
    def sırayıÖbürOyuncuyaGeçir = {
        oyuncu = oyuncu match {
            case Beyaz => Siyah
            case Siyah => Beyaz
        }
    }
    def hamleYoksa = {
        val boşlar = for (x <- 0 to 7; y <- 0 to 7; if tahta(y)(x) == Boş) yield Hane(y, x)
        boşlar.size == 0 || (boşlar.filter { hane =>
            hamleyiYapmayıDene(Hamle(oyuncu, hane)).size > 0
        }).size == 0
    }
    def say(t: Taş) = (for (x <- 0 to 7; y <- 0 to 7; if tahta(y)(x) == t) yield 1).size
    def bittiKaçKaç = satıryaz(s"Oyun bitti.\nbeyazlar: ${say(Beyaz)}\nsiyahlar: ${say(Siyah)}")
}

trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön

case class Komşu(yön: Yön, hane: Hane)

def hamleyiYapmayıDene(h: Hamle) = komşularıBul(h.hane) filter { komşu =>
    val komşuTaş = tahta(komşu.hane.str)(komşu.hane.stn)
    komşuTaş != Boş && komşuTaş != oyuncu && sonuDaYasalMı(komşu, oyuncu)
}

def komşularıBul(h: Hane): Dizi[Komşu] = Dizi(
    Komşu(D, Hane(h.str, h.stn + 1)),
    Komşu(B, Hane(h.str, h.stn - 1)),
    Komşu(K, Hane(h.str + 1, h.stn)),
    Komşu(G, Hane(h.str - 1, h.stn)),
    Komşu(KD, Hane(h.str + 1, h.stn + 1)),
    Komşu(KB, Hane(h.str + 1, h.stn - 1)),
    Komşu(GD, Hane(h.str - 1, h.stn + 1)),
    Komşu(GB, Hane(h.str - 1, h.stn - 1))) filter { k => yasalHane(k.hane) }

def sonuDaYasalMı(k: Komşu, oyuncu: Taş): İkil = {
    val sıraTaşlar = gerisi(k).dropWhile { h =>
        val t = tahta(h.str)(h.stn)
        t != oyuncu && t != Boş
    }
    if (sıraTaşlar.isEmpty) yanlış else {
        val hane = sıraTaşlar.head
        tahta(hane.str)(hane.stn) == oyuncu
    }
}

def gerisi(k: Komşu): Dizi[Hane] = {
    val sıra = EsnekDizim.boş[Hane]
    val (x, y) = (k.hane.stn, k.hane.str)
    k.yön match {
        case D => for (i <- x + 1 to 7) /* */ sıra += Hane(y, i)
        case B => for (i <- x - 1 to 0 by -1) sıra += Hane(y, i)
        case K => for (i <- y + 1 to 7) /* */ sıra += Hane(i, x)
        case G => for (i <- y - 1 to 0 by -1) sıra += Hane(i, x)
        case KD => // hem str hem stn artacak
            if (x >= y) for (i <- x + 1 to 7) /*      */ sıra += Hane(y + i - x, i)
            else for (i <- y + 1 to 7) /*             */ sıra += Hane(i, x + i - y)
        case GB => // hem str hem stn azalacak
            if (x >= y) for (i <- y - 1 to 0 by -1) /**/ sıra += Hane(i, x - y + i)
            else for (i <- x - 1 to 0 by -1) /*       */ sıra += Hane(y - x + i, i)
        case KB => // str artacak stn azalacak
            if (x + y >= 7) for (i <- y + 1 to 7) /*  */ sıra += Hane(i, x + y - i)
            else for (i <- x - 1 to 0 by -1) /*       */ sıra += Hane(y + x - i, i)
        case GD => // str azalacak stn artacak
            if (x + y >= 7) for (i <- x + 1 to 7) /*  */ sıra += Hane(y + x - i, i)
            else for (i <- y - 1 to 0 by -1) /*       */ sıra += Hane(i, x + y - i)
    }
    sıra.dizi
}

def hamleyiYap(h: Hamle, yasal: Dizi[Komşu]): Birim = yasal.foreach { komşu =>
    val komşuTaş = tahta(komşu.hane.str)(komşu.hane.stn)
    gerisi(komşu).takeWhile { h =>
        val t = tahta(h.str)(h.stn)
        t != Boş && t != oyuncu
    }.foreach(taşıAltÜstYap(_))
    taşıAltÜstYap(komşu.hane)
}
def taşıAltÜstYap(h: Hane): Birim = {
    tahta(h.str)(h.stn) = oyuncu
    val k = hane2kare(h)
    k.boyamaRenginiKur(
        oyuncu match {
            case Beyaz => beyaz
            case Siyah => siyah
        })
    kare2taş += (k, oyuncu)
}

silVeSakla
çıktıyıSil
tahtayıKur
tahtayıYaz