//#include types

var i = 0
class Oyun(tane: Sayı) {
    val t = yeniTahta(tane)
    val durum = new Durum(t, Siyah)

    def oyna: Durum = döngü(durum)

    import scala.annotation.tailrec
    @tailrec
    private def döngü(durum: Durum): Durum = {
        durum.tahta.yaz("döngü")
        i += 1
        if (durum.oyunBittiMi) return durum
        if (i > tane * tane) { satıryaz("çok uzadı!"); return durum }
        val hamle = ABa.hamleYap(durum) match {
            case Biri(oda) => oda
            case _ => ABa.hamleYap(new Durum(durum.tahta, durum.karşıTaş)) match {
                case Biri(oda) => oda
                case _         => throw new Exception("Burada olmamalı")
            }
        }
        val yeni = durum.oyna(hamle)
        satıryaz(s"$i. hamle ${durum.sıra} $hamle:")
        döngü(yeni)
    }
}

//

object ABa { // alfa-beta arama
    private val aramaDerinliğiSınırı = 6 // for minimax and alfabeta only
    private val seçim = 3
    val mesaj = seçim match {
        case 1 => "atmaca"
        case 2 => "minimax" // epey yavas. d=5 => Hamle 5 ~20 saniye aldı
        case _ => "alfaBeta"
    }
    def hamleYap(durum: Durum): Belki[Oda] = {
        var çıktı: Belki[Oda] = Hiçbiri
        zamanTut(mesaj) {
            çıktı = hamleYap_(durum)
        }("sürdü")
        çıktı
    }
    def hamleYap_(durum: Durum): Belki[Oda] = seçim match {
        case 1 => atmaca(durum)
        case 2 => minimaxHamle(durum)
        case _ => alfaBetaHamle(durum)
    }

    def atmaca(durum: Durum): Belki[Oda] = {
        def rastgeleSeç[T](dizi: Dizi[T]): Belki[T] = if (dizi.isEmpty) Hiçbiri else
            Biri(dizi.drop(rastgele(dizi.size)).head)
        rastgeleSeç(durum.yasallar)
    }

    def minimaxHamle(durum: Durum): Belki[Oda] =
        if (durum.seçenekler.isEmpty) Hiçbiri
        else Biri({
            satıryaz(durum.seçenekler.mkString(s"${durum.sıra} Top: ", " ", ""))
            val dizi = for (hamle <- durum.seçenekler)
                yield hamle -> minimax(durum.oyna(hamle), aramaDerinliğiSınırı)
            satıryaz(dizi.mkString(s"${durum.sıra}: ", " ", ""))
            dizi.minBy(_._2)._1
        })

    def minimax(durum: Durum, derinlik: Sayı): Sayı = {
        //minimize(durum, derinlik) // minimax -- works
        //optimize(Minimize)(durum, derinlik)  // minimax -- stack overflow
        min2(durum, derinlik) // minimax -- works
    }
    def min2(durum: Durum, derinlik: Sayı): Sayı =
        if (durum.bitti || derinlik == 0 || durum.seçenekler.isEmpty) durum.skor
        else durum.seçenekler.map { h => max2(durum.oyna(h), derinlik - 1) }.min
    def max2(durum: Durum, derinlik: Sayı): Sayı =
        if (durum.bitti || derinlik == 0 || durum.seçenekler.isEmpty) durum.skor
        else durum.seçenekler.map { h => min2(durum.oyna(h), derinlik - 1) }.max

    trait Optimize {
        val init: Sayı
        val other: Optimize
        val name: Yazı
        def opt(s1: Sayı, s2: Sayı): Sayı
    }
    case object Minimize extends Optimize {
        val init = Int.MaxValue
        val other = Maximize
        val name = "minimizing"
        def opt(s1: Sayı, s2: Sayı) = enUfağı(s1, s2)
    }
    case object Maximize extends Optimize {
        val init = Int.MinValue
        val other = Minimize
        val name = "maximizing"
        def opt(s1: Sayı, s2: Sayı) = enİrisi(s1, s2)
    }
    // todo: stack overflow!
    def optimize(opt: Optimize)(durum: Durum, derinlik: Sayı): Sayı = {
        val tab = s"$i: $derinlik" + "d----" * derinlik
        durum.tahta.yaz(s"$tab ${durum.sıra} in $opt", tab)
        if (durum.bitti || derinlik == 0) durum.skor
        else if (durum.seçenekler.size == 0) {
            satıryaz(s"$tab ${durum.sıra} seçenek yok!")
            durum.skor
        }
        else {
            var best = opt.init // beta
            durum.seçenekler.foreach { hamle =>
                satıryaz(s"$tab $hamle")
                val yeniDurum = durum.oyna(hamle)
                val optChild = optimize(opt.other)(yeniDurum, derinlik - 1)
                best = opt.opt(best, optChild)
                satıryaz(s"$tab ${opt.name} optChildren: $optChild")
            }
            satıryaz(s"$tab min: $best")
            best
        }
    }

    // alfa-beta search, works
    def abHamle2(durum: Durum, derinlik: Sayı): Sayı =
        if (durum.bitti || derinlik == 0 || durum.seçenekler.isEmpty) durum.skor
        else azalt2(durum, derinlik, Int.MinValue, Int.MaxValue)

    def azalt2(durum: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        if (durum.bitti || derinlik == 0 || durum.seçenekler.isEmpty) durum.skor
        else {
            var yeniBeta = beta
            durum.seçenekler.foreach { hamle => // onun hamleleri
                val yeniDurum = durum.oyna(hamle)
                yeniBeta = enUfağı(beta, artır2(yeniDurum, derinlik - 1, alfa, yeniBeta))
                if (alfa >= yeniBeta) return alfa
            }
            yeniBeta
        }
    def artır2(durum: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        if (durum.bitti || derinlik == 0 || durum.seçenekler.isEmpty) durum.skor
        else {
            var yeniAlfa = alfa
            durum.seçenekler.foreach { hamle =>
                val yeniDurum = durum.oyna(hamle)
                yeniAlfa = enİrisi(yeniAlfa, azalt2(yeniDurum, derinlik - 1, yeniAlfa, beta))
                if (yeniAlfa >= beta) return beta
            }
            yeniAlfa
        }

    def alfaBetaHamle(durum: Durum): Belki[Oda] =
        if (durum.seçenekler.isEmpty) Hiçbiri
        else // todo: karşı oyuncunun skorunu azaltan birden çok hamle varsa rastgele seç
            // benim hamleler, onun skorunu ençok azaltan hamleyi seç
            Biri((for (hamle <- durum.seçenekler) yield hamle ->
                abHamle2(durum.oyna(hamle), aramaDerinliğiSınırı)
            // alfa-beta implementation below is buggy !
            //  alfaBeta(durum.oyna(hamle), aramaDerinliğiSınırı)
            ).minBy(_._2)._1)

    // karşı oyuncunun skorunu azaltmaya çalışalım
    private def alfaBeta(durum: Durum, derinlik: Sayı): Sayı =
        if (durum.bitti) durum.skor // needed due to the recursive call to this when there are no moves
        else if (durum.seçenekler.isEmpty) { // onun hamlesi yok!
            satıryaz(s"sıra değişti: ${durum.sıra} -> ${durum.karşıTaş}") // sıra bana döndü
            val yDurum = new Durum(durum.tahta, durum.karşıTaş)
            val (enİyiHamle, beta) = (for (hamle <- yDurum.seçenekler) yield hamle -> alfaBeta(yDurum.oyna(hamle), derinlik - 1)).maxBy(_._2)
            azalt(yDurum.oyna(enİyiHamle), derinlik - 2, Int.MinValue, beta)
        }
        else azalt(durum, derinlik, Int.MinValue, Int.MaxValue) // onun en iyi karşılık hamleleri arasından en zayıf olanı bulmak istiyoruz

    private def azalt(durum: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        if (durum.bitti || derinlik == 0) durum.skor
        else if (durum.seçenekler.isEmpty) {
            satıryaz(s"sıra değişti: ${durum.sıra} -> ${durum.karşıTaş}")
            val yDurum = new Durum(durum.tahta, durum.karşıTaş)
            val (enİyiHamle, yeniAlfa) = (for (hamle <- yDurum.seçenekler) yield hamle -> alfaBeta(yDurum.oyna(hamle), derinlik - 1)).maxBy(_._2)
            //artır(yDurum.oyna(enİyiHamle), derinlik - 2, yeniAlfa, beta)
            yeniAlfa
        }
        else {
            var yeniBeta = beta
            durum.seçenekler.foreach { hamle => // onun hamleleri
                val yeniDurum = durum.oyna(hamle)
                yeniBeta = enUfağı(beta, artır(yeniDurum, derinlik - 1, alfa, yeniBeta)) // benim hamlelerim
                if (alfa >= yeniBeta) return alfa
            }
            yeniBeta
        }
    private def artır(durum: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        if (durum.bitti || derinlik == 0) durum.skor
        else if (durum.seçenekler.isEmpty) {
            satıryaz(s"sıra değişti: ${durum.sıra} -> ${durum.karşıTaş}")
            val yDurum = new Durum(durum.tahta, durum.karşıTaş)
            val (enİyiHamle, yeniBeta) = (for (hamle <- yDurum.seçenekler) yield hamle -> alfaBeta(yDurum.oyna(hamle), derinlik - 1)).minBy(_._2)
            //azalt(yDurum.oyna(enİyiHamle), derinlik - 1, alfa, yeniBeta)
            yeniBeta
        }
        else {
            var yeniAlfa = alfa
            durum.seçenekler.foreach { hamle =>
                val yeniDurum = durum.oyna(hamle)
                yeniAlfa = enİrisi(yeniAlfa, azalt(yeniDurum, derinlik - 1, yeniAlfa, beta))
                if (yeniAlfa >= beta) return beta
            }
            yeniAlfa
        }
}

class Durum(val tahta: Tahta, val sıra: Taş) {
    val karşıTaş = if (sıra == Beyaz) Siyah else Beyaz
    def skor = tahta.durum(sıra) - tahta.durum(karşıTaş)
    def bitti = oyunBittiMi
    def oyunBittiMi = {
        if (yasallar.size > 0) yanlış else {
            val yeni = new Durum(tahta, karşıTaş)
            yeni.yasallar.size == 0
        }
    }
    def seçenekler = yasallar
    def yasallar = tahta.yasallar(sıra)
    def oyna(oda: Oda) = {
        val yTahta = tahta.oyna(sıra, oda)
        new Durum(yTahta, karşıTaş)
    }
}

class Tahta(val tane: Sayı, val tahta: Sayılar) {
    def durum(t: Taş) = {
        say(t) // + 4 * isay(t)(köşeMi) + 2 * isay(t)(köşeyeİkiUzakMı)
        // + isay(t)(içKöşeMi)
        // - 2 * isay(t)(tuzakKöşeMi) - isay(t)(tuzakKenarMı)
        //
    }
    def yaz(msj: Yazı = "", tab: Yazı = "") = {
        for (y <- satırAralığıSondan) {
            val satır = for (x <- satırAralığı) yield s2t(tahta(y * tane + x))
            satıryaz(satır.mkString(s"$tab", " ", ""))
        }
        if (msj.size > 0) satıryaz(msj)
        satıryaz(s"$tab Beyazlar: ${say(Beyaz)} durum: ${durum(Beyaz)}")
        satıryaz(s"$tab Siyahlar: ${say(Siyah)} durum: ${durum(Siyah)}")
    }
    def koy(oda: Oda, taş: Taş) = {
        new Tahta(tane, tahta.updated(oda.y * tane + oda.x, t2s(taş)))
    }
    def koy(odalar: Dizi[Oda], taş: Taş) = {
        var yeni = tahta
        for (o <- odalar) { yeni = yeni.updated(o.y * tane + o.x, t2s(taş)) }
        new Tahta(tane, yeni)
    }
    def taş(o: Oda): Taş = s2t(tahta(o.y * tane + o.x))
    def oyunVarMı(oyuncu: Taş) = yasallar(oyuncu).size > 0
    def yasallar(oyuncu: Taş) = {
        (for (y <- satırAralığı; x <- satırAralığı if taş(Oda(y, x)) == Yok)
            yield Oda(y, x)) filter { çevirilecekKomşuDiziler(oyuncu, _).size > 0 }
    }
    def oyna(oyuncu: Taş, oda: Oda) = {
        val odalar = EsnekDizim(oda)
        val karşı = if (oyuncu == Beyaz) Siyah else Beyaz
        çevirilecekKomşuDiziler(oyuncu, oda).foreach { komşu =>
            odalar += komşu.oda
            gerisi(komşu).takeWhile(taş(_) == karşı).foreach { o => odalar += o }
        }
        koy(odalar.dizi, oyuncu)
    }

    private def çevirilecekKomşuDiziler(oyuncu: Taş, oda: Oda): Dizi[Komşu] =
        komşularıBul(oda) filter { komşu =>
            val karşı = if (oyuncu == Beyaz) Siyah else Beyaz
            taş(komşu.oda) == karşı && sonuDaYasalMı(komşu, oyuncu)._1
        }

    private def komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
        Komşu(D, Oda(o.y, o.x + 1)), Komşu(B, Oda(o.y, o.x - 1)),
        Komşu(K, Oda(o.y + 1, o.x)), Komşu(G, Oda(o.y - 1, o.x)),
        Komşu(KD, Oda(o.y + 1, o.x + 1)), Komşu(KB, Oda(o.y + 1, o.x - 1)),
        Komşu(GD, Oda(o.y - 1, o.x + 1)), Komşu(GB, Oda(o.y - 1, o.x - 1))) filter {
            k => odaMı(k.oda)
        }

    private def sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
        val diziTaşlar = gerisi(k)
        val sıraTaşlar = diziTaşlar.dropWhile { o =>
            taş(o) != oyuncu && taş(o) != Yok
        }
        if (sıraTaşlar.isEmpty) (yanlış, 0) else {
            val oda = sıraTaşlar.head
            (taş(oda) == oyuncu, 1 + diziTaşlar.size - sıraTaşlar.size)
        }
    }
    private def gerisi(k: Komşu): Dizi[Oda] = {
        val sıra = EsnekDizim.boş[Oda]
        val (x, y) = (k.oda.x, k.oda.y)
        k.yön match {
            case D => for (i <- x + 1 to sonOda) /* */ sıra += Oda(y, i)
            case B => for (i <- x - 1 to 0 by -1) /**/ sıra += Oda(y, i)
            case K => for (i <- y + 1 to sonOda) /* */ sıra += Oda(i, x)
            case G => for (i <- y - 1 to 0 by -1) /**/ sıra += Oda(i, x)
            case KD => // hem y hem x artacak
                if (x >= y) for (i <- x + 1 to sonOda) /*         */ sıra += Oda(y + i - x, i)
                else for (i <- y + 1 to sonOda) /*                */ sıra += Oda(i, x + i - y)
            case GB => // hem y hem x azalacak
                if (x >= y) for (i <- y - 1 to 0 by -1) /*        */ sıra += Oda(i, x - y + i)
                else for (i <- x - 1 to 0 by -1) /*               */ sıra += Oda(y - x + i, i)
            case KB => // y artacak x azalacak
                if (x + y >= sonOda) for (i <- y + 1 to sonOda) /**/ sıra += Oda(i, x + y - i)
                else for (i <- x - 1 to 0 by -1) /*               */ sıra += Oda(y + x - i, i)
            case GD => // y azalacak x artacak
                if (x + y >= sonOda) for (i <- x + 1 to sonOda) /**/ sıra += Oda(y + x - i, i)
                else for (i <- y - 1 to 0 by -1) /*               */ sıra += Oda(i, x + y - i)
        }
        sıra.dizi
    }

    val sonOda = tane - 1; val son = sonOda
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
    def köşeyeİkiUzakMı: Oda => İkil = {
        case Oda(y, x) =>
            ((y == 0 || y == son) && (x == 2 || x == son - 2)) ||
                ((y == 2 || y == son - 2) &&
                    (x == 0 || x == 2 || x == son - 2 || x == son))
    }
    def içKöşeMi: Oda => İkil = {
        case Oda(y, x) => (x == 2 && (y == 2 || y == sonOda - 2)) ||
            (x == sonOda - 2 && (y == 2 || y == sonOda - 2))
    }
    def odaMı: Oda => İkil = {
        case Oda(y, x) => 0 <= y && y < tane && 0 <= x && x < tane
    }
    def isay(t: Taş)(iş: Oda => İkil) = (for (x <- satırAralığı; y <- satırAralığı; if taş(Oda(y, x)) == t && iş(Oda(y, x))) yield 1).size
    def say(t: Taş) = isay(t) { o => doğru }

    private def t2s(t: Taş) = t match {
        case Beyaz => 1
        case Siyah => 2
        case _     => 0
    }
    private def s2t(s: Sayı) = s match {
        case 1 => Beyaz
        case 2 => Siyah
        case _ => Yok
    }
}

def yeniTahta(tane: Sayı, çeşni: Sayı = 0): Tahta = {
    var t = new Tahta(tane, Vector.fill(tane * tane)(0))

    def diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = t = t.koy(dizi.map(p => Oda(p._1, p._2)), taş)
    def dörtTane: Oda => Birim = {
        case Oda(y, x) =>
            diziden(Dizi((y, x), (y + 1, x + 1)))(Beyaz)
            diziden(Dizi((y + 1, x), (y, x + 1)))(Siyah)
    }
    val orta: Sayı = tane / 2
    val son = tane - 1
    çeşni match {
        case 2 => // boş tahtayla oyun başlayamıyor
        case 1 =>
            gerekli((tane > 6), "Bu çeşni için 7x7 ya da daha iri bir tahta gerekli")
            dörtTane(Oda(1, 1))
            dörtTane(Oda(son - 2, son - 2))
            dörtTane(Oda(1, son - 2))
            dörtTane(Oda(son - 2, 1))
        case _ =>
            val çiftse = tane % 2 == 0
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
    t
}

def dene1 = {
    val tane = 4
    var t = new Tahta(tane, Vector.fill(tane * tane)(0))
    satıryaz("t"); t.yaz()
    val foo = t.koy(Oda(1, 1), Beyaz)
    t = t.koy(Dizi(Oda(2, 2), Oda(3, 3)), Beyaz)
    t = t.koy(Dizi(Oda(2, 3), Oda(3, 2)), Siyah)
    val t2 = t.oyna(Siyah, Oda(1, 2))
    satıryaz("t2"); t2.yaz()
    val t3 = t2.oyna(Beyaz, Oda(1, 3))
    satıryaz("t3"); t3.yaz()
    satıryaz("t"); t.yaz()
    foo.yaz()
}

def dene2 = {
    çıktıyıSil
    val o = new Oyun(4)
    o.oyna
}
//dene1
// Bu yazılımcığı otello.kojo kullanıyor. Onun için denemeleri artık çalıştırmıyoruz
//dene2
satıryaz("arama motoru hazır")
