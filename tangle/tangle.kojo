import Staging._ // şu komutlar gelsin bakalım: line, circle, square
import Staging.{ circle, clear } // bu komut adları çatışıyor
import math.pow, math.random

val KNS = 2 // Karenin bir kenarında kaç tane nokta olsun? KNS arttıkça oyun zorlaşır.
val BNS = KNS * KNS // başlangıçtaki nokta sayısı. kare grid çizgesi kuracağız. düzlemseldir.
val YÇ = 20 // bu da noktanın yarıçapı
var çizgiler = Vector[Çizgi]() // boş küme olarak başlarız
var noktalar = Vector[Nokta]()
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
    val boy = 1.8 * canvasBounds.getMaxY // KNS * YÇ * 6 // serpme alanının kenar boyu
    hepsi.foreach(nkt => nkt.yeniKonum(boy * (random - 0.5), boy * (random - 0.5)))
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
    noktalar = (0 until BNS).foldLeft(Vector[Nokta]())((v, i) => { v :+ Nokta(0, 0) })
    // çizgileri tanımlar ve iki noktasına bağlarız. Bir balık ağı gibi. KNS * KNS düğümlü
    çizgiler = (0 until BNS).foldLeft(Vector[Çizgi]())(
        (çv, i) => {
            val (x, y) = (i / KNS, i % KNS)
            val çzg = if (y < KNS - 1) { çv :+ Çizgi(noktalar(i), noktalar(i + 1)) } else çv
            if (x < KNS - 1) { çzg :+ Çizgi(noktalar(i), noktalar(i + KNS)) } else çzg
        })
    println(kaçTane())
    serpiştir(noktalar) // rasgele dağıt ve çizgileri çiz
    kareDüğmeler
}
def kaçTane() = f"${noktalar.size}%2d nokta ve ${çizgiler.size}%2d çizgi var"
def kareDüğmeler() = {
    val (kx, ky) = (0.9 * canvasBounds.getMinX, 0.9 * canvasBounds.getMinY)
    val b = square(kx, ky - 40, 20)
    b.setFillColor(kırmızı) // kırmızı kareye tıkla: yeni bir düğümle baştan başlat
    b.onMouseClick { (x, y) => serpiştir(noktalar) }
    val b2 = square(kx, ky, 20) // mavi kare yeni bir nokta ekler
    b2.setPenColor(mavi)
    b2.setFillColor(mavi)
    var yeniNokta = 0 // kk'nin kaçıncı kümesine bağlanacak bu yeni nokta?
    val kk = kümeler(KNS)
    println(kk.size + " nokta ekleyebilirsin")
    b2.onMouseClick { (x, y) =>  // bu kareye her basışımızda yeni bir nokta ekleyelim
        if (yeniNokta < kk.size) {
            val yn = Nokta(kx + 40, ky + 40)
            noktalar = noktalar :+ yn
            çizgiler = çizgiler ++ (for (i <- kk(yeniNokta)) yield (Çizgi(yn, noktalar(i))))
            yeniNokta += 1
            println(kaçTane())
            çizelim(çizgiler)
        }
        else {
            b2.setPenThickness(4)
            b2.setPenColor(kırmızı)
            println("Başka nokta ekleyemiyoruz!")
        }
    }
}
def kümeler(d: Int) = {
    def iç4Kenar() = {  // ilk kare çizitin kenarları
        val r1 = Vector((0, 1), (d - 1, d), (d * d - 1, -1), (d * (d - 1), -d))
        val l0 = (for ((a, c) <- (for (i <- 0 to 3) yield r1(i))) yield (a, (a + c * d), c)).toList
        for ((a, b, c) <- l0) yield (Range(a, b, c).toList)
    }
    // dış kenarı iki listeyi fermuar gibi bir araya getirerek buluyoruz
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
clear()
toggleFullScreenCanvas()
baştan()
