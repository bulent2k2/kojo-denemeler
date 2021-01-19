import Staging._ // şu komutlar gelsin bakalım: line, circle, square
import Staging.{ circle, clear } // bu komut adları çatışıyor
import math.pow, math.random

clear(); çıktıyıSil()
// Kare grid çizgesi kuracağız. düzlemseldir.
val KNS = 4 // Karenin bir kenarında kaç tane nokta olsun? kns arttıkça oyun zorlaşır.
// başlangıçtaki nokta sayısı = kns*kns. 
val YÇ = 10 // bu da noktanın yarıçapı
var çizgiler = Vector[Çizgi]() // boş küme olarak başlarız
var noktalar = Vector[Nokta]()

case class Çizgi(n1: Nokta, n2: Nokta) { // iki noktayı bağla
    require(n1 != n2, s"YANLIŞ! döngü ${n1.yaz}")
    require(!n1.komşuMu(n2), s"YANLIŞ! ${n1.yaz} ve ${n2.yaz} zaten bağlı")
    //satıryaz(s"${n1.yaz} ve ${n2.yaz} bağlanıyor")
    var çizgi = line(n1.x, n1.y, n2.x, n2.y) // bir doğru çizer
    n1.komşu(n2)
    n2.komşu(n1)
}

var selection = Set[Nokta]()
def seç(n: Nokta) = {
    selection += n
    n.sel(doğru)
}
def seçme(n: Nokta) = {
    selection -= n
    n.sel(yanlış)
}
def seçiliKümeyiYaz() = s"${selection.size} nokta seçili: " +
    selection.toList.map(n => n.ne).mkString("{", " ", "}")

case class Nokta(var x: Kesir, var y: Kesir) {
    val bu = this
    var komşular = Set[Nokta]()
    def komşuMu(n: Nokta) = komşular.contains(n)
    def komşu(n: Nokta) =
        if (n != bu) komşular += n
        else satıryaz("YANLIŞ! " + bu.yaz)
    def kim() = komşular
    val n = circle(x, y, YÇ) // verilen x ve y coordinatı merkezimiz. yarıçap belli.
    def sel(s: Boolean) = {
        n.setPenThickness(if (s) 6 else 2)
        n.setPenColor(kırmızı)
        n.setFillColor(if (s) mavi else (renksiz))
    }
    sel(yanlış)
    def yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        n.setPosition(yeniX, yeniY)
    }

    override def toString = s"N(${round(x, 2)},${round(y, 2)})"
    def yaz = s"$ne d=${kim.size}"
    def ne = s"N[${round(x, 0)} ${round(y, 0)}]"

    // bbx todo double click?
    n.onMouseClick { (mx, my) =>
        if (selection.contains(bu)) seçme(bu) else seç(bu)
        satıryaz(s"$yaz. $seçiliKümeyiYaz")
    }
    // çekince bu çalışacak. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
    n.onMouseDrag { (mx, my) => { yeniKonum(mx, my); çizelim(çizgiler) } }
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
        val (s, g) = (komşular.size, 2 * YÇ)
        val tg = 2 * g
        if (s == 0) satıryaz("Komşum yok!") else {
            val ikik = 24 // bundan çok komşu varsa, iki kere çevirsek de yetmez
            val evre = List((g, g), (g, 0), (g, -g), (0, -g), (-g, -g), (-g, 0), (-g, g), (0, g))
            val e2 = if (s < 9) evre else { //                                          21012
                if (s > ikik) satıryaz(s"$ikik'ten çok komşu var! " + //                g...g
                    s"${s - ikik} tanesini elinle yap, ne olur!") //                    0.x.0
                evre ::: List((tg, tg), (tg, g), (tg, 0), (tg, -g), (tg, -tg), //       -...-
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

def serpiştir(hepsi: Vector[Nokta]) { // her noktayı tuvalde rasgele bir yere yerleştir
    val (mx, my) = (canvasBounds.getMinX, canvasBounds.getMinY)
    val en = canvasBounds.getMaxX - mx - 2 * YÇ
    val boy = canvasBounds.getMaxY - my - 2 * YÇ
    def dene() = { // düğmelere değmesin!
        var dene = doğru
        var (x, y) = (0.0, 0.0)
        while (dene) {
            x = mx + YÇ + en * random
            y = my + YÇ + boy * random
            dene = düğmelereDeğdiMi(x, y)
        }
        (x, y)
    }
    hepsi.foreach(nkt => { val (x, y) = dene(); nkt.yeniKonum(x, y) })
    çizelim(çizgiler)
}
def çizelim(hepsi: Vector[Çizgi]) { // Her çizgi iki noktasının çemberine kadar gelsin
    hepsi.foreach(çzg => {
        val (x1, y1) = (çzg.n1.x, çzg.n1.y)
        val (x2, y2) = (çzg.n2.x, çzg.n2.y)
        val boy = sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2))
        val (xr, yr) = (YÇ / boy * (x2 - x1), YÇ / boy * (y2 - y1))
        çzg.çizgi.erase
        çzg.çizgi = line(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
    })
}
def baştan(kns: Sayı) = { // Her nokta (0,0) yani orijine konuyor başta. Merak etme birazdan dağıtacağız
    var s = 0 // yoksa komşu seti yanlış çalışıyor!
    val bns = kns * kns // başlangıçtaki nokta sayısı
    noktalar = (0 until bns).foldLeft(Vector[Nokta]())((v, i) => { s += 1; v :+ Nokta(s, 0); })
    // çizgileri tanımlar ve iki noktasına bağlarız. Bir balık ağı gibi. kns * kns düğümlü
    çizgiler = (0 until bns).foldLeft(Vector[Çizgi]())(
        (çv, i) => {
            val (x, y) = (i / kns, i % kns)
            val çzg = if (y < kns - 1) { çv :+ Çizgi(noktalar(i), noktalar(i + 1)) } else çv
            if (x < kns - 1) { çzg :+ Çizgi(noktalar(i), noktalar(i + kns)) } else çzg
        })
    serpiştir(noktalar) // rasgele dağıt ve çizgileri çiz
    //axesOn; gridOn
    düğmeler(kns)
}
def kaçTane() = f"${noktalar.size}%2d nokta ve ${çizgiler.size}%2d çizgi var"
val dGrid = 40
val dBoyu = dGrid - 5
val (kx, ky) = (0.9 * canvasBounds.getMinX, 0.9 * canvasBounds.getMinY)
def düğmelereDeğdiMi(x: Kesir, y: Kesir) = {
    val (llx, lly) = (kx - YÇ, ky - YÇ)
    // DİKKAT 4x3'lük alandan fazla düğme olursa bunu da büyüt
    val (urx, ury) = (kx + 3 * dGrid + YÇ, ky + 4 * dGrid + YÇ)
    llx < x && x < urx && lly < y && y < ury
}
def düğmeler(kns: Sayı) = {
    // place the buttons in rows and columns
    val (row1, row2, row3, row4) = (ky, ky + dGrid, ky + 2 * dGrid, ky + 3 * dGrid)
    val (col1, col2, col3, col4) = (kx, kx + dGrid, kx + 2 * dGrid, kx + 3 * dGrid)
    def ynkx = kx + 3 * dGrid - random // yeni nokta konumu
    def ynky = ky + 4 * dGrid - random // random for selection set equality check
    var yardım = Picture.widget(Label("Yardım"))
    def yardımEt(x: Kesir, y: Kesir, m: Yazı) = {
        yardım = Picture.widget(Label(m))
        yardım.setPosition(col1, row4 + dGrid)
        yardım.setScale(2.0) // yazıyı büyütelim
        if (!yardım.drawn) draw(yardım)
        else yardım.erase()
    }
    def yardımıKapat() = { yardım.erase() }
    def çiz() = {
        // dört sıra düğmemiz var.
        // Birinci sıradaki iki sütun:
        val (b, b2) = (square(col1, row1, dBoyu), square(col2, row1, dBoyu))
        b.setFillColor(kırmızı); b.onMouseClick { (_, _) => serpiştir(noktalar) }
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Noktaları rastgele serpiştir") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b2.setFillColor(turuncu); b2.onMouseClick { (_, _) => { yardımıKapat; toggleFullScreenCanvas } }
        b2.onMouseEnter { (x, y) => yardımEt(x, y, "Tüm ekrana geç ya da tüm ekrandan çık") }
        b2.onMouseExit { (_, _) => yardımıKapat }
        // nokta/çizgi ekleme komutları: d2: otomatik dış kenara, d2b/c: seçili noktalara
        d2(); d2b(); d2c()
        // nokta seçme komutları
        d3(); d3b(); d3c()
        // nokta devinim komutları
        d4(); d4b()
    }
    def d2() = {
        val b = square(col1, row2, dBoyu) // mavi kare yeni bir nokta ekler
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Dışsal nokta ekle (çizgi yüzeysel kalır)") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setPenColor(mavi)
        b.setFillColor(mavi)
        var yeniNokta = 0 // kk'nin kaçıncı kümesine bağlanacak bu yeni nokta?
        val kk = kümeler(kns)
        satıryaz(kk.size + " dışsal nokta ekleyebilirsin")
        b.onMouseClick { (x, y) => // bu kareye her basışımızda yeni bir nokta ekleyelim
            if (yeniNokta < kk.size) {
                val yn = Nokta(ynkx, ynky)
                seç(yn)
                noktalar = noktalar :+ yn
                çizgiler = çizgiler ++ (for (i <- kk(yeniNokta)) yield (Çizgi(yn, noktalar(i))))
                yeniNokta += 1
                satıryaz(kaçTane())
                çizelim(çizgiler)
            }
            else {
                b.setPenThickness(4)
                b.setPenColor(kırmızı)
                satıryaz("Başka nokta ekleyemiyoruz!")
            }
        }
    }
    def d2b() = {
        val b = square(col2, row2, dBoyu)
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Seçili noktalara bağlı yeni bir nokta ekle") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(sarı)
        b.onMouseClick { (x, y) => // nokta ekle ve bütün kümeye bağla
            val yn = Nokta(ynkx, ynky)
            noktalar = noktalar :+ yn
            çizgiler = çizgiler ++ (for (en <- selection) yield (Çizgi(yn, en)))
            seç(yn)
            satıryaz(kaçTane())
            çizelim(çizgiler)
        }
    }
    def ekle(yeni: Çizgi) = { çizgiler = çizgiler :+ yeni }
    def d2c() = { // seçili noktaları çizgiyle bağla
        def warn1(a: Nokta, b: Nokta) = satıryaz(s"DİKKAT: ${a.ne} ve ${b.ne} zaten bağlı")
        val b = square(col3, row2, dBoyu)
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Seçili noktaları bağla") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(kahverengi)
        b.onMouseClick { (x, y) =>
            if (selection.size < 2) satıryaz("Önce en az iki nokta seçmelisin")
            else {
                selection.subsets(2).map(_.toList).foreach {
                    case List(a, b) => if (!a.komşuMu(b)) ekle(Çizgi(a, b)) else warn1(a, b)
                    case xs         => satıryaz(s"YANLIŞ! $xs")
                }
                çizelim(çizgiler)
            }
        }
    }
    def d3() = { // seçim kümesini sil, ya da hepsini seç
        val b = square(col1, row3, dBoyu)
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Seçim kümesini sil ya da her noktayı seç") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(yeşil)
        b.setPenThickness(4)
        b.onMouseClick { (x, y) =>
            if (!selection.isEmpty) {
                selection.map(_.sel(yanlış)) // seçme(n)
                selection = selection.empty
            }
            else noktalar.map(seç(_))
        }
    }
    def d3b() = { // seçili noktaları göster
        val b = square(col2, row3, dBoyu)
        b.onMouseEnter { (x, y) =>
            {
                val s = selection.size
                val mesaj = if (s == 1) {
                    val k = selection.head.komşular.size;
                    s"$s nokta şeçili. $k komşusu var"
                }
                else s"$s nokta seçili"
                yardımEt(x, y, mesaj)
            }
        }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(yeşil)
        b.setPenThickness(4)
        b.setPenColor(mavi)
        b.onMouseClick { (x, y) =>
            val s = selection.size
            if (s > 0) satıryaz(seçiliKümeyiYaz) else satıryaz("Seçilmiş nokta yok")
        }
    }
    def d3c() = {
        val b = square(col3, row3, dBoyu)
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Şu anda " + kaçTane()) }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(mor)
        b.onMouseClick { (x, y) =>
            satıryaz(kaçTane())
            //satıryaz(canvasBounds)
            if (!selection.isEmpty)
                selection.map(n => satıryaz(s"$n: ${n.kim.size} komşusu var: ${n.kim}"))
        }
    }
    def d4() = {
        val b = square(col1, row4, dBoyu)
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Seçili noktayı komşularının ağırlık merkezine götür") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(gri)
        b.onMouseClick { (x, y) =>
            if (selection.size == 0) satıryaz("Nokta seç ki komşularının ağırlık merkezine götürelim")
            else selection.map(_.merkezeGit())
        }
    }
    def d4b() = {
        val b = square(col2, row4, dBoyu)
        b.onMouseEnter { (x, y) => yardımEt(x, y, "Seçili noktanın komşularını yanına topla") }
        b.onMouseExit { (_, _) => yardımıKapat }
        b.setFillColor(açıkGri)
        b.onMouseClick { (x, y) =>
            if (selection.size == 0) satıryaz("Nokta seç ki komşularını çağıralım")
            else selection.map(_.merkezeGetir())
        }
    }

    def kümeler(d: Int) = {
        def iç4Kenar() = { // ilk kare çizitin kenarları
            val r1 = Vector((0, 1), (d - 1, d), (d * d - 1, -1), (d * (d - 1), -d))
            val l0 = (for ((a, c) <- (for (i <- 0 to 3) yield r1(i))) yield (a, (a + c * d), c)).toList
            for ((a, b, c) <- l0) yield (Range(a, b, c).toList)
        }
        // dış kenarı, iki listeyi fermuar gibi bir araya getirerek buluyoruz
        def dış(i1: List[Int], i2: List[Int]) = {
            val l3 = i2.zip(i1.tail ::: List(i1.head)).flatMap(p => List(p._1, p._2))
            val l4 = l3 ::: List(l3.head)
            for (i <- 0 to 7 by 2) yield (l4.drop(i).take(3))
        }
        val d2 = d * d
        val l1 = List(0, d - 1, d2 - 1, d2 - d)
        val l2 = List(d2, d2 + 1, d2 + 2, d2 + 3)
        val l3 = l2.map(_ + 4)
        val l4 = l3.map(_ + 4)
        val l5 = l4.map(_ + 4)
        List(iç4Kenar, dış(l1, l2), dış(l2, l3), dış(l3, l4), dış(l4, l5), dış(l5, l5.map(_ + 4))).flatMap(_.toList)
    }
    çiz()
}
baştan(KNS)
