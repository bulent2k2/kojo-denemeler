val odaSayısı = 7
val kimBaşlar = Siyah // Beyaz ya da Siyah başlayabilir. Seç :-)
val çeşni = 1

trait Taş
case object Beyaz extends Taş { override def toString() = "B" }
case object Siyah extends Taş { override def toString() = "S" }
case object Yok extends Taş { override def toString() = "." }
// satır ve sütün sırası yazılımda ters!
case class Oda(str: Sayı, stn: Sayı) { override def toString() = s"${stn + 1}x${str + 1}" }

class Tahta(
    val odaSayısı: Sayı, // satır ve sütunların oda sayısı
    val kimBaşlar: Taş) {
    gerekli(3 < odaSayısı, "En küçük tahtamız 4x4lük. odaSayısı değerini artırın") // başlangıç taşları sığmıyor
    gerekli(20 > odaSayısı, "En büyük tahtamız 19x19luk. odaSayısı değerini azaltın") // çok yavaşlıyor
    gerekli(kimBaşlar != Yok, "Beyaz ya da Siyah başlamalı")
    def yaz = for (y <- satırAralığıSondan) satıryaz(tahta(y).mkString(" "))
    def say(t: Taş) = (for (x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == t) yield 1).size
    val hamleSayısı = new HamleSayısı
    var oyunBitti = yanlış
    var sonHamle: Belki[Oda] = _ // sadece son hamleyi tuvalde göstermek için gerekli
    def yasallar() = (for {
        x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == Yok
    } yield Oda(y, x)) filter { hamleyiDene(_).size > 0 }
    def taş(oda: Oda): Taş = tahta(oda.str)(oda.stn)
    def taş(y: Sayı, x: Sayı): Taş = tahta(y)(x)
    def taşıKur(y: Sayı)(x: Sayı)(taş: Taş): Birim = tahta(y)(x) = taş
    def taşıKur(oda: Oda)(taş: Taş): Birim = tahta(oda.str)(oda.stn) = taş
    private val tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Yok)
    val oyuncu = new Oyuncu(kimBaşlar)
    def başaAl(başlık: Yazı = "") = {
        for (x <- satırAralığı; y <- satırAralığı) taşıKur(Oda(y, x))(Yok)
        başlangıçTaşlarınıKur
        oyuncu.başaAl() // todo: new tahta?
        hamleSayısı.başaAl()
        oyunBitti = yanlış
        sonHamle = Hiçbiri
        if (başlık.size > 0) satıryaz(başlık)
        yaz
    }
    def başlangıçTaşlarınıKur = {
        def diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = for { (y, x) <- dizi } taşıKur(Oda(y, x))(taş)
        def dörtTane: Oda => Birim = {
            case Oda(y, x) =>
                diziden(Dizi((y, x), (y + 1, x + 1)))(Beyaz)
                diziden(Dizi((y + 1, x), (y, x + 1)))(Siyah)
        }
        val orta: Sayı = odaSayısı / 2
        çeşni match {
            case 2 => // boş tahtayla oyun başlayamıyor
            case 1 =>
                gerekli((odaSayısı > 6), "Bu çeşni için 7x7 ya da daha iri bir tahta gerekli")
                dörtTane(Oda(1, 1))
                dörtTane(Oda(sonOda - 2, sonOda - 2))
                dörtTane(Oda(1, sonOda - 2))
                dörtTane(Oda(sonOda - 2, 1))
            case _ =>
                val çiftse = odaSayısı % 2 == 0
                if (çiftse) dörtTane(Oda(orta - 1, orta - 1)) else {
                    val (a, b) = (orta - 1, orta + 1)
                    diziden(Dizi(a -> a, b -> b))(Beyaz)
                    diziden(Dizi((a, b), (b, a)))(Siyah)
                    if (yanlış) { // (a, b) odaları boş kalıyor her a ve b çift sayısı için
                        diziden(Dizi(a + 1 -> a, (b - 1, b)))(Beyaz)
                        diziden(Dizi((a, b - 1), (b, a + 1)))(Siyah)
                    }
                    else {
                        diziden(Dizi((a + 1, a), (a + 1, b), (b + 1, b - 1), (a - 1, b - 1)))(Beyaz)
                        diziden(Dizi((a, a + 1), (b, a + 1), (a + 1, a - 1), (a + 1, b + 1)))(Siyah)
                    }
                }
        }
    }

    val sonOda = odaSayısı - 1
    val satırAralığı = 0 to sonOda
    val satırAralığıSondan = sonOda to 0 by -1

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
    def odaMı: Oda => İkil = {
        case Oda(y, x) => 0 <= y && y < odaSayısı && 0 <= x && x < odaSayısı
    }

    başaAl()
}
class HamleSayısı { // bir sonraki hamle kaçıncı hamle olacak? 1, 2, 3, ...
    def apply() = say
    def başaAl() = say = 1
    def artır() = say += 1
    def azalt() = say -= 1
    private var say: Sayı = _
    başaAl()
}
class Oyuncu(val kimBaşlar: Taş) {
    def apply() = oyuncu
    def başaAl() = oyuncu = kimBaşlar
    def değiştir() = oyuncu = if (oyuncu == Beyaz) Siyah else Beyaz
    def kur(o: Taş) = oyuncu = o
    private var oyuncu = kimBaşlar
    başaAl()
}

// assert(yanlış, "geldik")

def adı(o: Taş) = if (o == Siyah) "siyah" else "beyaz"
def hamleYoksa = tahta.yasallar().size == 0

def kaçkaç(kısa: İkil = yanlış) = if (kısa) s"B: ${tahta.say(Beyaz)}\nS: ${tahta.say(Siyah)}"
else s"Beyazlar: ${tahta.say(Beyaz)}\nSiyahlar: ${tahta.say(Siyah)}"
def bittiKaçKaç = if (!tahta.oyunBitti) {
    tahta.oyunBitti = doğru
    satıryaz(s"Oyun bitti.\n${kaçkaç()}")
}

def sırayıÖbürOyuncuyaGeçir = {
    tahta.oyuncu.değiştir()
    araYüz.seçenekleriGöster
}

trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön

case class Komşu(yön: Yön, oda: Oda)

def bütünYasalHamleler = tahta.yasallar()

def hamleyiDene(oda: Oda): Dizi[Komşu] = komşularıBul(oda) filter { komşu =>
    val komşuTaş = tahta.taş(komşu.oda)
    komşuTaş != Yok && komşuTaş != tahta.oyuncu() && sonuDaYasalMı(komşu, tahta.oyuncu())._1
}

def hamleGetirisi(oda: Oda): Sayı = {
    val getiriliYönler = komşularıBul(oda) map { komşu =>
        val komşuTaş = tahta.taş(komşu.oda)
        val sonunaKadar = sonuDaYasalMı(komşu, tahta.oyuncu())
        if (komşuTaş != Yok && komşuTaş != tahta.oyuncu() && sonunaKadar._1) sonunaKadar._2 else 0
    }
    getiriliYönler.sum
}

def komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
    Komşu(D, Oda(o.str, o.stn + 1)), Komşu(B, Oda(o.str, o.stn - 1)),
    Komşu(K, Oda(o.str + 1, o.stn)), Komşu(G, Oda(o.str - 1, o.stn)),
    Komşu(KD, Oda(o.str + 1, o.stn + 1)), Komşu(KB, Oda(o.str + 1, o.stn - 1)),
    Komşu(GD, Oda(o.str - 1, o.stn + 1)), Komşu(GB, Oda(o.str - 1, o.stn - 1))) filter {
        k => tahta.odaMı(k.oda)
    }

def sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
    val diziTaşlar = gerisi(k)
    val sıraTaşlar = diziTaşlar.dropWhile { o =>
        tahta.taş(o) != oyuncu && tahta.taş(o) != Yok
    }
    if (sıraTaşlar.isEmpty) (yanlış, 0) else {
        val oda = sıraTaşlar.head
        (tahta.taş(oda) == oyuncu, 1 + diziTaşlar.size - sıraTaşlar.size)
    }
}
def gerisi(k: Komşu): Dizi[Oda] = {
    val sonOda = tahta.sonOda
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
        val komşuTaş = tahta.taş(komşu.oda)
        gerisi(komşu).takeWhile { oda =>
            tahta.taş(oda) != Yok && tahta.taş(oda) != tahta.oyuncu()
        }.foreach(taşıAltÜstYap(_))
        taşıAltÜstYap(komşu.oda)
    }
    saklaTahtayı(doğru, tahta.sonHamle)
    tahta.sonHamle = Biri(hane)
    bütünTuşlarıÇevir
    tahta.taşıKur(hane)(tahta.oyuncu())
    araYüz.boya(hane, tahta.oyuncu())
    satıryaz(s"Hamle ${tahta.hamleSayısı()} ${tahta.oyuncu()} $hane:")
    tahta.yaz
    sırayıÖbürOyuncuyaGeçir
    tahta.hamleSayısı.artır()
    yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    araYüz.skoruGüncelle
    araYüz.hamleyiGöster(hane)
    if (duraklamaSüresi > 0) durakla(duraklamaSüresi)
}
def taşıAltÜstYap(oda: Oda): Birim = {
    tahta.taşıKur(oda)(tahta.oyuncu())
    araYüz.boya(oda, tahta.oyuncu())
}

def yeniOyun = if (tahta.hamleSayısı() != 1) {
    tahta.başaAl("Yeni oyun:")
    for (x <- tahta.satırAralığı; y <- tahta.satırAralığı) araYüz.boya(Oda(y, x), tahta.taş(y, x))
    eskiTahtalar.sil()
    oyuncular.sil()
    hamleler.sil()
    araYüz.skorBaşlangıç
    araYüz.hamleResminiSil
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
                    satıryaz(s"Yasal hamle yok. Sıra yine ${adı(tahta.oyuncu())}ın")
                    dallanma += bütünYasalHamleler.size
                    hamleyiYap(hamleyiDene(oda), oda, duraklamaSüresi)
                case _ =>
                    bittiKaçKaç
                    if (dallanma.sayı > 0) {
                        val d = dallanma.dizi
                        satıryaz(s"Oyun ${d.size} kere dallandı. Dal sayıları: ${d.mkString(",")}")
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
def köşeYaklaşımı(yasallar: Dizi[Oda]): Belki[Oda] = rastgeleSeç(yasallar.filter(tahta.köşeMi(_))) match {
    case Biri(oda) => Biri(oda) // köşe bulduk!
    case _ => rastgeleSeç(yasallar.filter(tahta.içKöşeMi(_))) match {
        case Biri(oda) => Biri(oda)
        case _ => { // tuzakKenarlar tuzakKöşeleri içeriyor
            val tuzakKenarOlmayanlar = yasallar.filterNot(tahta.tuzakKenarMı(_))
            enİriGetirililerArasındanRastgele(
                if (!tuzakKenarOlmayanlar.isEmpty) tuzakKenarOlmayanlar
                else { // tuzak kenarlardan getirisi en iri olanlardan seçiyoruz
                    val tuzakKöşeOlmayanlar = yasallar.filterNot(tahta.tuzakKöşeMi(_))
                    if (tuzakKöşeOlmayanlar.isEmpty) yasallar else tuzakKöşeOlmayanlar
                }
            )
        }
    }
}

// ileri geri gidiş için gerekli bellek
val eskiTahtalar = EsnekDizim.boş[Dizim[Array[Taş]]] // todo
val oyuncular = EsnekDizim.boş[Taş]
val hamleler = EsnekDizim.boş[Belki[Oda]]
def saklaTahtayı(yeniHamleMi: İkil, hane: Belki[Oda]) = {
    if (yeniHamleMi) while (tahta.hamleSayısı() <= eskiTahtalar.sayı) {
        eskiTahtalar.çıkar(eskiTahtalar.sayı - 1)
        oyuncular.çıkar(oyuncular.sayı - 1)
        hamleler.çıkar(hamleler.sayı - 1)
    }
    val yeni = Dizim.boş[Taş](odaSayısı, odaSayısı)
    for (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
        yeni(y)(x) = tahta.taş(y, x)
    eskiTahtalar += yeni
    oyuncular += tahta.oyuncu()
    hamleler += hane
}
def verilenHamleTahtasınaGeç(hamle: Sayı) = {
    val eski = eskiTahtalar(hamle)
    for (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
        tahta.taşıKur(y)(x)(eski(y)(x))
    for (y <- tahta.satırAralığı; x <- tahta.satırAralığı)
        araYüz.boya(Oda(y, x), tahta.taş(y, x))
    tahta.oyuncu.kur(oyuncular(hamle))
    tahta.sonHamle = hamleler(hamle)
    tahta.sonHamle match {
        case Biri(hane) => araYüz.hamleyiGöster(hane)
        case _          => araYüz.hamleResminiSil
    }
}
var yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru // yeni bir hamleden önce geri/ileri çalışmaz ki
def geriAl = if (tahta.hamleSayısı() > 1) {
    if (yeniHamleEnYeniGeriAlKomutundanDahaGüncel) {
        yeniHamleEnYeniGeriAlKomutundanDahaGüncel = yanlış
        saklaTahtayı(yanlış, tahta.sonHamle)
    }
    tahta.oyunBitti = yanlış
    tahta.hamleSayısı.azalt()
    verilenHamleTahtasınaGeç(tahta.hamleSayısı() - 1)
    araYüz.skoruGüncelle
}
def ileriGit = if (tahta.hamleSayısı() < eskiTahtalar.sayı) {
    verilenHamleTahtasınaGeç(tahta.hamleSayısı())
    tahta.hamleSayısı.artır()
    araYüz.skoruGüncelle
}

class arayüz() { // tahtayı ve taşları çizelim ve canlandıralım
    /* arayüz sınıfının arayüzünde şunlar var sadece:
       boya(hane, oyuncu), hamleyiGöster(hane), hamleResminiSil
       seçenekleriGöster, skoruGüncelle */
    private def tahtayıKur = {
        silVeSakla; tümEkranTuval; artalanıKur(koyuGri)
        val içKöşeler = EsnekDizim.boş[Resim]
        val içKöşeKalemRengi = Renk(255, 215, 85, 101) // soluk sarımsı bir renk
        for (x <- tahta.satırAralığı; y <- tahta.satırAralığı) {
            val oda = Oda(y, x)
            val kRenk = if (tahta.içKöşeMi(oda)) içKöşeKalemRengi else mor
            val r = kalemRengi(kRenk) * boyaRengi(taşınRengi(tahta.taş(y, x))) *
                götür(odanınNoktası(oda)) -> Resim.dikdörtgen(boy, boy)
            r.çiz()
            kareninOdası += (r -> oda)
            odanınKaresi += (oda -> r)
            if (kRenk == içKöşeKalemRengi) içKöşeler += r
            kareyiTanımla(r)
        }
        içKöşeler.dizi.map(_.öneAl())
    }
    private val boy = 60 // karelerin boyu inç başına nokta sayısı. 64 => 1cm'de yaklaşık 25 nokta var (/ 64 2.54)
    private val (köşeX, köşeY) = (-odaSayısı / 2 * boy, -odaSayısı / 2 * boy) // tahtayı ortalamak için sol alt köşesini belirle
    private val (b2, b3, b4) = (boy / 2, boy / 3, boy / 4)
    private val kareninOdası = Eşlem.boş[Resim, Oda]
    private val odanınKaresi = Eşlem.boş[Oda, Resim]

    def boya(hane: Oda, taş: Taş) = odanınKaresi(hane).boyamaRenginiKur(taşınRengi(taş))

    private def odanınNoktası(oda: Oda, solAltKöşe: İkil = doğru) =
        if (solAltKöşe) Nokta(köşeX + oda.stn * boy, köşeY + oda.str * boy)
        else Nokta(köşeX + oda.stn * boy + b2, köşeY + oda.str * boy + b2)
    private def kareyiTanımla(k: Resim) = {
        val oda = kareninOdası(k)
        k.fareyeTıklayınca { (_, _) =>
            var oynadıMı = yanlış
            tahta.taş(oda) match {
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
                if (hamleYoksa) bittiKaçKaç else satıryaz(s"Yasal hamle yok. Sıra yine ${adı(tahta.oyuncu())}ın")
            }
        }
        def odaRengi = taşınRengi(tahta.taş(oda))
        def renk = taşınRengi(tahta.oyuncu())
        k.fareGirince { (x, y) =>
            ipucu.konumuKur(odanınNoktası(oda, yanlış) - Nokta(b2, -b2))
            tahta.taş(oda) match {
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
            hamleResmi = götür(odanınNoktası(oda, yanlış)) * kalemRengi(mavi) * kalemBoyu(3) *
                boyaRengi(renksiz) -> Resim.daire(b4)
            hamleResmi.girdiyiAktar(odanınKaresi(oda))
            hamleResmi.çiz()
        }
    }
    def hamleResminiSil = hamleResmi.sil()
    private var hamleResmi: Resim = Resim.daire(b4)
    private var hamleResmiAçık = yanlış
    def hamleyiAçKapa(d: Resim) = {
        hamleResmiAçık = !hamleResmiAçık
        if (hamleResmiAçık) düğmeSeçili(d) else düğmeTepkisi(d)
        tahta.sonHamle match {
            case Biri(hane) => hamleyiGöster(hane)
            case _          =>
        }
    }

    def seçenekleriGöster = {
        seçenekResimleri.foreach { r => r.sil() }
        if (seçeneklerAçık) {
            val sıralı = bütünYasalHamleler.map { oda => (oda, hamleGetirisi(oda)) }.sortBy { p => p._2 }.reverse
            if (sıralı.size > 0) {
                val enİriGetiri = sıralı.head._2
                seçenekResimleri = sıralı map {
                    case (oda, getirisi) =>
                        val renk = if (getirisi == enİriGetiri) sarı else turuncu
                        val göster = götür(odanınNoktası(oda, yanlış)) * kalemRengi(renk) * kalemBoyu(3) *
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
        seçeneklerAçık = !seçeneklerAçık
        seçenekleriGöster
        if (seçeneklerAçık) düğmeSeçili(d) else düğmeTepkisi(d)
        if (!seçeneklerAçık) seçenekleriKapa(d)
    }
    private def seçenekleriKapa(d: Resim) = {
        seçeneklerAçık = yanlış
        seçenekResimleri.foreach { r => r.sil() }
        düğmeTepkisi(d)
        d.kalemRenginiKur(renksiz)
    }

    private def düğme(x: Kesir, y: Kesir, boya: Renk, mesaj: Yazı) = {
        val d = götür(x, y) * kalemRengi(renksiz) * boyaRengi(boya) -> Resim.dizi(
            götür(boy / 5, b2 + b4 / 3) -> Resim.yazıRenkli(mesaj, 10, beyaz),
            kalemBoyu(3) -> Resim.daire(boy * 9 / 20))
        düğmeTepkisi(d)
        d.çiz()
        d
    }
    private def düğmeTepkisi(d: Resim, rFareGirince: Renk = beyaz, rFareÇıkınca: Renk = renksiz) = {
        d.fareGirince { (_, _) => d.kalemRenginiKur(rFareGirince) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(rFareÇıkınca) }
    }
    private def düğmeSeçili(d: Resim) = düğmeTepkisi(d, renksiz, beyaz)

    private val (dx, dy) = ((0.8 + odaSayısı) * boy + köşeX, köşeY + b2)
    private val d0 = { // alttan birinci sırada soldan ikinci
        val d = düğme(dx, dy + 2 * boy, pembe, "öneri")
        d.fareGirince { (_, _) =>
            d.kalemRenginiKur(if (bütünYasalHamleler.isEmpty) kırmızı else beyaz)
        }
        d.fareyeTıklayınca { (_, _) => öneri }
    }
    private val d1 = {
        val d = düğme(dx, dy + boy, sarı, "seçenekler")
        d.fareyeTıklayınca { (_, _) => seçenekleriAçKapa(d) }
        d
    }
    private val d2 = {
        val d = düğme(dx + boy, dy + boy, mavi, "son hamle aç/kapa")
        d.kalemRenginiKur(renksiz) // başlangıçta son hamleyi görmeyelim
        d.fareyeTıklayınca { (_, _) => hamleyiAçKapa(d) }
        d
    }
    düğme(dx + boy, dy + 2 * boy, turuncu, "tüm ekran aç/kapa").fareyeTıklayınca { (_, _) => tümEkranTuval() }
    düğme(dx, dy, kırmızı, "özdevin").fareyeTıklayınca { (_, _) => özdevin() }
    düğme(dx + boy, dy, yeşil, "yeni oyun").fareyeTıklayınca { (_, _) => yeniOyun; seçenekleriGöster }
    düğme(dx, dy + 3 * boy, açıkGri, "geri").fareyeTıklayınca { (_, _) => geriAl; seçenekleriGöster }
    düğme(dx + boy, dy + 3 * boy, renkler.blanchedAlmond, "ileri").fareyeTıklayınca { (_, _) => ileriGit; seçenekleriGöster }
    private val skorYazısı = {
        val y = {
            val tahtaTavanı = dy + (odaSayısı - 0.75) * boy
            val düğmelerinTavanı = dy + 4.75 * boy
            enİrisi(tahtaTavanı, düğmelerinTavanı)
        }
        val yazı = götür(dx - b3, y) -> Resim.yazıRenkli(s"", 20, sarı)
        yazı.çiz(); yazı
    }
    def skorBaşlangıç = skorYazısı.güncelle(s"${adı(tahta.oyuncu()).capitalize} başlar")
    def skoruGüncelle = skorYazısı.güncelle(s"${tahta.hamleSayısı()}\n${kaçkaç(doğru)}")
    skorBaşlangıç

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
çıktıyıSil
val tahta = new Tahta(odaSayısı, kimBaşlar)
val araYüz = new arayüz()
//özdevin(0.02) // bilgisayar çabucak bir oyunla başlayabilir istersen

