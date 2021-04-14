
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

val kare2taş = Eşlem.boş[Resim, Taş]
val kare2hane = Eşlem.boş[Resim, Hane]
val hane2kare = Eşlem.boş[Hane, Resim]

var oyuncu: Taş = Beyaz // Beyaz başlasın

var hamleSayısı = 0
def tahtayıYaz = {
    hamleSayısı += 1
    satıryaz(s"\nHamle: $hamleSayısı. Sıra $oyuncu'nin")
    for (y <- 7 to 0 by -1)
        satıryaz(tahta(y).mkString(" "))
}

def taş2renk(t: Taş) = t match {
    case Boş   => koyuYeşil
    case Beyaz => beyaz
    case Siyah => siyah
}
val koyuYeşil = Renk(10, 111, 23)

val boy = 64
def kare = Resim.dikdörtgen(boy, boy)

def kareyiTanımla(k: Resim) = {
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
    def kaçKaç = {
        def say(t: Taş) = (for (x <- 0 to 7; y <- 0 to 7; if tahta(y)(x) == t) yield 1).size
        satıryaz(s"beyazlar: ${say(Beyaz)}")
        satıryaz(s"siyahlar: ${say(Siyah)}")
    }
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
            if (hamleYoksa) {
                satıryaz("Hamle kalmadı!")
                sırayıÖbürOyuncuyaGeçir
                if (hamleYoksa) {
                    satıryaz("İki oyuncu için de hamle kalmadı!")
                    kaçKaç
                }
                else tahtayıYaz
            }
            else tahtayıYaz
        }
    }
    k.fareGirince { (_, _) =>
        val hane = kare2hane(k)
        if (hamleyiYapmayıDene(Hamle(oyuncu, hane)).size > 0) kare2taş(k) match {
            case Boş => k.boyamaRenginiKur(taş2renk(oyuncu))
            case _   =>
        }
    }
    k.fareÇıkınca { (_, _) =>
        k.boyamaRenginiKur(taş2renk(kare2taş(k)))
    }
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

def tahtayıKur = {
    val offset = -256
    val ipucu = EsnekDizim.boş[Resim]
    val gözeBatanRenk = turuncu
    for (x <- 0 to 7; y <- 0 to 7) {
        val renk = tahta(y)(x) match {
            case Boş   => koyuYeşil
            case Siyah => siyah
            case Beyaz => beyaz
        }
        val kRenk = if ((x == 2 && (y == 2 || y == 5)) ||
            (x == 5 && (y == 2 || y == 5))) gözeBatanRenk else mor
        val r = kalemRengi(kRenk) * boyaRengi(renk) * götür(offset + x * 64, offset + y * 64) -> kare
        kare2taş += (r -> tahta(y)(x))
        kare2hane += (r -> Hane(y, x))
        hane2kare += (Hane(y, x) -> r)
        çiz(r)
        if (kRenk == gözeBatanRenk) {
            ipucu += r
        }
        kareyiTanımla(r)
    }
    ipucu.dizi.map(_.öneAl()) // üste çıkar ki sonra çizilen komşu kareler sağ ve üst kenarları mor yapmasın
}

case class Komşu(d: Doğrultu, hane: Hane)

trait Doğrultu // toString needed for debugging only
case object D extends Doğrultu { override def toString = "->>-" }
case object B extends Doğrultu { override def toString = "-<<-" }
case object K extends Doğrultu { override def toString = "-^^-" }
case object G extends Doğrultu { override def toString = "-vv-" }
case object KD extends Doğrultu { override def toString = "-^>-" }
case object KB extends Doğrultu { override def toString = "-^<-" }
case object GD extends Doğrultu { override def toString = "-v>-" }
case object GB extends Doğrultu { override def toString = "-v<-" }

def hamleyiYapmayıDene(h: Hamle) = {
    val kml = komşularıBul(h.hane)
    val yasal = kml filter { komşu =>
        val komşuTaş = tahta(komşu.hane.str)(komşu.hane.stn)
        komşuTaş != Boş && komşuTaş != oyuncu && sonuDaYasalMı(komşu, oyuncu)
    }
    yasal
}

def hamleyiYap(h: Hamle, yasal: Dizi[Komşu]): Birim = {
    yasal.foreach { komşu =>
        val komşuTaş = tahta(komşu.hane.str)(komşu.hane.stn)
        gerisi(komşu).takeWhile { h =>
            val t = tahta(h.str)(h.stn)
            t != Boş && t != oyuncu
        }.foreach(taşıAltÜstYap(_))
        taşıAltÜstYap(komşu.hane)
    }
}

def komşularıBul(h: Hane): Dizi[Komşu] = {
    val kml = EsnekDizim.boş[Komşu]
    kml += Komşu(D, Hane(h.str, h.stn + 1))
    kml += Komşu(B, Hane(h.str, h.stn - 1))
    kml += Komşu(K, Hane(h.str + 1, h.stn))
    kml += Komşu(G, Hane(h.str - 1, h.stn))
    kml += Komşu(KD, Hane(h.str + 1, h.stn + 1))
    kml += Komşu(KB, Hane(h.str + 1, h.stn - 1))
    kml += Komşu(GD, Hane(h.str - 1, h.stn + 1))
    kml += Komşu(GB, Hane(h.str - 1, h.stn - 1))
    kml.dizi.filter { k =>
        val hane = k.hane
        hane.str > -1 && hane.str < 8 && hane.stn > -1 && hane.stn < 8
    }
}

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
    k.d match {
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

silVeSakla
çıktıyıSil
tahtayıKur
tahtayıYaz