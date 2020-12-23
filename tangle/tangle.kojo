import Staging._ // şu komutlar gelsin bakalım: line, circle, square
import Staging.{ circle, clear } // bu komut adları çatışıyor
import math.pow, math.random

// KS arttıkça oyun zorlaşır. Bir kenarda kaç tane nokta olsun?
val KS = 4; val AS = KS * KS
val YÇ = 20 // bu da noktaların yarıçapı
// her çizgi iki noktayı bağlar
case class Çizgi(n1: Nokta, n2: Nokta) {
    var çizgi = line(n1.x, n1.y, n2.x, n2.y) // bir doğru çizer
}
// bütün çizgiler. boş küme olarak başlarız
var çizgiler = Vector[Çizgi]()
// Noktayı tuvalde kaydıracağız. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
case class Nokta(var x: Kesir, var y: Kesir) {
    val n = circle(x, y, YÇ) // verilen x ve y coordinatı merkezimiz. yarıçap belli.
    n.setFillColor(mavi) // rengi mavi olsun
    def yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        n.setPosition(yeniX, yeniY)
    }
    // fareye tıklayıp çekince bu çalışacak
    n.onMouseDrag { (mx, my) => { n.setPosition(mx, my); x = mx; y = my; çizelim(çizgiler) } }
}
// Bütün noktaları (0,0) yani orijine üştüste koyalım. Merak etme birazdan dağıtacağız
clear()
val noktalar = (0 until AS).foldLeft(Vector[Nokta]())((v, i) => { v :+ Nokta(0, 0) })

// çizgileri tanımlar ve noktalara bağlarız. Bir balık ağı gibi. KS * KS düğümlü
çizgiler = (0 until AS).foldLeft(Vector[Çizgi]())(
    (çv, i) => {
        val (x, y) = (i / KS, i % KS)
        val çzg = if (y < KS - 1) { çv :+ Çizgi(noktalar(i), noktalar(i + 1)) } else çv
        if (x < KS - 1) { çzg :+ Çizgi(noktalar(i), noktalar(i + KS)) } else çzg
    })
serpiştir(noktalar) // noktaları yerleştir ve çizgileri çiz

// noktaları rasgele yerleştir
def serpiştir(hepsi: Vector[Nokta]) {
    hepsi.foreach(nkt => nkt.yeniKonum(KS * YÇ * 6 * (random - 0.5), KS * YÇ * 6 * (random - 0.5)))
    çizelim(çizgiler)
}

// noktalar arasındaki çizgileri çizelim. Her çizgi, iki noktasının çemberine kadar gelsin
def çizelim(hepsi: Vector[Çizgi]) {
    hepsi.foreach(çzg => {
        val (x1, y1) = (çzg.n1.x, çzg.n1.y)
        val (x2, y2) = (çzg.n2.x, çzg.n2.y)
        val boy = sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2))
        val (xr, yr) = (YÇ / boy * (x2 - x1), YÇ / boy * (y2 - y1))
        çzg.çizgi.erase
        çzg.çizgi = line(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
    })
}

// kırmızı kareye basınca yeni bir düğümle baştan başlarız
val b = square(-KS * 35, -KS * 35, 20)
b.setFillColor(red)
b.onMouseClick { (x, y) => serpiştir(noktalar) }
