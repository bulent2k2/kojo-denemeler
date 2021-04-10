silVeSakla(); çıktıyıSil()
// Kare grid çizgesi kuracağız. düzlemseldir.
val KNS = 2 // Karenin bir kenarında kaç tane nokta olsun? kns arttıkça oyun zorlaşır.
// başlangıçtaki nokta sayısı = kns*kns.
var yarıçap = 10 // bu da noktanın yarıçapı
var çizgiler = Küme[Çizgi]() // boş küme olarak başlarız
var noktalar = Küme[Nokta]()
var noktalar2 = Yöney[Nokta]() // dışsal nokta eklemek için gerekli

case class Çizgi(n1: Nokta, n2: Nokta) { // iki noktayı bağla
    gerekli(n1 != n2, s"YANLIŞ! döngü ${n1.yaz}")
    gerekli(!n1.komşuMu(n2), s"YANLIŞ! ${n1.yaz} ve ${n2.yaz} zaten bağlı")
    //satıryaz(s"${n1.yaz} ve ${n2.yaz} bağlanıyor")
    var resim = doğruÇiz(n1.x, n1.y, n2.x, n2.y)
    n1.komşu(n2)
    n2.komşu(n1)
}

case class Nokta(var x: Kesir, var y: Kesir) {
    val bu = this
    var resim: Resim = başla(); seçimiKur(yanlış)
    private def başla() = {
        val res = götür(x, y) -> Resim.daire(yarıçap)
        çiz(res)
        fareyiTanımla(res)
        res
    }
    var komşular = Set[Nokta]()
    def komşuMu(n: Nokta) = komşular(n)
    def komşu(n: Nokta) =
        if (n != bu) komşular += n
        else satıryaz("YANLIŞ! " + bu.yaz)
    def kim() = komşular
    def yineÇiz() {
        resim.sil
        resim = başla()
        seçimiKur(seçiliNoktalar(this))
    }
    def seçimiKur(s: İkil) = {
        resim.kalemKalınlığınıKur(if (s) 6 else 2)
        resim.kalemRenginiKur(kırmızı)
        resim.boyamaRenginiKur(if (s) mavi else (renksiz))
    }
    def yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        resim.konumuKur(yeniX, yeniY)
    }
    override def toString = s"N(${yuvarla(x, 2)},${yuvarla(y, 2)})"
    def yaz = s"$ne d=${kim.size}"
    def ne = s"N[${yuvarla(x, 0)} ${yuvarla(y, 0)}]" // todo default value

    def fareyiTanımla(r: Resim) = {
        // bbx todo double click?
        r.fareyeTıklayınca { (mx, my) =>
            if (seçiliNoktalar(bu)) seçme(bu) else seç(bu)
            satıryaz(s"$yaz. $seçiliKümeyiYaz")
        }
        // çekince bu çalışacak. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
        r.fareyiSürükleyince { (mx, my) => { yeniKonum(mx, my); çizelim(çizgiler) } }
    }
    def merkezeGit() = { // komşuların ağırlık merkezine gidelim
        val s = komşular.size
        if (s == 0) satıryaz("Komşum yok!") else {
            val nx = komşular.foldLeft(0.0)((d, n) => d + n.x) / s.toDouble
            val ny = komşular.foldLeft(0.0)((d, n) => d + n.y) / s.toDouble
            yeniKonum(nx, ny); çizelim(çizgiler)
        }
    }
    def merkezeGetir() = { // komşumuz olan her noktayı yörüngeye sokalım.
        val k = komşular
        val (s, g) = (komşular.size, 2 * yarıçap)
        val tg = 2 * g
        if (s == 0) satıryaz("Komşum yok!") else {
            val ikik = 24 // bundan çok komşu varsa, iki kere çevirsek de yetmez
            val evre = Dizin((g, g), (g, 0), (g, -g), (0, -g), (-g, -g), (-g, 0), (-g, g), (0, g))
            val e2 = if (s < 9) evre else { //                                          21012
                if (s > ikik) satıryaz(s"$ikik'ten çok komşu var! " + //                g...g
                    s"${s - ikik} tanesini elinle yap, ne olur!") //                    0.x.0
                evre ::: Dizin((tg, tg), (tg, g), (tg, 0), (tg, -g), (tg, -tg), //       -...-
                    (-tg, tg), (-tg, g), (-tg, 0), (-tg, -g), (-tg, -tg), //            21012
                    (-g, tg), (0, tg), (g, tg), (-g, -tg), (0, -tg), (g, -tg))
            }
            k.take(ikik).zip(e2).map { case (k, (nx, ny)) => k.yeniKonum(x + nx, y + ny) }
            if (s > ikik)
                k.drop(ikik).take(ikik).zip(e2).map { case (k, (nx, ny)) => k.yeniKonum(x + nx, y + ny) }
            if (s > 2 * ikik)
                k.drop(2 * ikik).take(ikik).zip(e2).map { case (k, (nx, ny)) => k.yeniKonum(x + nx, y + ny) }
            çizelim(çizgiler)
        }
    }
}

var noktaSilindiMi = yanlış
def noktalarıSil(silinecekNoktalar: Küme[Nokta]) = {
    if (silinecekNoktalar.isEmpty) {
        satıryaz("Önce en az bir nokta seçmelisin")
    }
    else {
        noktaSilindiMi = doğru
        val kalanNoktalar = noktalar.filterNot { silinecekNoktalar(_) }
        noktalar = kalanNoktalar
        noktalar2 = noktalar.toVector
        silinecekNoktalar.map { n =>
            n.komşular.map(_.komşular -= n)
            seçme(n)
            n.resim.sil
        }
        val (silinecek, gerisi) = çizgiler.partition { çzg =>
            çzg match {
                case Çizgi(n1, n2) => silinecekNoktalar(n1) || silinecekNoktalar(n2)
                case _             => yanlış
            }
        }
        satıryaz(s"${silinecek.size} çizgi silinecek ")
        silinecek.map { çzg =>
            çzg.resim.sil()
        }
        çizgiler = gerisi
    }
    // satıryaz("Henüz hazır değil!")
}
def silmeBilgisi(silinecekNoktalar: Küme[Nokta]) = {
    val (silinecek, gerisi) = çizgiler.partition { çzg =>
        çzg match {
            case Çizgi(n1, n2) => silinecekNoktalar(n1) || silinecekNoktalar(n2)
            case _             => yanlış
        }
    }
    s"${silinecekNoktalar.size} nokta ve ${silinecek.size} çizgi silinecek"
}

def baştan(kns: Sayı) = { // Her nokta (0,0) yani orijine konuyor başta. Merak etme birazdan dağıtacağız
    val düğmeler = new Düğmeler(kns) { kur() }
    var s = 0 // yoksa komşu seti yanlış çalışıyor!
    val bns = kns * kns // başlangıçtaki nokta sayısı
    val noktalar3 = (0 until bns).foldLeft(Yöney[Nokta]())((v, i) => { s += 1; v :+ Nokta(s, 0); })
    // her bir çizgiyi tanımlar ve iki noktasına bağlarız. Bir balık ağı gibi. kns * kns düğümlü
    val çizgiler2 = (0 until bns).foldLeft(Yöney[Çizgi]())(
        (çv, i) => {
            val (x, y) = (i / kns, i % kns)
            val çzg = if (y < kns - 1) { çv :+ Çizgi(noktalar3(i), noktalar3(i + 1)) } else çv
            if (x < kns - 1) { çzg :+ Çizgi(noktalar3(i), noktalar3(i + kns)) } else çzg
        })
    çizgiler = çizgiler2.toSet
    noktalar2 = noktalar3
    noktalar = noktalar2.toSet
    serpiştir(noktalar, düğmeler) // rasgele dağıt ve çizgileri çiz
}

var seçiliNoktalar = Küme[Nokta]()
def seç(n: Nokta) = {
    seçiliNoktalar += n
    n.seçimiKur(doğru)
}
def seçme(n: Nokta) = {
    seçiliNoktalar -= n
    n.seçimiKur(yanlış)
}
def seçiliKümeyiYaz() = s"${seçiliNoktalar.size} nokta seçili: " +
    seçiliNoktalar.toList.map(n => n.ne).mkString("{", " ", "}")
// toList: dizine çevirir. map dizinin bütün elemanlarını verilen adsız işleve sokar
// mkString de hepsini yazıya çevirir

def doğruÇiz(llx: Kesir, lly: Kesir, urx: Kesir, ury: Kesir) = {
    val (en, boy) = (urx - llx, ury - lly)
    val r = götür(llx, lly) -> Resim.doğru(en, boy)
    çiz(r)
    r
}
def serpiştir(hepsi: Küme[Nokta], düğmeler: Düğmeler) { // her noktayı tuvalde rasgele bir yere yerleştir
    val (mx, my) = (tuvalAlanı.x, tuvalAlanı.y)
    val en = tuvalAlanı.eni - 2 * yarıçap
    val boy = tuvalAlanı.boyu - 2 * yarıçap
    def dene() = { // düğmelere değmesin!
        var dene = doğru
        var (x, y) = (0.0, 0.0)
        while (dene) {
            x = mx + yarıçap + en * rastgele
            y = my + yarıçap + boy * rastgele
            dene = düğmeler.düğmelereDeğdiMi(x, y)
        }
        (x, y)
    }
    hepsi.foreach(nkt => { val (x, y) = dene(); nkt.yeniKonum(x, y) })
    çizelim(çizgiler)
}
def çizelim(hepsi: Küme[Çizgi]) { // Her çizgi iki noktasının çemberine kadar gelsin
    hepsi.foreach(çzg => {
        val (x1, y1) = (çzg.n1.x, çzg.n1.y)
        val (x2, y2) = (çzg.n2.x, çzg.n2.y)
        val boy = karekökü(karesi(x2 - x1) + karesi(y2 - y1))
        val (xr, yr) = (yarıçap / boy * (x2 - x1), yarıçap / boy * (y2 - y1))
        çzg.resim.sil
        çzg.resim = doğruÇiz(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
    })
}

class Düğmeler(kns: Sayı) {
    def kur() { // düğmeleri bir grid üzerine koyalım
        düğmelerinİlkSırası() // dört sıra düğmemiz var
        düğmelerinİkinciSırası() // nokta/çizgi ekleme komutları
        düğmelerinÜçüncüSırası() // nokta seçme komutları
        düğmelerinDördüncüSırası() // nokta devinim komutları
    }
    def düğmelereDeğdiMi(x: Kesir, y: Kesir) = {
        val (llx, lly) = (kx - yarıçap, ky - yarıçap)
        // DİKKAT 4x3'lük alandan fazla düğme olursa bunu da büyüt
        val (urx, ury) = (kx + 3 * dGrid + yarıçap, ky + 4 * dGrid + yarıçap)
        llx < x && x < urx && lly < y && y < ury
    }
    val dGrid = 40 // düğmeleri sol alt köşede bir gride yerleştirelim
    val (kx, ky) = (0.9 * tuvalAlanı.x, 0.9 * tuvalAlanı.y) // sol alt köşe
    val dBoyu = dGrid - 5
    val (sıra1, sıra2, sıra3, sıra4) = (ky, ky + dGrid, ky + 2 * dGrid, ky + 3 * dGrid)
    val (sütn1, sütn2, sütn3, sütn4) = (kx, kx + dGrid, kx + 2 * dGrid, kx + 3 * dGrid)
    def ynkx = kx + 3 * dGrid - rastgele // yeni nokta konumu
    def ynky = {
        val ynky = ky + 4 * dGrid - rastgele // rastgele olmazsa seçiliNoktalar küme metodları hata veriyor
        rastgeleKesir(ynky - 4 * dGrid, ynky)
    }
    var yardım = Resim.arayüz(ay.Tanıt("Yardım"))
    def yardımEt(x: Kesir, y: Kesir, m: Yazı) = {
        yardım = Resim.arayüz(ay.Tanıt(m))
        yardım.konumuKur(sütn1, sıra4 + dGrid)
        yardım.büyütmeyiKur(2.0) // yazıyı büyütelim
        if (!yardım.çizili) çiz(yardım)
        else yardım.sil()
    }
    def yardımıKapat() = { yardım.sil() }
    def yardımıKur(b: Resim, mesaj: Yazı) = {
        b.fareGirince { (x, y) => yardımEt(x, y, mesaj) }
        b.fareÇıkınca { (_, _) => yardımıKapat }
    }
    def yardımıKur2(b: Resim, işlev: () => Yazı) {
        b.fareGirince { (x, y) => yardımEt(x, y, işlev()) }
        b.fareÇıkınca { (_, _) => yardımıKapat }
    }
    def kare(x: Kesir, y: Kesir, en: Kesir) = {
        val k = götür(x, y) * boyaRengi(kırmızı) -> Resim.dikdörtgen(en, en)
        çiz(k)
        k
    }
    def kaçTane(düzenli: İkil = doğru) = {
        if (düzenli) f"${noktalar.size}%2d nokta ve ${çizgiler.size}%2d çizgi var"
        else s"${noktalar.size} nokta ve ${çizgiler.size} çizgi var"
    }
    def düğmelerinİlkSırası() {
        val (b, b2) = (kare(sütn1, sıra1, dBoyu), kare(sütn2, sıra1, dBoyu))
        b.boyamaRenginiKur(kırmızı); b.fareyeTıklayınca { (_, _) => serpiştir(noktalar, this) }
        yardımıKur(b, "Noktaları rastgele serpiştir")
        b2.boyamaRenginiKur(turuncu); b2.fareyeTıklayınca { (_, _) => { yardımıKapat; toggleFullScreenCanvas } }
        yardımıKur(b2, "Tüm ekrana geç ya da tüm ekrandan çık")
        val b3 = kare(sütn3, sıra1, dBoyu)
        b3.boyamaRenginiKur(siyah); b3.fareyeTıklayınca { (_, _) => noktalarıSil(seçiliNoktalar) }
        yardımıKur2(b3, () =>
            if (seçiliNoktalar.isEmpty) "Seçili noktaları sil"
            else silmeBilgisi(seçiliNoktalar)
        )
    }
    def düğmelerinİkinciSırası() { // otomatik dış kenara (dışsal), d2b/c: seçili noktalara
        def d2() = {
            def kümeler(d: Int) = {
                def iç4Kenar() = { // ilk kare çizitin kenarları
                    val r1 = Yöney((0, 1), (d - 1, d), (d * d - 1, -1), (d * (d - 1), -d))
                    val l0 = (for ((a, c) <- (for (i <- 0 to 3) yield r1(i))) yield (a, (a + c * d), c)).toList
                    for ((a, b, c) <- l0) yield (Range(a, b, c).toList)
                }
                // dış kenarı, iki listeyi fermuar gibi bir araya getirerek buluyoruz
                def dış(i1: Dizin[Int], i2: Dizin[Int]) = {
                    val l3 = i2.zip(i1.tail ::: Dizin(i1.head)).flatMap(p => Dizin(p._1, p._2))
                    val l4 = l3 ::: Dizin(l3.head)
                    for (i <- 0 to 7 by 2) yield (l4.drop(i).take(3))
                }
                val d2 = d * d
                val l1 = Dizin(0, d - 1, d2 - 1, d2 - d)
                val l2 = Dizin(d2, d2 + 1, d2 + 2, d2 + 3)
                val l3 = l2.map(_ + 4)
                val l4 = l3.map(_ + 4)
                val l5 = l4.map(_ + 4)
                Dizin(iç4Kenar, dış(l1, l2), dış(l2, l3), dış(l3, l4), dış(l4, l5), dış(l5, l5.map(_ + 4))).flatMap(_.toList)
            }
            val b = kare(sütn1, sıra2, dBoyu) // mavi kare yeni bir nokta ekler
            yardımıKur2(b, () =>
                if (noktaSilindiMi) "Nokta silmeden önce kullan"
                else "Dışsal nokta ekle (çizgi yüzeysel kalır)")
            b.kalemRenginiKur(mavi)
            b.boyamaRenginiKur(mavi)
            var yeniNokta = 0 // kk'nin kaçıncı kümesine bağlanacak bu yeni nokta?
            val kk = kümeler(kns)
            satıryaz(kk.size + " dışsal nokta ekleyebilirsin")
            b.fareyeTıklayınca { (x, y) => // bu kareye her basışımızda yeni bir nokta ekleyelim
                if (noktaSilindiMi) {
                    satıryaz("Silinen noktalar var. Dışsal nokta ekleyemiyoruz artık!")
                }
                else if (yeniNokta < kk.size) {
                    val yn = Nokta(ynkx, ynky)
                    seç(yn)
                    noktalar += yn
                    noktalar2 = noktalar2 :+ yn
                    çizgiler = çizgiler ++ (for (i <- kk(yeniNokta)) yield (Çizgi(yn, noktalar2(i))))
                    yeniNokta += 1
                    satıryaz(kaçTane())
                    çizelim(çizgiler)
                }
                else {
                    b.kalemKalınlığınıKur(4)
                    b.kalemRenginiKur(kırmızı)
                    satıryaz("Başka nokta ekleyemiyoruz!")
                }
            }
        }
        def d2b() = {
            val b = kare(sütn2, sıra2, dBoyu)
            yardımıKur(b, "Seçili noktalara bağlı yeni bir nokta ekle")
            b.boyamaRenginiKur(sarı)
            b.fareyeTıklayınca { (x, y) => // nokta ekle ve bütün kümeye bağla
                val yn = Nokta(ynkx, ynky)
                noktalar += yn
                çizgiler = çizgiler ++ (for (en <- seçiliNoktalar) yield (Çizgi(yn, en)))
                seç(yn)
                satıryaz(kaçTane())
                çizelim(çizgiler)
            }
        }
        def ekle(yeni: Çizgi) = { çizgiler += yeni }
        def d2c() = { // seçili noktaları çizgiyle bağla
            def warn1(a: Nokta, b: Nokta) = satıryaz(s"DİKKAT: ${a.ne} ve ${b.ne} zaten bağlı")
            val b = kare(sütn3, sıra2, dBoyu)
            yardımıKur(b, "Seçili noktaları bağla")
            b.boyamaRenginiKur(kahverengi)
            b.fareyeTıklayınca { (x, y) =>
                if (seçiliNoktalar.size < 2) satıryaz("Önce en az iki nokta seçmelisin")
                else {
                    seçiliNoktalar.subsets(2).map(_.toList).foreach {
                        // todo: unapply
                        case List(a, b) => if (!a.komşuMu(b)) ekle(Çizgi(a, b)) else warn1(a, b)
                        case xs         => satıryaz(s"YANLIŞ! $xs")
                    }
                    çizelim(çizgiler)
                }
            }
        }
        d2(); d2b(); d2c()
    }
    def düğmelerinÜçüncüSırası() {
        def d3() = { // seçim kümesini sil, ya da hepsini seç
            val b = kare(sütn1, sıra3, dBoyu)
            yardımıKur(b, "Seçim kümesini sil ya da her noktayı seç")
            b.boyamaRenginiKur(yeşil)
            b.kalemKalınlığınıKur(4)
            b.fareyeTıklayınca { (x, y) =>
                if (!seçiliNoktalar.isEmpty) {
                    seçiliNoktalar.map(_.seçimiKur(yanlış)) // seçme(n)
                    seçiliNoktalar = seçiliNoktalar.empty
                }
                else noktalar.map(seç(_))
            }
        }
        def d3b() = { // seçili noktaları göster
            val b = kare(sütn2, sıra3, dBoyu)
            yardımıKur2(b, () => {
                val msj = s"${seçiliNoktalar.size} nokta seçili"
                if (seçiliNoktalar.size != 1) msj else s"$msj. ${seçiliNoktalar.head.komşular.size} komşusu var"
            })
            b.boyamaRenginiKur(yeşil)
            b.kalemKalınlığınıKur(4)
            b.kalemRenginiKur(mavi)
            b.fareyeTıklayınca { (x, y) =>
                val s = seçiliNoktalar.size
                if (s > 0) satıryaz(seçiliKümeyiYaz) else satıryaz("Seçilmiş nokta yok")
            }
        }
        def d3c() = {
            val b = kare(sütn3, sıra3, dBoyu)
            yardımıKur2(b, () => kaçTane(yanlış))
            b.boyamaRenginiKur(mor)
            b.fareyeTıklayınca { (x, y) =>
                satıryaz(kaçTane())
                if (!seçiliNoktalar.isEmpty)
                    seçiliNoktalar.map(n => satıryaz(s"$n: ${n.kim.size} komşusu var: ${n.kim}"))
            }
        }
        d3(); d3b(); d3c()
    }
    def düğmelerinDördüncüSırası() {
        def d4() = {
            val b = kare(sütn1, sıra4, dBoyu)
            yardımıKur(b, "Seçili noktayı komşularının ağırlık merkezine götür")
            b.boyamaRenginiKur(gri)
            b.fareyeTıklayınca { (x, y) =>
                if (seçiliNoktalar.size == 0) satıryaz("Nokta seçimiKur ki komşularının ağırlık merkezine götürelim")
                else seçiliNoktalar.map(_.merkezeGit())
            }
        }
        def d4b() = {
            val b = kare(sütn2, sıra4, dBoyu)
            yardımıKur(b, "Seçili noktanın komşularını yanına topla")
            b.boyamaRenginiKur(açıkGri)
            b.fareyeTıklayınca { (x, y) =>
                if (seçiliNoktalar.size == 0) satıryaz("Nokta seçimiKur ki komşularını çağıralım")
                else seçiliNoktalar.map(_.merkezeGetir())
            }
        }
        def d4c() = {
            val b = kare(sütn3, sıra4, dBoyu)
            yardımıKur(b, "Noktaları küçült ya da büyült")
            b.boyamaRenginiKur(renkler.turquoise)
            b.fareyeTıklayınca { (_, _) =>
                yarıçap = yarıçap match {
                    case 10 => 1
                    case 1  => 10
                    case _  => 10
                }
                noktalar.map(_.yineÇiz())
                çizelim(çizgiler)
            }
        }
        d4(); d4b(); d4c()
    }
}
baştan(KNS)
