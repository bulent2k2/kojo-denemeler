
trait Taş
case object Beyaz extends Taş { override def toString() = "B" }
case object Siyah extends Taş { override def toString() = "S" }
case object Yok extends Taş { override def toString() = "." }

case class Oda(str: Sayı, stn: Sayı) { override def toString() = s"(${str + 1},${stn + 1})" }

val odaSayısı = 8 // bir satır ya da sütundaki oda sayısı
gerekli(3 < odaSayısı, "En küçük tahtamız 4x4lük. odaSayısı değerini artırın") // başlangıç taşları sığmıyor
gerekli(20 > odaSayısı, "En büyük tahtamız 19x19luk. odaSayısı değerini azaltın") // çok yavaşlıyor
val tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Yok)
val sonStr = odaSayısı - 1
val strArtı = 0 to sonStr
val strEksi = sonStr to 0 by -1

var oyuncu: Taş = Beyaz // Beyaz ya da Siyah başlayabilir. Seç :-)

var hamleSayısı = 0
def tahtayıYaz = {
    for (y <- strEksi) satıryaz(tahta(y).mkString(" "))
    hamleSayısı += 1
    val enİriler = enGetirililer()
    val getiri = if (enİriler.isEmpty) 0 else hamleGetirisi(enİriler.head)
    val odaYazı = if (enİriler.size > 1) "Odalar" else "Oda"
    val yasal = bütünYasalHamleler()
    satıryaz(s"Oyun: $hamleSayısı." +
        s" Sıra ${adı(oyuncu)}ın." +
        s" ${yasal.size} seçenek var" +
        (if (yasal.size > 0)
            s". En iri getiri $getiri" +
            (if (getiri <= 1) "" else s". $odaYazı: ${enİriler.mkString(", ")}")
        else "")
    )
}

def adı(o: Taş) = if (o == Siyah) "siyah" else "beyaz"

val boşOdaRengi = Renk(10, 111, 23) // koyuYeşil

val (xoffset, yoffset) = (-320, -256)

def tahtayıKur = {
    val ipucu = EsnekDizim.boş[Resim]
    val içKöşeKalemRengi = Renk(255, 215, 85, 101) // soluk sarımsı bir renk
    for (x <- strArtı; y <- strArtı) {
        val renk = tahta(y)(x) match {
            case Yok   => boşOdaRengi
            case Siyah => siyah
            case Beyaz => beyaz
        }
        val kRenk = if ((x == 2 && (y == 2 || y == sonStr - 2)) ||
            (x == sonStr - 2 && (y == 2 || y == sonStr - 2))) içKöşeKalemRengi else mor
        val r = kalemRengi(kRenk) * boyaRengi(renk) * götür(xoffset + x * boy, yoffset + y * boy) -> kare
        kare2oda += (r -> Oda(y, x))
        oda2kare += (Oda(y, x) -> r)
        çiz(r)
        if (kRenk == içKöşeKalemRengi) ipucu += r
        kareyiTanımla(r)
    }
    ipucu.dizi.map(_.öneAl())
}

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
            case Yok =>
                val yasal = hamleyiDene(oda)
                if (yasal.size > 0) {
                    oynadıMı = doğru
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
                else satıryaz(s"Yasal hamle yok. Sıra yine ${adı(oyuncu)}ın")
            }
        }
    }
    k.fareGirince { (_, _) =>
        val oda = kare2oda(k)
        tahta(oda.str)(oda.stn) match {
            case Yok => if (hamleyiDene(oda).size > 0) {
                k.boyamaRenginiKur(taş2renk(oyuncu))
                satıryaz(s"$oda getirisi ${hamleGetirisi(oda)}")
            }
            case _ =>
        }
    }
    k.fareÇıkınca { (_, _) =>
        val oda = kare2oda(k)
        k.boyamaRenginiKur(taş2renk(tahta(oda.str)(oda.stn)))
    }

}

def hamleYoksa = bütünYasalHamleler().size == 0
def say(t: Taş) = (for (x <- strArtı; y <- strArtı; if tahta(y)(x) == t) yield 1).size
def bittiKaçKaç = satıryaz(s"Oyun bitti.\nbeyazlar: ${say(Beyaz)}\nsiyahlar: ${say(Siyah)}")
def taş2renk(t: Taş) = t match {
    case Yok   => boşOdaRengi
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

def bütünYasalHamleler() = (for (x <- strArtı; y <- strArtı; if tahta(y)(x) == Yok) yield Oda(y, x)).
    filter { hamleyiDene(_).size > 0 }

def hamleyiDene(oda: Oda): Dizi[Komşu] = komşularıBul(oda) filter { komşu =>
    val komşuTaş = tahta(komşu.oda.str)(komşu.oda.stn)
    komşuTaş != Yok && komşuTaş != oyuncu && sonuDaYasalMı(komşu, oyuncu)._1
}

def hamleGetirisi(oda: Oda): Sayı = {
    val getiriliYönler = komşularıBul(oda) map { komşu =>
        val komşuTaş = tahta(komşu.oda.str)(komşu.oda.stn)
        val sonunaKadar = sonuDaYasalMı(komşu, oyuncu)
        if (komşuTaş != Yok && komşuTaş != oyuncu && sonunaKadar._1) sonunaKadar._2 else 0
    }
    getiriliYönler.sum
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

def sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
    val diziTaşlar = gerisi(k)
    val sıraTaşlar = diziTaşlar.dropWhile { o =>
        val taş = tahta(o.str)(o.stn)
        taş != oyuncu && taş != Yok
    }
    if (sıraTaşlar.isEmpty) (yanlış, 0) else {
        val oda = sıraTaşlar.head

        (tahta(oda.str)(oda.stn) == oyuncu, 1 + diziTaşlar.size - sıraTaşlar.size)
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
        taş != Yok && taş != oyuncu
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
}

def yeniOyun = {
    for (x <- strArtı; y <- strArtı) boşalt(Oda(y, x))
    başlangıçTaşlarınıKur
    for (x <- strArtı; y <- strArtı) {
        val kare = oda2kare(Oda(y, x))
        val taş = tahta(y)(x)
        if (taş != Yok) kare.boyamaRenginiKur(taş2renk(taş))
    }
    oyuncu = Beyaz
}
def boşalt(oda: Oda): Birim = {
    tahta(oda.str)(oda.stn) = Yok
    val k = oda2kare(oda)
    k.boyamaRenginiKur(boşOdaRengi)
}
def başlangıçTaşlarınıKur = {
    val orta = sayıya(odaSayısı / 2)
    val a = orta - 1
    val b = if (odaSayısı % 2 == 0) orta else orta + 1
    tahta(a)(a) = Beyaz
    tahta(b)(b) = Beyaz
    tahta(a)(b) = Siyah
    tahta(b)(a) = Siyah
    if (odaSayısı % 2 != 0) {
        tahta(a)(a + 1) = Siyah
        tahta(b)(a + 1) = Siyah
        tahta(a + 1)(a) = Beyaz
        tahta(a + 1)(b) = Beyaz
        tahta(b + 1)(b - 1) = Beyaz
        tahta(a - 1)(b - 1) = Beyaz
        tahta(a + 1)(a - 1) = Siyah
        tahta(a + 1)(b + 1) = Siyah
    }
}

def özdevinimliOyun(
    yaklaşım:        İşlev1[Dizi[Oda], Oda],
    duraklamaSüresi: Kesir                  = 0.0 /*saniye*/
) = {
    var oyna = doğru; val dallanma = EsnekDizim.boş[Sayı]
    while (oyna) {
        val yasallar1 = bütünYasalHamleler()
        var sıraAtla = yanlış
        val yasallar = if (yasallar1.size > 0) yasallar1 else {
            sırayıÖbürOyuncuyaGeçir
            sıraAtla = doğru
            bütünYasalHamleler
        }
        if (yasallar.size == 0) {
            bittiKaçKaç
            if (dallanma.sayı > 0) {
                val d = dallanma.dizi
                satıryaz(s"Dallanma: ${d.mkString(",")}")
                satıryaz(s"Ortalaması: ${yuvarla(d.sum / (1.0 * d.size), 1)}")
                satıryaz(s"En irisi: ${d.max}")
            }
            oyna = yanlış
        }
        else {
            if (sıraAtla) satıryaz(s"Yasal hamle yok. Sıra yine ${adı(oyuncu)}ın")
            satıryaz(s"${yasallar.size} seçenek var."); dallanma += yasallar.size
            val oda = yaklaşım(yasallar)
            hamleyiYap(hamleyiDene(oda))
            tahta(oda.str)(oda.stn) = oyuncu
            oda2kare(oda).boyamaRenginiKur(taş2renk(oyuncu))
            sırayıÖbürOyuncuyaGeçir
            tahtayıYaz
            durakla(duraklamaSüresi)
        }
    }
}
def rastgeleHamle(yasallar: Dizi[Oda]): Oda =
    yasallar.drop(rastgele(yasallar.size)).head
def enİriGetiriliHamle(yasallar: Dizi[Oda]): Oda =
    yasallar maxBy { oda => hamleGetirisi(oda) }
def enİriGetirililerArasındanRastgele(yasallar: Dizi[Oda]): Oda = {
    val seçenekler = enGetirililer()
    seçenekler.drop(rastgele(seçenekler.size)).head
}
def enGetirililer(): Dizi[Oda] = {
    def bütünEnİriler[A, B: Ordering](d: Dizin[A])(iş: A => B): Dizin[A] = {
        d.sortBy(iş).reverse match {
            case Dizin()       => Dizin()
            case baş :: kuyruk => baş :: kuyruk.takeWhile { oda => iş(oda) == iş(baş) }
        }
    }
    bütünEnİriler(bütünYasalHamleler().toList) { hamleGetirisi(_) }
}

def düğmeleriKur = {
    def düğme(x: Kesir, y: Kesir, yarıçap: Kesir) = {
        val k = götür(x, y) * kalemRengi(renksiz) -> Resim.daire(yarıçap)
        çiz(k)
        k
    }
    val kx = (0.5 + odaSayısı) * boy + xoffset
    val b2 = boy / 2
    val ky = yoffset + b2
    val d1 = düğme(kx, ky, b2 * 9 / 10)
    d1.boyamaRenginiKur(mavi)
    d1.fareyeTıklayınca { (_, _) => yeniOyun }
    def tepkili(d: Resim) = {
        d.fareGirince { (_, _) => d.saydamlığıKur(0.5) }
        d.fareÇıkınca { (_, _) => d.saydamlığıKur(1.0) }
    }
    tepkili(d1)
    val d2 = düğme(kx, ky + boy, b2 * 9 / 10)
    d2.boyamaRenginiKur(kırmızı)
    d2.fareyeTıklayınca { (_, _) =>
        zamanTut("Özdevinimli oyun") {
            // öbür iki yaklaşım: rastgeleHamle, enİriGetiriliHamle
            özdevinimliOyun(enİriGetirililerArasındanRastgele)
        }("sürdü")
    }
    tepkili(d2)
}
silVeSakla
çıktıyıSil
başlangıçTaşlarınıKur
tahtayıKur
düğmeleriKur
tahtayıYaz
//zamanTut("Oyun"){özdevinimliOyun(enİriGetirililerArasındanRastgele, 0.05)}("sürdü")