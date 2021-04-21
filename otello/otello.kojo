
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

val tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Yok)
val odanınKaresi = Eşlem.boş[Oda, Resim]

val sonOda = odaSayısı - 1
val satırAralığı = 0 to sonOda
val satırAralığıSondan = sonOda to 0 by -1

def tahtayıYaz = for (y <- satırAralığıSondan) satıryaz(tahta(y).mkString(" "))

var hamleSayısı = 0 // bir sonraki hamle kaçıncı hamle olacak? 1, 2, 3, ...
def adı(o: Taş) = if (o == Siyah) "siyah" else "beyaz"
def hamleYoksa = bütünYasalHamleler.size == 0
def say(t: Taş) = (for (x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == t) yield 1).size
def kaçkaç(kısa: İkil = yanlış) = if (kısa) s"B: ${say(Beyaz)}\nS: ${say(Siyah)}"
else s"Beyazlar: ${say(Beyaz)}\nSiyahlar: ${say(Siyah)}"
def bittiKaçKaç = satıryaz(s"Oyun bitti.\n${kaçkaç()}")
def sırayıÖbürOyuncuyaGeçir = {
    oyuncu = oyuncu match {
        case Beyaz => Siyah
        case Siyah => Beyaz
    }
    araYüz.seçenekleriGöster
}

trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön

case class Komşu(yön: Yön, oda: Oda)

def bütünYasalHamleler = (for (x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == Yok) yield Oda(y, x)).
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

def hamleyiYap(yasal: Dizi[Komşu], hane: Oda, duraklamaSüresi: Kesir = 0.0): Birim = {
    def bütünTuşlarıÇevir = yasal.foreach { komşu =>
        val komşuTaş = tahta(komşu.oda.str)(komşu.oda.stn)
        gerisi(komşu).takeWhile { oda =>
            val taş = tahta(oda.str)(oda.stn)
            taş != Yok && taş != oyuncu
        }.foreach(taşıAltÜstYap(_))
        taşıAltÜstYap(komşu.oda)
    }
    saklaTahtayı(doğru, sonHamle)
    sonHamle = Biri(hane)
    bütünTuşlarıÇevir
    tahta(hane.str)(hane.stn) = oyuncu
    araYüz.boya(hane, oyuncu)
    satıryaz(s"Hamle $hamleSayısı $oyuncu $hane:")
    tahtayıYaz
    sırayıÖbürOyuncuyaGeçir
    hamleSayısı += 1
    yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    araYüz.skoruGüncelle
    araYüz.hamleyiGöster(hane)
    if (duraklamaSüresi > 0) durakla(duraklamaSüresi)
}
var sonHamle: Belki[Oda] = Hiçbiri // sadece son hamleyi tuvalde göstermek için gerekli
def taşıAltÜstYap(oda: Oda): Birim = {
    tahta(oda.str)(oda.stn) = oyuncu
    val k = odanınKaresi(oda)
    k.boyamaRenginiKur(
        oyuncu match {
            case Beyaz => beyaz
            case Siyah => siyah
        })
}

def yeniOyun = if (hamleSayısı != 1) {
    for (x <- satırAralığı; y <- satırAralığı) boşalt(Oda(y, x))
    başlangıçTaşlarınıKur
    for (x <- satırAralığı; y <- satırAralığı) if (tahta(y)(x) != Yok)
        araYüz.boya(Oda(y, x), tahta(y)(x))
    oyuncu = Beyaz
    hamleSayısı = 1
    satıryaz("Yeni oyun:")
    tahtayıYaz
    eskiTahtalar.sil()
    oyuncular.sil()
    hamleler.sil()
    sonHamle = Hiçbiri
    araYüz.skoruGüncelle
    araYüz.hamleResminiSil
}
def boşalt(oda: Oda): Birim = {
    tahta(oda.str)(oda.stn) = Yok
    araYüz.boya(oda, Yok)
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

def özdevin(süre: Kesir = 0.0) = zamanTut("Özdevinimli oyun") {
    özdevinimliOyun(köşeYaklaşımı, süre)
}("sürdü")
def özdevinimliOyun( // özdevinim ve bir kaç hamle seçme yöntemi/yaklaşımı (heuristic)
    yaklaşım:        İşlev1[Dizi[Oda], Belki[Oda]],
    duraklamaSüresi: Kesir /*saniye*/
) = {
    val dallanma = EsnekDizim.boş[Sayı]
    var oyna = doğru
    while (oyna) yaklaşım(bütünYasalHamleler) match {
        case Biri(oda) =>
            dallanma += bütünYasalHamleler.size
            hamleyiYap(hamleyiDene(oda), oda, duraklamaSüresi)
        case _ =>
            sırayıÖbürOyuncuyaGeçir
            yaklaşım(bütünYasalHamleler) match {
                case Biri(oda) =>
                    satıryaz(s"Yasal hamle yok. Sıra yine ${adı(oyuncu)}ın")
                    dallanma += bütünYasalHamleler.size
                    hamleyiYap(hamleyiDene(oda), oda, duraklamaSüresi)
                case _ =>
                    bittiKaçKaç
                    if (dallanma.sayı > 0) {
                        val d = dallanma.dizi
                        satıryaz(s"${d.size} dallanma oldu. Dal sayıları: ${d.mkString(",")}")
                        satıryaz(s"Ortalama dal sayısı: ${yuvarla(d.sum / (1.0 * d.size), 1)}")
                        satıryaz(s"En iri dal sayısı: ${d.max}")
                    }
                    oyna = yanlış
            }
    }
}
def öneri: Birim = köşeYaklaşımı(bütünYasalHamleler) match {
    case Biri(oda) => hamleyiYap(hamleyiDene(oda), oda)
    case _ =>
        sırayıÖbürOyuncuyaGeçir
        köşeYaklaşımı(bütünYasalHamleler) match {
            case Biri(oda) => hamleyiYap(hamleyiDene(oda), oda)
            case _         => bittiKaçKaç
        }
}
def rastgeleSeç[T](dizi: Dizi[T]): Belki[T] = if (dizi.isEmpty) Hiçbiri else
    Biri(dizi.drop(rastgele(dizi.size)).head)
def rastgeleHamle(yasallar: Dizi[Oda]): Belki[Oda] = rastgeleSeç(yasallar)
def enİriGetiriliHamle(yasallar: Dizi[Oda]): Belki[Oda] = if (yasallar.isEmpty) Hiçbiri else
    Biri(yasallar maxBy { oda => hamleGetirisi(oda) })
def enİriGetirililerArasındanRastgele(yasallar: Dizi[Oda]): Belki[Oda] =
    rastgeleSeç(enGetirililer(yasallar))

def enGetirililer(yasallar: Dizi[Oda]): Dizi[Oda] = {
    def bütünEnİriler[A, B: Ordering](d: Dizin[A])(iş: A => B): Dizin[A] = {
        d.sortBy(iş).reverse match {
            case Dizin()       => Dizin()
            case baş :: kuyruk => baş :: kuyruk.takeWhile { oda => iş(oda) == iş(baş) }
        }
    }
    bütünEnİriler(yasallar.toList) { hamleGetirisi(_) }
}
def köşeYaklaşımı(yasallar: Dizi[Oda]): Belki[Oda] = rastgeleSeç(yasallar.filter(köşeMi(_))) match {
    case Biri(oda) => Biri(oda) // köşe bulduk!
    case _ => rastgeleSeç(yasallar.filter(içKöşeMi(_))) match {
        case Biri(oda) => Biri(oda)
        case _ => { // tuzakKenarlar tuzakKöşeleri içeriyor
            val tuzakKenarOlmayanlar = yasallar.filterNot(tuzakKenarMı(_))
            enİriGetirililerArasındanRastgele(
                if (!tuzakKenarOlmayanlar.isEmpty) tuzakKenarOlmayanlar
                else { // tuzak kenarlardan getirisi en iri olanlardan seçiyoruz
                    val tuzakKöşeOlmayanlar = yasallar.filterNot(tuzakKöşeMi(_))
                    if (tuzakKöşeOlmayanlar.isEmpty) yasallar else tuzakKöşeOlmayanlar
                }
            )
        }
    }
}

def tuzakKenarMı: Oda => İkil = {
    case Oda(str, stn) => str == 1 || stn == 1 || str == sonOda - 1 || stn == sonOda - 1
}
def tuzakKöşeMi: Oda => İkil = {
    case Oda(y, x) => (x == 1 && (y == 1 || y == sonOda - 1)) ||
        (x == sonOda - 1 && (y == 1 || y == sonOda - 1))
}
def köşeMi: Oda => İkil = {
    case Oda(str, stn) => if (str == 0) stn == 0 || stn == sonOda else
        str == sonOda && (stn == 0 || stn == sonOda)
}
def içKöşeMi: Oda => İkil = {
    case Oda(y, x) => (x == 2 && (y == 2 || y == sonOda - 2)) ||
        (x == sonOda - 2 && (y == 2 || y == sonOda - 2))
}

// ileri geri gidiş için gerekli bellek
val eskiTahtalar = EsnekDizim.boş[Dizim[Array[Taş]]] // todo
val oyuncular = EsnekDizim.boş[Taş]
val hamleler = EsnekDizim.boş[Belki[Oda]]
def saklaTahtayı(yeniHamleMi: İkil, hane: Belki[Oda]) = {
    if (yeniHamleMi) while (hamleSayısı <= eskiTahtalar.sayı) {
        eskiTahtalar.çıkar(eskiTahtalar.sayı - 1)
        oyuncular.çıkar(oyuncular.sayı - 1)
        hamleler.çıkar(hamleler.sayı - 1)
    }
    val yeni = Dizim.boş[Taş](odaSayısı, odaSayısı)
    for (x <- satırAralığı; y <- satırAralığı)
        yeni(y)(x) = tahta(y)(x)
    eskiTahtalar += yeni
    oyuncular += oyuncu
    hamleler += hane
}
def verilenHamleTahtasınaGeç(hamle: Sayı) = {
    val eski = eskiTahtalar(hamle)
    for (x <- satırAralığı; y <- satırAralığı)
        tahta(y)(x) = eski(y)(x)
    for (y <- satırAralığı; x <- satırAralığı)
        araYüz.boya(Oda(y, x), tahta(y)(x))
    oyuncu = oyuncular(hamle)
    sonHamle = hamleler(hamle)
    sonHamle match {
        case Biri(hane) => araYüz.hamleyiGöster(hane)
        case _          => araYüz.hamleResminiSil
    }
}
var yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru // yeni bir hamleden önce geri/ileri çalışmaz ki
def geriAl = if (hamleSayısı > 1) {
    if (yeniHamleEnYeniGeriAlKomutundanDahaGüncel) {
        yeniHamleEnYeniGeriAlKomutundanDahaGüncel = yanlış
        saklaTahtayı(yanlış, sonHamle)
    }
    hamleSayısı -= 1
    verilenHamleTahtasınaGeç(hamleSayısı - 1)
    araYüz.skoruGüncelle
}
def ileriGit = if (hamleSayısı < eskiTahtalar.sayı) {
    verilenHamleTahtasınaGeç(hamleSayısı)
    hamleSayısı += 1
    araYüz.skoruGüncelle
}

class arayüz() { // tahtayı ve taşları çizelim ve canlandıralım
    /* arayüz sınıfının arayüzünde şunlar var sadece:
       boya(hane, oyuncu), hamleyiGöster(hane), hamleResminiSil
       seçenekleriGöster, skoruGüncelle */
    private val boy = 60 // karelerin boyu inç başına nokta sayısı. 64 => 1cm'de yaklaşık 25 nokta var (/ 64 2.54)
    private val (xKöşe, yKöşe) = (-odaSayısı / 2 * boy, -odaSayısı / 2 * boy) // tahtayı ortalamak için sol alt köşesini belirle
    private val (b2, b3, b4) = (boy / 2, boy / 3, boy / 4)
    private val kareninOdası = Eşlem.boş[Resim, Oda]

    private def tahtayıKur = {
        silVeSakla; çıktıyıSil; tümEkranTuval; artalanıKur(koyuGri)
        başlangıçTaşlarınıKur
        val içKöşeler = EsnekDizim.boş[Resim]
        val içKöşeKalemRengi = Renk(255, 215, 85, 101) // soluk sarımsı bir renk
        for (x <- satırAralığı; y <- satırAralığı) {
            val oda = Oda(y, x)
            val kRenk = if (içKöşeMi(oda)) içKöşeKalemRengi else mor
            val r = kalemRengi(kRenk) * boyaRengi(taşınRengi(tahta(y)(x))) *
                götür(oda2nokta(oda)) -> Resim.dikdörtgen(boy, boy)
            r.çiz()
            kareninOdası += (r -> oda)
            odanınKaresi += (oda -> r)
            if (kRenk == içKöşeKalemRengi) içKöşeler += r
            kareyiTanımla(r)
        }
        içKöşeler.dizi.map(_.öneAl())
        hamleSayısı = 1
        tahtayıYaz
    }
    def boya(hane: Oda, oyuncu: Taş) =
        odanınKaresi(hane).boyamaRenginiKur(taşınRengi(oyuncu))

    private def oda2nokta(oda: Oda, solaltKöşe: İkil = doğru) =
        if (solaltKöşe) Nokta(xKöşe + oda.stn * boy, yKöşe + oda.str * boy)
        else Nokta(xKöşe + oda.stn * boy + b2, yKöşe + oda.str * boy + b2)
    private def kareyiTanımla(k: Resim) = {
        val oda = kareninOdası(k)
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
        def odaRengi = taşınRengi(tahta(oda.str)(oda.stn))
        def renk = taşınRengi(oyuncu)
        k.fareGirince { (x, y) =>
            ipucu.konumuKur(oda2nokta(oda, yanlış) - Nokta(b2, -b2))
            tahta(oda.str)(oda.stn) match {
                case Yok => if (hamleyiDene(oda).size > 0) {
                    k.boyamaRenginiKur(renk)
                    ipucu.güncelle(s"${hamleGetirisi(oda)}")
                }
                else {
                    ipucu.güncelle(s"$oda")
                }
                case _ => ipucu.güncelle(s"$oda")
            }
            ipucu.göster()
            ipucu.öneAl()
            ipucu.girdiyiAktar(k)
        }
        k.fareÇıkınca { (_, _) =>
            k.boyamaRenginiKur(odaRengi)
            ipucu.gizle()
        }
    }
    private val boşOdaRengi = Renk(10, 111, 23) // koyuYeşil
    private def taşınRengi(t: Taş) = t match {
        case Yok   => boşOdaRengi
        case Beyaz => beyaz
        case Siyah => siyah
    }

    tahtayıKur

    def hamleyiGöster(oda: Oda) = {
        hamleResminiSil
        if (hamleResmiAçık) {
            hamleResmi = götür(oda2nokta(oda, yanlış)) * kalemRengi(mavi) * kalemBoyu(3) *
                boyaRengi(renksiz) -> Resim.daire(b4)
            hamleResmi.girdiyiAktar(odanınKaresi(oda))
            hamleResmi.çiz()
        }
    }
    def hamleResminiSil = hamleResmi.sil()
    private var hamleResmi: Resim = Resim.daire(b4)
    private val hamleResmiAçık = doğru

    def seçenekleriGöster = {
        seçenekResimleri.foreach { r => r.sil() }
        if (seçeneklerAçık) {
            val sıralı = bütünYasalHamleler.map { oda => (oda, hamleGetirisi(oda)) }.sortBy { p => p._2 }.reverse
            if (sıralı.size > 0) {
                val enİriGetiri = sıralı.head._2
                seçenekResimleri = sıralı map {
                    case (oda, getirisi) =>
                        val renk = if (getirisi == enİriGetiri) sarı else turuncu
                        val göster = götür(oda2nokta(oda, yanlış)) * kalemRengi(renk) * kalemBoyu(3) *
                            boyaRengi(renksiz) -> Resim.daire(b4)
                        göster.girdiyiAktar(odanınKaresi(oda))
                        göster.çiz()
                        göster
                }
            }
        }
    }
    private var seçenekResimleri: Dizi[Resim] = Dizi()
    private var seçeneklerAçık = yanlış
    private def seçenekleriAçKapa(d: Resim) = {
        seçeneklerAçık = if (seçeneklerAçık) yanlış else doğru
        seçenekleriGöster
        val renk1 = if (seçeneklerAçık) beyaz else renksiz
        val renk2 = if (seçeneklerAçık) renksiz else beyaz
        d.fareGirince { (_, _) => d.kalemRenginiKur(renk2) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(renk1) }
        if (!seçeneklerAçık) seçenekleriKapa(d)
    }
    private def seçenekleriKapa(d: Resim) = {
        seçeneklerAçık = yanlış
        seçenekResimleri.foreach { r => r.sil() }
        d.fareGirince { (_, _) => d.kalemRenginiKur(beyaz) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(renksiz) }
        d.kalemRenginiKur(renksiz)
    }

    private def düğme(x: Kesir, y: Kesir, boya: Renk, mesaj: Yazı) = {
        val d = götür(x, y) * kalemRengi(renksiz) * boyaRengi(boya) -> Resim.dizi(
            götür(boy / 5, b2 + b4 / 3) -> Resim.yazıRenkli(mesaj, 10, beyaz),
            kalemBoyu(3) -> Resim.daire(boy * 9 / 20))
        d.fareGirince { (_, _) => d.kalemRenginiKur(beyaz) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(renksiz) }
        d.çiz()
        d
    }
    private val (dx, dy) = ((0.8 + odaSayısı) * boy + xKöşe, yKöşe + b2)
    private val d0 = { // alttan birinci sırada soldan ikinci
        val d = düğme(dx + boy, dy, pembe, "öneri")
        d.fareGirince { (_, _) =>
            d.kalemRenginiKur(if (bütünYasalHamleler.isEmpty) kırmızı else beyaz)
        }
        d.fareyeTıklayınca { (_, _) => öneri }
    }
    private val d1 = {
        val d = düğme(dx, dy + 2 * boy, sarı, "seçenekler")
        d.fareyeTıklayınca { (_, _) => seçenekleriAçKapa(d) }
        d
    }
    düğme(dx + boy, dy + 2 * boy, turuncu, "tüm ekran aç/kapa").fareyeTıklayınca { (_, _) => tümEkranTuval() }
    düğme(dx, dy, kırmızı, "özdevin").fareyeTıklayınca { (_, _) => özdevin() }
    düğme(dx, dy + boy, mavi, "yeni oyun").fareyeTıklayınca { (_, _) => yeniOyun; seçenekleriGöster }
    düğme(dx, dy + 3 * boy, açıkGri, "geri").fareyeTıklayınca { (_, _) => geriAl; seçenekleriGöster }
    düğme(dx + boy, dy + 3 * boy, renkler.blanchedAlmond, "ileri").fareyeTıklayınca { (_, _) => ileriGit; seçenekleriGöster }
    private val skorYazısı = {
        val y = {
            val tahtaTavanı = dy + (odaSayısı - 0.75) * boy
            val düğmelerinTavanı = dy + 4.75 * boy
            enİrisi(tahtaTavanı, düğmelerinTavanı)
        }
        val yazı = götür(dx - b3, y) -> Resim.yazıRenkli("", 20, sarı)
        yazı.çiz(); yazı
    }

    def skoruGüncelle = skorYazısı.güncelle(s"${hamleSayısı}\n${kaçkaç(doğru)}")

    private val ipucu = Resim.yazıRenkli("", 20, kırmızı)
    ipucu.çiz()

    tuşaBasınca { t =>
        t match {
            case tuşlar.VK_RIGHT =>
                ileriGit; seçenekleriGöster
            case tuşlar.VK_LEFT =>
                geriAl; seçenekleriGöster
            case tuşlar.VK_UP =>
                öneri; seçenekleriGöster
            case _ =>
        }
    }
}
val araYüz = new arayüz()
özdevin(0.02)
