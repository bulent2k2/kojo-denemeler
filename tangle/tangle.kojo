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
    var resim = doğruÇiz(n1.x, n1.y, n2.x, n2.y)
    n1.komşu(n2)
    n2.komşu(n1)
}

var nkn = 0 // nokta kimlik numarası
case class Nokta(var x: Kesir, var y: Kesir) {
    // no/equals/hashCode Küme'nin noktaları birbirinden ayırabilmesi için gerekli
    val no = nkn
    nkn += 1
    override def equals(ne: Her) = ne match { // bu nokta verilen nesneyle aynı mı?
        case o: Nokta => o.no == no
        case _        => yanlış
    }
    override def hashCode = no.hashCode // her nesnenin kendine özgü bir sayıya çevrilmesinde fayda var
    // iki nesnenin bu karışık kodu farklıysa, nesneler de farklıdır. Değilse, o zaman daha yavaş olan equals metodu kullanılır
    val buNokta = this
    var resim: Resim = başla(); seçimiKur(yanlış)
    private def başla() = {
        val res = götür(x, y) -> Resim.daire(yarıçap)
        res.çiz
        fareyiTanımla(res)
        res
    }
    var komşular = Küme[Nokta]()
    def komşuMu(n: Nokta) = komşular(n)
    def komşu(n: Nokta) =
        if (n != buNokta) komşular += n
        else satıryaz("YANLIŞ! " + buNokta.yaz)
    def kim() = komşular
    def yineÇiz() {
        resim.sil
        resim = başla()
        seçimiKur(seçiliNoktalar(buNokta))
    }
    def seçimiKur(s: İkil) = { // seçilmiş Noktalar farklı görünsün
        resim.kalemKalınlığınıKur(if (s) 4 else 2)
        resim.kalemRenginiKur(mavi)
        resim.boyamaRenginiKur(if (s) kırmızı else (renksiz))
    }
    def yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        resim.konumuKur(yeniX, yeniY)
    }
    override def toString = ne
    def yaz = s"$ne d=${kim.size}"
    def ne = s"N$no@${yuvarla(x).toInt},${yuvarla(y).toInt}"

    def fareyiTanımla(r: Resim) = {
        r.fareyeTıklayınca { (mx, my) =>
            if (seçiliNoktalar(buNokta)) seçme(buNokta) else seç(buNokta)
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
    def merkezeGetir() = { // komşumuz olan noktaları yakın yörüngeye çekelim.
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

/* todo
   noktaları bir grid üstüne koysak? Üstüste hiç getirmesek? Ama ya yaklaşınca uzaklaşınca?
   sahneyi sınırlarız olur biter...
val GNS = 100
val grid = Dizim.boş[Nokta](GNS, GNS)*/

var noktaSilindiMi = yanlış
var çizgiEklendiMi = yanlış
def çelişkiVarMı(nktlar: Küme[Nokta]) = {
    for (n1 <- nktlar) {
        for (n2 <- nktlar) {
            if (n1 != n2 &&
                n1.komşuMu(n2) != n2.komşuMu(n1)) {
                satıryaz(hataMesajı(n1, n2))
            }
        }
    }
}
def hataMesajı(n1: Nokta, n2: Nokta) = s"\ntek yanlı komşuluk! $n1 ve $n2." +
    s"\n${n1.no}->${n2.no}: ${n1.komşuMu(n2)}" +
    s"\n${n2.no}->${n1.no}: ${n2.komşuMu(n1)}"
def tekÇizgiMiSilelim(snk: Küme[Nokta]): İkil = {
    if (snk.size != 2) yanlış else {
        val (n1, n2) = (snk.head, snk.tail.head)
        val komşuMu = n1.komşuMu(n2)
        gerekli(komşuMu == n2.komşuMu(n1), hataMesajı(n1, n2))
        komşuMu
    }
}
def noktalarYaDaTekBirÇizgiSil(silinecekNoktalar: Küme[Nokta]) = {
    if (silinecekNoktalar.isEmpty) {
        satıryaz("Önce en az bir nokta seçmelisin")
    }
    else {
        val tekÇizgiSilinsin = tekÇizgiMiSilelim(silinecekNoktalar)
        if (!tekÇizgiSilinsin) {
            noktaSilindiMi = doğru
            val kalanNoktalar = noktalar.filterNot { silinecekNoktalar(_) }
            noktalar = kalanNoktalar
            noktalar2 = noktalar.toVector
            silinecekNoktalar.map { n =>
                n.komşular.map(_.komşular -= n)
                seçme(n)
                n.resim.sil
            }
        }
        else {
            val snk = silinecekNoktalar
            val (n1, n2) = (snk.head, snk.tail.head)
            n1.komşular -= n2
            n2.komşular -= n1
        }
        val (silinecek, gerisi) = çizgiler.partition { çzg =>
            çzg match {
                case Çizgi(n1, n2) => if (tekÇizgiSilinsin) {
                    silinecekNoktalar(n1) && silinecekNoktalar(n2)
                }
                else {
                    silinecekNoktalar(n1) || silinecekNoktalar(n2)
                }
                case _ => yanlış
            }
        }
        satıryaz(s"${silinecek.size} çizgi silinecek ")
        silinecek.map { çzg =>
            çzg.resim.sil()
        }
        çizgiler = gerisi
    }
}
def silmeBilgisi(silinecekNoktalar: Küme[Nokta]) = {
    val tekÇizgiSilinsin = tekÇizgiMiSilelim(silinecekNoktalar)
    if (tekÇizgiSilinsin) s"Seçili çizgiyi sil" else {
        val (silinecek, gerisi) = çizgiler.partition { çzg =>
            çzg match {
                case Çizgi(n1, n2) => silinecekNoktalar(n1) || silinecekNoktalar(n2)
                case _             => yanlış
            }
        }
        s"Seçili ${silinecekNoktalar.size} nokta ve ${silinecek.size} çizgiyi sil"
    }
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

def doğruÇiz(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir) = {
    val (en, boy) = (x2 - x1, y2 - y1)
    val r = götür(x1, y1) -> Resim.düz(en, boy)
    r.çiz
    r
}
def serpiştir(hepsi: Küme[Nokta], düğmeler: Düğmeler) {
    // noktaları tuvalde rasgele yerleştirelim ama düğmelerin üstüne gelmesinlar
    val (mx, my) = (tuvalAlanı.x, tuvalAlanı.y)
    val en = tuvalAlanı.eni - 2 * yarıçap
    val boy = tuvalAlanı.boyu - 2 * yarıçap
    def deney() = { // düğmelere değmesin!
        var deney = doğru
        var (x, y) = (0.0, 0.0)
        while (deney) {
            x = mx + yarıçap + en * rasgele
            y = my + yarıçap + boy * rasgele
            deney = düğmeler.düğmelereDeğdiMi(x, y)
        }
        (x, y)
    }
    hepsi.foreach(nkt => { val (x, y) = deney(); nkt.yeniKonum(x, y) })
    çizelim(çizgiler)  // çizgileri de yeniden çizelim
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
def kaçTane(düzenli: İkil = doğru) = {
    if (düzenli) f"${noktalar.size}%2d nokta ve ${çizgiler.size}%2d çizgi var"
    else s"${noktalar.size} nokta ve ${çizgiler.size} çizgi var"
}

class Düğmeler(kns: Sayı) {
    def kur() { // düğmeleri bir grid üzerine koyalım
        düğmelerinİlkSırası() // dört sıra düğmemiz var
        düğmelerinİkinciSırası() // nokta/çizgi ekleme komutları
        düğmelerinÜçüncüSırası() // nokta seçme komutları
        düğmelerinDördüncüSırası() // nokta devinim komutları
    }
    def düğmelereDeğdiMi(x: Kesir, y: Kesir) = {
        val (x1, y1) = (kx - yarıçap, ky - yarıçap)
        // DİKKAT 4x3'lük alandan fazla düğme olursa bunu da büyüt
        val (x2, y2) = (kx + 3 * dGrid + yarıçap, ky + 4 * dGrid + yarıçap)
        x1 < x && x < x2 && y1 < y && y < y2
    }
    val dGrid = 40 // düğmeleri sol alt köşede bir gride yerleştirelim
    val (kx, ky) = (0.9 * tuvalAlanı.x, 0.9 * tuvalAlanı.y) // sol alt köşe
    val dBoyu = dGrid - 5
    val (sıra1, sıra2, sıra3, sıra4) = (ky, ky + dGrid, ky + 2 * dGrid, ky + 3 * dGrid)
    val (sütn1, sütn2, sütn3, sütn4) = (kx, kx + dGrid, kx + 2 * dGrid, kx + 3 * dGrid)
    // yeni nokta konumu
    def ynkx = { // rastgele olmazsa seçiliNoktalar küme metodları hata veriyor
        val yeniZ = kx + 3 * dGrid + yarıçap
        rastgeleKesir(yeniZ, yeniZ + 2 * dGrid + yarıçap)
    }
    def ynky = {
        val yeniZ = ky - yarıçap
        rastgeleKesir(yeniZ, yeniZ + 4 * dGrid + yarıçap)
    }
    var yardım = Resim.arayüz(ay.Tanıt("Yardım"))
    def yardımEt(x: Kesir, y: Kesir, m: Yazı) = {
        yardım = Resim.arayüz(ay.Tanıt(m))
        yardım.konumuKur(sütn1, sıra4 + dGrid)
        yardım.büyütmeyiKur(2.0) // yazıyı büyütelim
        if (!yardım.çizili) yardım.çiz
        else yardım.sil()
    }
    def yardımıKapat() = { yardım.sil() }
    def yardımıKur(r: Resim, mesaj: Yazı) = {
        r.fareGirince { (x, y) => yardımEt(x, y, mesaj) }
        r.fareÇıkınca { (_, _) => yardımıKapat }
    }
    def yardımıKur2(r: Resim, işlev: () => Yazı) {
        r.fareGirince { (x, y) => yardımEt(x, y, işlev()) }
        r.fareÇıkınca { (_, _) => yardımıKapat }
    }
    def kare(x: Kesir, y: Kesir, en: Kesir) = {
        val k = götür(x, y) * kalemRengi(renksiz) -> Resim.dikdörtgen(en, en)
        k.çiz
        k
    }
    def düğmelerinİlkSırası() {
        val (b1, b2) = (kare(sütn1, sıra1, dBoyu), kare(sütn2, sıra1, dBoyu))
        b1.boyamaRenginiKur(kırmızı); b1.fareyeTıklayınca { (_, _) => serpiştir(noktalar, this) }
        yardımıKur(b1, "Noktaları rastgele serpiştir")
        b2.boyamaRenginiKur(turuncu); b2.fareyeTıklayınca { (_, _) => { yardımıKapat; toggleFullScreenCanvas } }
        yardımıKur(b2, "Tüm ekrana geç ya da tüm ekrandan çık")
        val b3 = kare(sütn3, sıra1, dBoyu)
        b3.boyamaRenginiKur(siyah)
        b3.fareyeTıklayınca { (_, _) => noktalarYaDaTekBirÇizgiSil(seçiliNoktalar); yardımıKapat() }
        yardımıKur2(b3, () =>
            if (seçiliNoktalar.isEmpty) "Seçili çizgiyi ya da noktaları sil"
            else silmeBilgisi(seçiliNoktalar)
        )
    }
    def düğmelerinİkinciSırası() { // otomatik dış kenara (dışsal), d2b/c: seçili noktalara
        def d2a() = {
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
            val düğme = kare(sütn1, sıra2, dBoyu) // mavi kare yeni bir nokta ekler
            var kümeTükendi = yanlış
            yardımıKur2(düğme, () =>
                if (noktaSilindiMi) "Nokta silmeden önce kullan"
                else if (çizgiEklendiMi) "Çizgi eklemeden önce kullan"
                else if (kümeTükendi) "Başka dışsal nokta ekleyemiyoruz"
                else "Dışsal nokta ekle (çizgi yüzeysel kalır)")
            düğme.kalemRenginiKur(mavi)
            düğme.boyamaRenginiKur(mavi)
            var yeniNokta = 0 // kk'nin kaçıncı kümesine bağlanacak bu yeni nokta?
            val kk = kümeler(kns)
            satıryaz(kk.size + " dışsal nokta ekleyebilirsin")
            düğme.fareyeTıklayınca { (x, y) => // bu kareye her basışımızda yeni bir nokta ekleyelim
                val msj = (ne: Yazı) => satıryaz(s"$ne var. Dışsal nokta ekleyemiyoruz artık!")
                if (noktaSilindiMi) msj("Silinen noktalar")
                else if (çizgiEklendiMi) msj("Eklenen çizgiler")
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
                    kümeTükendi = doğru
                    satıryaz("Başka dışsal nokta ekleyemiyoruz (şimdilik!)")
                }
                if (noktaSilindiMi || çizgiEklendiMi || kümeTükendi)
                    düğme.saydamlığıKur(0.2)
            }
        }
        def d2b() = {
            val düğme = kare(sütn2, sıra2, dBoyu)
            yardımıKur(düğme, "Seçili noktalara bağlı yeni bir nokta ekle")
            düğme.boyamaRenginiKur(sarı)
            düğme.fareyeTıklayınca { (x, y) => // nokta ekle ve bütün kümeye bağla
                val yn = Nokta(ynkx, ynky)
                noktalar += yn
                çizgiler = çizgiler ++ (for (en <- seçiliNoktalar) yield (Çizgi(yn, en)))
                seç(yn)
                satıryaz(kaçTane())
                çizelim(çizgiler)
            }
        }
        def çizgiEkle(çizgi: Çizgi) = { çizgiler += çizgi }
        def d2c() = { // seçili noktaları çizgiyle bağla
            def warn1(n1: Nokta, n2: Nokta) = satıryaz(s"DİKKAT: ${n1.ne} ve ${n2.ne} zaten bağlı")
            val düğme = kare(sütn3, sıra2, dBoyu)
            yardımıKur(düğme, "Seçili noktaları bağla")
            düğme.boyamaRenginiKur(kahverengi)
            düğme.fareyeTıklayınca { (x, y) =>
                if (seçiliNoktalar.size < 2) satıryaz("Önce en az iki nokta seçmelisin")
                else {
                    seçiliNoktalar.subsets(2).map(_.toList).foreach {
                        case Dizin(n1, n2) => if (!n1.komşuMu(n2)) çizgiEkle(Çizgi(n1, n2)) else warn1(n1, n2)
                        case xs            => satıryaz(s"YANLIŞ! $xs")
                    }
                    çizelim(çizgiler)
                    çizgiEklendiMi = doğru
                }
            }
        }
        d2a(); d2b(); d2c()
    }
    def düğmelerinÜçüncüSırası() {
        def d3a() = { // seçim kümesini sil, ya da hepsini seç
            val düğme = kare(sütn1, sıra3, dBoyu)
            yardımıKur(düğme, "Seçim kümesini sil ya da her noktayı seç")
            düğme.boyamaRenginiKur(yeşil)
            düğme.kalemKalınlığınıKur(4)
            düğme.fareyeTıklayınca { (x, y) =>
                if (!seçiliNoktalar.isEmpty) {
                    seçiliNoktalar.map(_.seçimiKur(yanlış)) // seçme(n)
                    seçiliNoktalar = seçiliNoktalar.empty
                }
                else noktalar.map(seç(_))
            }
        }
        def d3b() = { // seçili noktaları göster
            val düğme = kare(sütn2, sıra3, dBoyu)
            yardımıKur2(düğme, () => {
                val msj = s"${seçiliNoktalar.size} nokta seçili"
                if (seçiliNoktalar.size != 1) msj else s"$msj. ${seçiliNoktalar.head.komşular.size} komşusu var"
            })
            düğme.boyamaRenginiKur(renkler.darkGreen)
            düğme.fareyeTıklayınca { (x, y) =>
                val s = seçiliNoktalar.size
                if (s > 0) satıryaz(seçiliKümeyiYaz) else satıryaz("Seçilmiş nokta yok")
            }
        }
        def d3c() = {
            val düğme = kare(sütn3, sıra3, dBoyu)
            yardımıKur2(düğme, () => kaçTane(yanlış))
            düğme.boyamaRenginiKur(mor)
            düğme.fareyeTıklayınca { (x, y) =>
                satıryaz(kaçTane())
                if (!seçiliNoktalar.isEmpty) {
                    val komşular = (n: Nokta) => n.kim.mkString("{",", ","}")
                    seçiliNoktalar.map(n => satıryaz(f"$n%11s: ${n.kim.size} komşusu var: ${komşular(n)}"))
                    if (seçiliNoktalar.size == 2) {
                        val (n1, n2) = (seçiliNoktalar.head, seçiliNoktalar.tail.head)
                        val msj = "seçili iki nokta komşu"
                        satıryaz(msj + (if (n1.komşuMu(n2)) "lar" else " değiller"))
                    }
                    çelişkiVarMı(seçiliNoktalar)
                }
            }
        }
        d3a(); d3b(); d3c()
    }
    def düğmelerinDördüncüSırası() {
        def d4a() = {
            val düğme = kare(sütn1, sıra4, dBoyu)
            yardımıKur(düğme, "Seçili noktayı komşularının ağırlık merkezine götür")
            düğme.boyamaRenginiKur(gri)
            düğme.fareyeTıklayınca { (x, y) =>
                if (seçiliNoktalar.size == 0) satıryaz("Nokta seçimiKur ki komşularının ağırlık merkezine götürelim")
                else seçiliNoktalar.map(_.merkezeGit())
            }
        }
        def d4b() = {
            val düğme = kare(sütn2, sıra4, dBoyu)
            yardımıKur(düğme, "Seçili noktanın komşularını yanına topla")
            düğme.boyamaRenginiKur(açıkGri)
            düğme.fareyeTıklayınca { (x, y) =>
                if (seçiliNoktalar.size == 0) satıryaz("Nokta seçimiKur ki komşularını çağıralım")
                else seçiliNoktalar.map(_.merkezeGetir())
            }
        }
        def d4c() = {
            val düğme = kare(sütn3, sıra4, dBoyu)
            yardımıKur(düğme, "Noktaları küçült ya da büyült")
            düğme.boyamaRenginiKur(renkler.turquoise)
            düğme.fareyeTıklayınca { (_, _) =>
                yarıçap = yarıçap match {
                    case 10 => 1
                    case 1  => 10
                    case _  => 10
                }
                noktalar.map(_.yineÇiz())
                çizelim(çizgiler)
            }
        }
        d4a(); d4b(); d4c()
    }
}
baştan(KNS)
