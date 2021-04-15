
trait Taş
case object Beyaz extends Taş { override def toString() = "B" }
case object Siyah extends Taş { override def toString() = "S" }
case object Boş extends Taş { override def toString() = "." }

case class Oda(str: Sayı, stn: Sayı)
case class Oyun(oyuncu: Taş, oda: Oda)

val odaSayısı = 8 // bir satır ya da sütundaki oda sayısı
val tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Boş)
val sonStr = odaSayısı - 1
val strArtı = 0 to sonStr
val strEksi = sonStr to 0 by -1

var oyuncu: Taş = Beyaz // Beyaz ya da Siyah başlayabilir. Seç :-)

var hamleSayısı = 0
def tahtayıYaz = {
    for (y <- strEksi) satıryaz(tahta(y).mkString(" "))
    hamleSayısı += 1
    satıryaz(s"Oyun: $hamleSayısı. Sıra ${adı(oyuncu)}ın")
}
def adı(o: Taş) = if (o == Siyah) "siyah" else "beyaz"

val koyuYeşil = Renk(10, 111, 23)

def tahtayıKur = {
    val offset = -256
    val ipucu = EsnekDizim.boş[Resim]
    val içKöşeKalemRengi = Renk(255, 215, 85, 101)
    for (x <- strArtı; y <- strArtı) {
        val renk = tahta(y)(x) match {
            case Boş   => koyuYeşil
            case Siyah => siyah
            case Beyaz => beyaz
        }
        val kRenk = if ((x == 2 && (y == 2 || y == sonStr - 2)) || // todo
            (x == sonStr - 2 && (y == 2 || y == sonStr - 2))) içKöşeKalemRengi else mor
        val r = kalemRengi(kRenk) * boyaRengi(renk) * götür(offset + x * boy, offset + y * boy) -> kare
        kare2taş += (r -> tahta(y)(x))
        kare2oda += (r -> Oda(y, x))
        oda2kare += (Oda(y, x) -> r)
        çiz(r)
        if (kRenk == içKöşeKalemRengi) ipucu += r
        kareyiTanımla(r)
    }
    ipucu.dizi.map(_.öneAl())
}

val kare2taş = Eşlem.boş[Resim, Taş]
val kare2oda = Eşlem.boş[Resim, Oda]
val oda2kare = Eşlem.boş[Oda, Resim]

val boy = 64
def kare = Resim.dikdörtgen(boy, boy)
def kareyiTanımla(k: Resim) = {
    k.fareyeTıklayınca { (_, _) =>
        var oynadıMı = yanlış
        val oda = kare2oda(k)
        val (str, stn) = (oda.str, oda.stn)
        tahta(str)(stn) match {
            case Boş =>
                val hamle = Oyun(oyuncu, oda)
                val yasal = hamleyiYapmayıDene(hamle)
                if (yasal.size > 0) {
                    oynadıMı = doğru
                    kare2taş += (k -> oyuncu)
                    hamleyiYap(yasal)
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
            case Boş => if (hamleyiYapmayıDene(Oyun(oyuncu, kare2oda(k))).size > 0)
                k.boyamaRenginiKur(taş2renk(oyuncu))
            case _ =>
        }
    }
    k.fareÇıkınca { (_, _) =>
        k.boyamaRenginiKur(taş2renk(kare2taş(k)))
    }

}

def hamleYoksa = {
    val boşlar = for (x <- strArtı; y <- strArtı; if tahta(y)(x) == Boş) yield Oda(y, x)
    (boşlar.filter { oda =>
        hamleyiYapmayıDene(Oyun(oyuncu, oda)).size > 0
    }).size == 0
}
def say(t: Taş) = (for (x <- strArtı; y <- strArtı; if tahta(y)(x) == t) yield 1).size
def bittiKaçKaç = satıryaz(s"Oyun bitti.\nbeyazlar: ${say(Beyaz)}\nsiyahlar: ${say(Siyah)}")
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

trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön

case class Komşu(yön: Yön, oda: Oda)

def hamleyiYapmayıDene(o: Oyun): Dizi[Komşu] = komşularıBul(o.oda) filter { komşu =>
    val komşuTaş = tahta(komşu.oda.str)(komşu.oda.stn)
    komşuTaş != Boş && komşuTaş != oyuncu && sonuDaYasalMı(komşu, oyuncu)
}

def komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
    Komşu(D, Oda(o.str, o.stn + 1)),
    Komşu(B, Oda(o.str, o.stn - 1)),
    Komşu(K, Oda(o.str + 1, o.stn)),
    Komşu(G, Oda(o.str - 1, o.stn)),
    Komşu(KD, Oda(o.str + 1, o.stn + 1)),
    Komşu(KB, Oda(o.str + 1, o.stn - 1)),
    Komşu(GD, Oda(o.str - 1, o.stn + 1)),
    Komşu(GB, Oda(o.str - 1, o.stn - 1))) filter { k => odaMı(k.oda) }
def odaMı(o: Oda) = 0 <= o.str && o.str < odaSayısı && 0 <= o.stn && o.stn < odaSayısı

def sonuDaYasalMı(k: Komşu, oyuncu: Taş): İkil = {
    val sıraTaşlar = gerisi(k).dropWhile { o =>
        val taş = tahta(o.str)(o.stn)
        taş != oyuncu && taş != Boş
    }
    if (sıraTaşlar.isEmpty) yanlış else {
        val oda = sıraTaşlar.head
        tahta(oda.str)(oda.stn) == oyuncu
    }
}

def gerisi(k: Komşu): Dizi[Oda] = {
    val sıra = EsnekDizim.boş[Oda]
    val (x, y) = (k.oda.stn, k.oda.str)
    k.yön match {
        case D => for (i <- x + 1 to sonStr) /* */ sıra += Oda(y, i)
        case B => for (i <- x - 1 to 0 by -1) /**/ sıra += Oda(y, i)
        case K => for (i <- y + 1 to sonStr) /* */ sıra += Oda(i, x)
        case G => for (i <- y - 1 to 0 by -1) /**/ sıra += Oda(i, x)
        case KD => // hem str hem stn artacak
            if (x >= y) for (i <- x + 1 to sonStr) /*         */ sıra += Oda(y + i - x, i)
            else for (i <- y + 1 to sonStr) /*                */ sıra += Oda(i, x + i - y)
        case GB => // hem str hem stn azalacak
            if (x >= y) for (i <- y - 1 to 0 by -1) /*        */ sıra += Oda(i, x - y + i)
            else for (i <- x - 1 to 0 by -1) /*               */ sıra += Oda(y - x + i, i)
        case KB => // str artacak stn azalacak
            if (x + y >= sonStr) for (i <- y + 1 to sonStr) /**/ sıra += Oda(i, x + y - i)
            else for (i <- x - 1 to 0 by -1) /*               */ sıra += Oda(y + x - i, i)
        case GD => // str azalacak stn artacak
            if (x + y >= sonStr) for (i <- x + 1 to sonStr) /**/ sıra += Oda(y + x - i, i)
            else for (i <- y - 1 to 0 by -1) /*               */ sıra += Oda(i, x + y - i)
    }
    sıra.dizi
}

def hamleyiYap(yasal: Dizi[Komşu]): Birim = yasal.foreach { komşu =>
    val komşuTaş = tahta(komşu.oda.str)(komşu.oda.stn)
    gerisi(komşu).takeWhile { oda =>
        val taş = tahta(oda.str)(oda.stn)
        taş != Boş && taş != oyuncu
    }.foreach(taşıAltÜstYap(_))
    taşıAltÜstYap(komşu.oda)
}
def taşıAltÜstYap(oda: Oda): Birim = {
    tahta(oda.str)(oda.stn) = oyuncu
    val k = oda2kare(oda)
    k.boyamaRenginiKur(
        oyuncu match {
            case Beyaz => beyaz
            case Siyah => siyah
        })
    kare2taş += (k, oyuncu)
}

def başlangıçTaşlarınıKur =
    if (odaSayısı % 2 == 0) {
        val (a, b) = (odaSayısı / 2 - 1, odaSayısı / 2)
        tahta(a)(a) = Beyaz //todo
        tahta(b)(b) = Beyaz
        tahta(a)(b) = Siyah
        tahta(b)(a) = Siyah
    }
    else {
        val orta = sayıya(odaSayısı / 2)
        val (a, b) = (orta - 1, orta + 1)
        tahta(a)(a) = Beyaz
        tahta(b)(b) = Beyaz
        tahta(a)(b) = Siyah
        tahta(b)(a) = Siyah
        tahta(a + 1)(a) = Beyaz
        tahta(a + 1)(b) = Beyaz
        tahta(b)(a + 1) = Siyah
        tahta(a)(a + 1) = Siyah
        tahta(b + 1)(b - 1) = Beyaz
        tahta(a - 1)(b - 1) = Beyaz
        tahta(a + 1)(a - 1) = Siyah
        tahta(a + 1)(b + 1) = Siyah
    }

def rastgeleOyun(duraklamaSüresi: Kesir = 1.0 /*saniye*/ ) = {
    var oyna = doğru; val dallanma = EsnekDizim.boş[Sayı]
    while (oyna) {
        val boşlar = for (x <- strArtı; y <- strArtı; if tahta(y)(x) == Boş) yield Oda(y, x)
        def bütünYasalHamleler = boşlar.filter { oda => hamleyiYapmayıDene(Oyun(oyuncu, oda)).size > 0 }
        val yasallar1 = bütünYasalHamleler
        val yasallar = if (yasallar1.size > 0) yasallar1 else {
            sırayıÖbürOyuncuyaGeçir
            satıryaz(s"Yasal hamle yok. Sıra yine ${adı(oyuncu)}ın")
            bütünYasalHamleler
        }
        if (yasallar.size == 0) {
            bittiKaçKaç; satıryaz(dallanma.dizi); satıryaz(s"en irisi: ${dallanma.dizi.max}")
            oyna = yanlış
        }
        else {
            satıryaz(s"${yasallar.size} seçenek var."); dallanma += yasallar.size
            val oda = yasallar.drop(rastgele(yasallar.size)).head
            val kare = oda2kare(oda)
            kare2taş += (kare -> oyuncu)
            hamleyiYap(hamleyiYapmayıDene(Oyun(oyuncu, oda)))
            tahta(oda.str)(oda.stn) = oyuncu
            kare.boyamaRenginiKur(taş2renk(oyuncu))
            sırayıÖbürOyuncuyaGeçir
            tahtayıYaz
            durakla(duraklamaSüresi)
        }
    }
}
silVeSakla
tümEkranTuval()
çıktıyıSil
başlangıçTaşlarınıKur
tahtayıKur
tahtayıYaz
//rastgeleOyun(0.01)