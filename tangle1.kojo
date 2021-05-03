import Staging._ // şu komutlar gelsin bakalım: line, circle, square
import Staging.{ circle, clear } // bu komut adları çatışıyor
import math.pow, math.random

val KS = 4; val AS = KS * KS // // KS arttıkça oyun zorlaşır. Bir kenarda kaç tane nokta olsun?
val YÇ = 20 // bu da noktanın yarıçapı
var çizgiler = Vector[Çizgi]() // boş küme olarak başlarız
var noktalar = Vector[Nokta]()
var yeniNokta = 0 // yeni noktanın KS tane komşusu olsun,  
case class Çizgi(n1: Nokta, n2: Nokta) { // her çizgi iki noktayı bağlar
    var çizgi = line(n1.x, n1.y, n2.x, n2.y) // bir doğru çizer
}
case class Nokta(var x: Kesir, var y: Kesir) {
    val n = circle(x, y, YÇ) // verilen x ve y coordinatı merkezimiz. yarıçap belli.
    n.setFillColor(mavi) // rengi mavi olsun
    def yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        n.setPosition(yeniX, yeniY)
    }
    // çekince bu çalışacak. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
    n.onMouseDrag { (mx, my) => { n.setPosition(mx, my); x = mx; y = my; çizelim(çizgiler) } }
}
def serpiştir(hepsi: Vector[Nokta]) { // her noktayı rasgele yerleştir
    val kenar = 1.8 * canvasBounds.getMaxY // KS * YÇ * 6 // serpme alanının kenarı
    hepsi.foreach(nkt => nkt.yeniKonum(kenar*(random-0.5), kenar*(random-0.5)))
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
def baştan() = { // Her nokta (0,0) yani orijine konuyor başta. Merak etme birazdan dağıtacağız
    noktalar = (0 until AS).foldLeft(Vector[Nokta]())((v, i) => { v :+ Nokta(0, 0) })
    // çizgileri tanımlar ve iki noktasına bağlarız. Bir balık ağı gibi. KS * KS düğümlü
    çizgiler = (0 until AS).foldLeft(Vector[Çizgi]())(
        (çv, i) => {
            val (x, y) = (i / KS, i % KS)
            val çzg = if (y < KS - 1) { çv :+ Çizgi(noktalar(i), noktalar(i + 1)) } else çv
            if (x < KS - 1) { çzg :+ Çizgi(noktalar(i), noktalar(i + KS)) } else çzg
        })
    println(kaçTane())
    serpiştir(noktalar) // rasgele dağıt ve çizgileri çiz
    kareDüğmeler
}
def kaçTane() = f"${noktalar.size}%2d nokta ve ${çizgiler.size}%2d çizgi"
def kareDüğmeler() = {
    val (kx, ky) = (0.9 * canvasBounds.getMinX, 0.9 * canvasBounds.getMinY)
    val b = square(kx, ky - 40, 20)
    b.setFillColor(kırmızı) // kırmızı kareye tıkla: yeni bir düğümle baştan başlat
    b.onMouseClick { (x, y) => serpiştir(noktalar) }
    val b2 = square(kx, ky, 20) // mavi kare yeni bir nokta ekler
    b2.setPenColor(mavi)
    b2.setFillColor(mavi)
    b2.onMouseClick { (x, y) =>
        if (yeniNokta + KS <= noktalar.size) {
            val yn = Nokta(kx + 40, ky + 40)
            noktalar = noktalar :+ yn
            // bir kenardaki her noktaya bağlayalım
            çizgiler = çizgiler ++ (for (i <- yeniNokta until yeniNokta + KS) yield (Çizgi(yn, noktalar(i))))
            yeniNokta += KS
            println(kaçTane())
            çizelim(çizgiler)
        }
        else println("Başka nokta eklemesek iyi olur!")
    }
}
clear()
toggleFullScreenCanvas()
baştan()