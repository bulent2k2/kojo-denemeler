
trait Taş
case object Beyaz extends Taş { override def toString() = "B" }
case object Siyah extends Taş { override def toString() = "S" }
case object Yok extends Taş { override def toString() = "." }
// satır ve sütün sırası yazılımda ters!
case class Oda(str: Sayı, stn: Sayı) { override def toString() = s"${stn + 1}x${str + 1}" }

val odaSayısı = 8 // satır ve sütunların oda sayısı
gerekli(3 < odaSayısı, "En küçük tahtamız 4x4lük. odaSayısı değerini artırın") // başlangıç taşları sığmıyor
gerekli(20 > odaSayısı, "En büyük tahtamız 19x19luk. odaSayısı değerini azaltın") // çok yavaşlıyor

var oyuncu: Taş = Beyaz // Beyaz ya da Siyah başlayabilir. Seç :-)

var tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Yok)
val kare2oda = Eşlem.boş[Resim, Oda]
val oda2kare = Eşlem.boş[Oda, Resim]

def tahtayıKur(boy: Sayı) = {
    artalanıKur(koyuGri)
    başlangıçTaşlarınıKur
    val içKöşeler = EsnekDizim.boş[Resim]
    val içKöşeKalemRengi = Renk(255, 215, 85, 101) // soluk sarımsı bir renk
    for (x <- satırAralığı; y <- satırAralığı) {
        val kRenk = if ((x == 2 && (y == 2 || y == sonOda - 2)) ||
            (x == sonOda - 2 && (y == 2 || y == sonOda - 2))) içKöşeKalemRengi else mor
        val oda = Oda(y, x)
        val r = kalemRengi(kRenk) * boyaRengi(taş2renk(tahta(y)(x))) * götür(oda2nokta(oda)) -> Resim.dikdörtgen(boy, boy)
        kare2oda += (r -> oda)
        oda2kare += (oda -> r)
        r.çiz()
        if (kRenk == içKöşeKalemRengi) içKöşeler += r
        kareyiTanımla(r)
    }
    içKöşeler.dizi.map(_.öneAl())
    hamleSayısı = 1
    tahtayıYaz
}
val boşOdaRengi = Renk(10, 111, 23) // koyuYeşil
val sonOda = odaSayısı - 1
val satırAralığı = 0 to sonOda
val satırAralığıSondan = sonOda to 0 by -1

val (xoffset, yoffset) = (-320, -256)
def oda2nokta(oda: Oda, solaltKöşe: İkil = doğru) =
    if (solaltKöşe) Nokta(xoffset + oda.stn * boy, yoffset + oda.str * boy)
    else Nokta(xoffset + oda.stn * boy + b2, yoffset + oda.str * boy + b2)
val boy = 64
val (b2, b4, b2p5, b9o20) = (boy / 2, boy / 4, boy / 2.5, boy * 9 / 20)
def kareyiTanımla(k: Resim) = {
    val oda = kare2oda(k)
    k.fareyeTıklayınca { (_, _) =>
        var oynadıMı = yanlış
        tahta(oda.str)(oda.stn) match {
            case Yok =>
                val yasal = hamleyiDene(oda)
                if (yasal.size > 0) {
                    oynadıMı = doğru
                    hamleyiYap(yasal, oda)
                }
            case _ =>
        }
        if (oynadıMı && hamleYoksa) {
            sırayıÖbürOyuncuyaGeçir
            if (hamleYoksa) bittiKaçKaç else satıryaz(s"Yasal hamle yok. Sıra yine ${adı(oyuncu)}ın")
        }
    }
    def odaRengi = taş2renk(tahta(oda.str)(oda.stn))
    var ipucu: Resim = Resim.yazıRenkli("?", 10, kırmızı)
    def renk = taş2renk(oyuncu)
    k.fareGirince { (x, y) =>
        val n = oda2nokta(oda, yanlış) - Nokta(b2, -b2)
        def ipucunuKur(yazı: Yazı) = götür(n) -> Resim.yazıRenkli(yazı, 20, kırmızı)
        tahta(oda.str)(oda.stn) match {
            case Yok => if (hamleyiDene(oda).size > 0) {
                k.boyamaRenginiKur(renk)
                ipucu = ipucunuKur(s"${hamleGetirisi(oda)}")
            }
            else {
                ipucu = ipucunuKur(s"$oda")
            }
            case _ => ipucu = ipucunuKur(s"$oda")
        }
        çiz(ipucu)
        ipucu.girdiyiAktar(k)
    }
    k.fareÇıkınca { (_, _) =>
        k.boyamaRenginiKur(odaRengi)
        ipucu.sil()
    }

}

def tahtayıYaz = {
    for (y <- satırAralığıSondan) satıryaz(tahta(y).mkString(" "))
    val yasal = bütünYasalHamleler()
    satıryaz(s"Hamle: $hamleSayısı. Sıra ${adı(oyuncu)}ın")
}

var hamleSayısı = 0 // bir sonraki hamle kaçıncı hamle olacak? 1, 2, 3, ...
def adı(o: Taş) = if (o == Siyah) "siyah" else "beyaz"
def hamleYoksa = bütünYasalHamleler().size == 0
def say(t: Taş) = (for (x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == t) yield 1).size
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
    seçenekleriGöster
}

trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön

case class Komşu(yön: Yön, oda: Oda)

def bütünYasalHamleler() = (for (x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == Yok) yield Oda(y, x)).
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
    Komşu(D, Oda(o.str, o.stn + 1)), Komşu(B, Oda(o.str, o.stn - 1)),
    Komşu(K, Oda(o.str + 1, o.stn)), Komşu(G, Oda(o.str - 1, o.stn)),
    Komşu(KD, Oda(o.str + 1, o.stn + 1)), Komşu(KB, Oda(o.str + 1, o.stn - 1)),
    Komşu(GD, Oda(o.str - 1, o.stn + 1)), Komşu(GB, Oda(o.str - 1, o.stn - 1))) filter {
        k => odaMı(k.oda)
    }
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
        case D => for (i <- x + 1 to sonOda) /* */ sıra += Oda(y, i)
        case B => for (i <- x - 1 to 0 by -1) /**/ sıra += Oda(y, i)
        case K => for (i <- y + 1 to sonOda) /* */ sıra += Oda(i, x)
        case G => for (i <- y - 1 to 0 by -1) /**/ sıra += Oda(i, x)
        case KD => // hem str hem stn artacak
            if (x >= y) for (i <- x + 1 to sonOda) /*         */ sıra += Oda(y + i - x, i)
            else for (i <- y + 1 to sonOda) /*                */ sıra += Oda(i, x + i - y)
        case GB => // hem str hem stn azalacak
            if (x >= y) for (i <- y - 1 to 0 by -1) /*        */ sıra += Oda(i, x - y + i)
            else for (i <- x - 1 to 0 by -1) /*               */ sıra += Oda(y - x + i, i)
        case KB => // str artacak stn azalacak
            if (x + y >= sonOda) for (i <- y + 1 to sonOda) /**/ sıra += Oda(i, x + y - i)
            else for (i <- x - 1 to 0 by -1) /*               */ sıra += Oda(y + x - i, i)
        case GD => // str azalacak stn artacak
            if (x + y >= sonOda) for (i <- x + 1 to sonOda) /**/ sıra += Oda(y + x - i, i)
            else for (i <- y - 1 to 0 by -1) /*               */ sıra += Oda(i, x + y - i)
    }
    sıra.dizi
}

def hamleyiYap(yasal: Dizi[Komşu], hane: Oda): Birim = {
    def bütünTuşlarıÇevir = yasal.foreach { komşu =>
        val komşuTaş = tahta(komşu.oda.str)(komşu.oda.stn)
        gerisi(komşu).takeWhile { oda =>
            val taş = tahta(oda.str)(oda.stn)
            taş != Yok && taş != oyuncu
        }.foreach(taşıAltÜstYap(_))
        taşıAltÜstYap(komşu.oda)
    }
    saklaTahtayı(doğru) // yeni hamle
    bütünTuşlarıÇevir
    tahta(hane.str)(hane.stn) = oyuncu
    oda2kare(hane).boyamaRenginiKur(taş2renk(oyuncu))
    sırayıÖbürOyuncuyaGeçir
    hamleSayısı += 1
    yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    tahtayıYaz
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
    for (x <- satırAralığı; y <- satırAralığı) boşalt(Oda(y, x))
    başlangıçTaşlarınıKur
    for (x <- satırAralığı; y <- satırAralığı) {
        val taş = tahta(y)(x)
        if (taş != Yok)
            oda2kare(Oda(y, x)).boyamaRenginiKur(taş2renk(taş))
    }
    oyuncu = Beyaz
    hamleSayısı = 1
    eskiTahtalar.a.clear() // todo
    oyuncular.a.clear()
}
def boşalt(oda: Oda): Birim = {
    tahta(oda.str)(oda.stn) = Yok
    oda2kare(oda).boyamaRenginiKur(boşOdaRengi)
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
                satıryaz(s"${d.size} dallanma oldu. Dal sayıları: ${d.mkString(",")}")
                satıryaz(s"Ortalama dal sayısı: ${yuvarla(d.sum / (1.0 * d.size), 1)}")
                satıryaz(s"En iri dal sayısı: ${d.max}")
            }
            oyna = yanlış
        }
        else {
            if (sıraAtla) satıryaz(s"Yasal hamle yok. Sıra yine ${adı(oyuncu)}ın")
            satıryaz(s"${yasallar.size} seçenek var."); dallanma += yasallar.size
            val oda = yaklaşım(yasallar)
            hamleyiYap(hamleyiDene(oda), oda)
            tahtayıYaz
            durakla(duraklamaSüresi)
        }
    }
}
def rastgeleHamle(yasallar: Dizi[Oda]): Oda =
    yasallar.drop(rastgele(yasallar.size)).head // çağıran metod yasalların boş olmadığını biliyor
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

def seçenekleriGöster = {
    if (seçeneklerAçık) {
        seçenekResimleri.foreach { r => r.sil() }
        val sıralı = bütünYasalHamleler.map { oda => (oda, hamleGetirisi(oda)) }.sortBy { p => p._2 }.reverse
        if (sıralı.size > 0) {
            val enİriGetiri = sıralı.head._2
            seçenekResimleri = sıralı map {
                case (oda, getirisi) =>
                    val renk = if (getirisi == enİriGetiri) sarı else turuncu
                    val göster = götür(oda2nokta(oda, yanlış)) * kalemRengi(renk) * kalemBoyu(3) *
                        boyaRengi(renksiz) -> Resim.daire(b4)
                    göster.girdiyiAktar(oda2kare(oda))
                    göster.çiz()
                    göster
            }
        }
    }
    else {
        seçenekResimleriniSil
    }
}
var seçenekResimleri: Dizi[Resim] = Dizi()
def seçenekResimleriniSil = {
    seçenekResimleri.foreach { r => r.sil() }
    seçeneklerAçık = yanlış
}
var seçeneklerAçık = yanlış
def seçenekleriAçKapa(d: Resim) = {
    seçeneklerAçık = if (seçeneklerAçık) yanlış else doğru
    seçenekleriGöster
    val renk = if (seçeneklerAçık) siyah else beyaz
    d.fareGirince { (_, _) => d.kalemRenginiKur(renk) }
    d.fareÇıkınca { (_, _) => d.kalemRenginiKur(renk) }
    if (!seçeneklerAçık) seçenekleriKapa(d)
}
def seçenekleriKapa(d: Resim) = {
    seçenekResimleriniSil
    d.fareGirince { (_, _) => d.kalemRenginiKur(siyah) }
    d.fareÇıkınca { (_, _) => d.kalemRenginiKur(renksiz) }
    d.kalemRenginiKur(renksiz)
}

val eskiTahtalar = EsnekDizim.boş[Dizim[Array[Taş]]] // todo
val oyuncular = EsnekDizim.boş[Taş]
def saklaTahtayı(yeniHamleMi: İkil) = {
    if (yeniHamleMi) while (hamleSayısı <= eskiTahtalar.sayı) {
        eskiTahtalar.a.remove(eskiTahtalar.sayı - 1) // todo
        oyuncular.a.remove(oyuncular.sayı - 1)
    }
    val yeni = Dizim.boş[Taş](odaSayısı, odaSayısı)
    for (x <- satırAralığı; y <- satırAralığı)
        yeni(y)(x) = tahta(y)(x)
    eskiTahtalar += yeni
    oyuncular += oyuncu
}
def verilenHamleTahtasınaGeç(hamle: Sayı) = {
    val eski = eskiTahtalar(hamle)
    for (x <- satırAralığı; y <- satırAralığı)
        tahta(y)(x) = eski(y)(x)
    oyuncu = oyuncular(hamle)
    for (y <- satırAralığı; x <- satırAralığı)
        oda2kare(Oda(y, x)).boyamaRenginiKur(taş2renk(tahta(y)(x)))
}
var yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru // yeni bir hamleden önce geri/ileri çalışmaz ki
def geriAl = if (hamleSayısı > 1) {
    if (yeniHamleEnYeniGeriAlKomutundanDahaGüncel) {
        yeniHamleEnYeniGeriAlKomutundanDahaGüncel = yanlış
        saklaTahtayı(yanlış)
    }
    hamleSayısı -= 1
    verilenHamleTahtasınaGeç(hamleSayısı - 1)
}
def ileriGit = if (hamleSayısı < eskiTahtalar.sayı) {
    verilenHamleTahtasınaGeç(hamleSayısı)
    hamleSayısı += 1
}

def düğmeleriKur = {
    def düğme(x: Kesir, y: Kesir, yarıçap: Kesir, mesaj: Yazı = "") = {
        val d = götür(x, y) * kalemRengi(renksiz) -> Resim.dizi(
            götür(b2p5, b2p5) -> Resim.yazıRenkli(mesaj, 10, siyah),
            kalemBoyu(3) -> Resim.daire(yarıçap))
        d.çiz(); d
    }
    def tepkili(d: Resim) = {
        d.fareGirince { (_, _) => d.kalemRenginiKur(beyaz) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(renksiz) }
    }
    val (dx, dy) = ((0.5 + odaSayısı) * boy + xoffset, yoffset + b2)
    val d3a = {
        val d = düğme(dx, dy + 2 * boy, b9o20, "seçenekler")
        d.boyamaRenginiKur(sarı)
        d.fareyeTıklayınca { (_, _) => seçenekleriAçKapa(d) }
        tepkili(d); d
    }
    val d3b = {
        val d = düğme(dx + boy, dy + 2 * boy, b9o20, "tüm ekran aç/kapa")
        d.boyamaRenginiKur(turuncu)
        d.fareyeTıklayınca { (_, _) => tümEkranTuval() }
        tepkili(d)
    }
    val d1 = {
        val d = düğme(dx, dy, b9o20, "özdevin")
        d.boyamaRenginiKur(kırmızı)
        d.fareyeTıklayınca { (_, _) =>
            seçenekleriKapa(d3a)
            zamanTut("Özdevinimli oyun") {
                // öbür yaklaşımlar: rastgeleHamle, enİriGetiriliHamle
                özdevinimliOyun(enİriGetirililerArasındanRastgele)
            }("sürdü")
        }
        tepkili(d)
    }
    val d2 = {
        val d = düğme(dx, dy + boy, b9o20, "yeni oyun")
        d.boyamaRenginiKur(mavi)
        d.fareyeTıklayınca { (_, _) => seçenekleriKapa(d3a); yeniOyun }
        tepkili(d)
    }
    val d4a = {
        val d = düğme(dx, dy + 3 * boy, b2 * 9 / 10, "geri")
        d.boyamaRenginiKur(açıkGri)
        d.fareyeTıklayınca { (_, _) => geriAl; seçenekleriGöster }
        tepkili(d)
    }
    val d4b = {
        val d = düğme(dx + boy, dy + 3 * boy, b2 * 9 / 10, "ileri")
        d.boyamaRenginiKur(renkler.blanchedAlmond)
        d.fareyeTıklayınca { (_, _) => ileriGit; seçenekleriGöster }
        tepkili(d)
    }
}
silVeSakla
çıktıyıSil
tahtayıKur(boy)
düğmeleriKur
zamanTut("Oyun") { özdevinimliOyun(enİriGetirililerArasındanRastgele, 0.02) }("sürdü")
