// iki yöntemden birini seçelim
trait Seçim
case object İlkAçıkYol extends Seçim
case object EnAçıkYol extends Seçim

val seçim: Seçim = İlkAçıkYol    // robot ilk bulunan açık yolu seçsin
//val seçim: Seçim = EnAçıkYol   // robot en açık yolu bulsun ve seçsin

/// robot.kojo
case class Robot(x0: Sayı, y: Sayı, duvarlar: Resim) {
    val renk = renkler.darkMagenta
    val en = 20
    val boy = 30
    val x = x0 + (50 - en) / 2
    val uzaklıkAlgıcısı = götür(en / 2, boy) * boyaRengi(renk.lighten(0.5)) *
        kalemRengi(renk) -> Resim.daire(5)
    val sesDalgası = götür(en / 2, boy) * boyaRengi(ColorMaker.lightBlue) *
        kalemRengi(ColorMaker.lightSlateGray) * kalemBoyu(1) -> Resim.daire(en / 2)
    val gövde = boyaRengi(renk) * kalemRengi(renk) -> Resim.dikdörtgen(en, boy)
    val hız = 200.0 // saniyede kaç nokta hızla ilerleyelim
    val dönüşHızı = 180.0 // saniyede kaç açı dönelim
    uzaklıkAlgıcısı.eksenleriGöster()

    gövde.konumuKur(x, y)
    uzaklıkAlgıcısı.konumuKur(x, y)
    sesDalgası.konumuKur(x, y)

    def engeleUzaklık = {
        sesDalgası.göster()
        var u = 0
        while (!sesDalgası.çarptıMı(duvarlar)) {
            sesDalgası.götür(0, 3)
            u += 3
        }
        sesDalgası.götür(0, -u)
        sesDalgası.gizle()
        u
    }

    def göster() {
        çiz(gövde, uzaklıkAlgıcısı, sesDalgası)
        sesDalgası.gizle()
    }

    def ileri(ms: Kesir) {
        // ms milisaniye süresince ileri git
        val u = hız * ms / 1000
        val adımSayısı = 10
        val adımUzunluğu = u / adımSayısı
        yinele(adımSayısı) {
            gövde.götür(0, adımUzunluğu)
            uzaklıkAlgıcısı.götür(0, adımUzunluğu)
            sesDalgası.götür(0, adımUzunluğu)
            durakla(ms / adımSayısı / 1000)
        }
    }

    def sol(ms: Kesir) {
        val angle = dönüşHızı * ms / 1000
        val adımSayısı = 30
        val adımAçısı = angle / adımSayısı
        yinele(adımSayısı) {
            gövde.döndürMerkezli(adımAçısı, en / 2, boy / 2)
            uzaklıkAlgıcısı.döndürMerkezli(adımAçısı, 0, -boy / 2)
            sesDalgası.döndürMerkezli(adımAçısı, 0, -boy / 2)
            durakla(ms / adımSayısı / 1000)
        }
    }

    def sağ(ms: Kesir) {
        val angle = dönüşHızı * ms / 1000
        val adımSayısı = 30
        val adımAçısı = angle / adımSayısı
        yinele(adımSayısı) {
            gövde.döndürMerkezli(-adımAçısı, en / 2, boy / 2)
            uzaklıkAlgıcısı.döndürMerkezli(-adımAçısı, 0, -boy / 2)
            sesDalgası.döndürMerkezli(-adımAçısı, 0, -boy / 2)
            durakla(ms / adımSayısı / 1000)
        }
    }

    def çarptıMı(other: Resim) = {
        gövde.çarptıMı(other) || uzaklıkAlgıcısı.çarptıMı(other)
    }
}
/// robot.kojo

/// environment1.kojo
def duvar(en: Kesir, boy: Kesir) = {
    val renk = renkler.darkOliveGreen
    boyaRengi(renk) * kalemRengi(renk) -> Resim.dikdörtgen(en, boy)
}
val duvarBatı = duvar(50, 500)
val duvarKuzey = duvar(800, 50)
val duvarDoğu = duvar(50, 500)
val duvarGüney = duvar(800, 50)
val duvar1 = duvar(50, 150)
val duvar1a = duvar(50, 150)
val duvar2 = duvar(200, 100)
val duvar3 = duvar(100, 200)
val duvar4 = duvar(200, 50)
val duvar4a = duvar(200, 50)
val duvar5 = duvar(50, 400)
val duvar6 = duvar(100, 150)
val duvar6a = duvar(100, 150)
val duvar7 = duvar(250, 100)
val duvar8 = duvar(25, 50)

duvarBatı.konumuKur(-450, -250)
duvarKuzey.konumuKur(-400, 250)
duvarDoğu.konumuKur(400, -250)
duvarGüney.konumuKur(-400, -300)
duvar1.konumuKur(-350, -250)
duvar1a.konumuKur(-350, 100)
duvar2.konumuKur(-400, -50)
duvar3.konumuKur(-150, -100)
duvar4.konumuKur(-250, -200)
duvar4a.konumuKur(-250, 150)
duvar5.konumuKur(50, -200)
duvar6.konumuKur(150, 100)
duvar6a.konumuKur(150, -250)
duvar7.konumuKur(100, -50)
duvar8.konumuKur(-75, -250)

val duvarlar = Resim.dizi(duvarBatı, duvarKuzey, duvarDoğu, duvarGüney, duvar1, duvar2,
    duvar3, duvar4, duvar1a, duvar4a, duvar5, duvar6, duvar6a, duvar7, duvar8)
/// environment1.kojo

// duvara gelince rastgele sağa ya da sola baksın ve ilk bulduğu açık yolda ilerlesin
def döngü1() {
    var u = robot.engeleUzaklık

    // Önümüzdeki engel uzaktaysa ( uzaklık >= 6 ) ona doğru gidelim.
    // Yoksa, yani önümüzdeki engel yakınsa, durup rastgele sağa ya da sola dönelim.
    // Yeni yöne göre önümüzdeki engel yeterince uzaksa ( uzaklık > 20 ), o yönde ilerleriz...
    // Yoksa aynı yönde dönmeye devam ederiz..

    if (u >= 6) {
        robot.ileri(50)
    }
    else {
        val sağaDönelimMi = rastgeleİkil
        repeatUntil(u > 20) {
            if (sağaDönelimMi) {
                robot.sağ(100)
            }
            else {
                robot.sol(100)
            }
            u = robot.engeleUzaklık
        }
    }
}

// duvara gelince en açık yolu bularak ilerlesin
def döngü2() {
    val u = robot.engeleUzaklık

    // bir engele yaklaşınca (ya da duvara çarpınca), önümüzü soldan sağa doğru tarayalım.
    // En açık yolu bulalım ve onda ilerleyelim.

    if (u > 6 && !robot.çarptıMı(duvarlar)) {
        robot.ileri(5000 / robot.hız)
    }
    else {
        // döne döne en açık yolu, yani engelin en uzakta olduğu açıyı bulalım
        var enİriUzaklık = 0.0 
        var yeniYöneDönüşSüresi = 0.0 // ona dönmek için geçen süreyi anımsayalım
        val dönüşAçısı = 90
        val toplamDönüşSüresi = 1000 * dönüşAçısı / robot.dönüşHızı
        val adımSayısı = 10
        val dönüşSüresi = toplamDönüşSüresi / adımSayısı
        var sağaDönelimMi = yanlış
        yineleİçin(1 to adımSayısı) { sayı =>
            robot.sol(dönüşSüresi)
            val u = robot.engeleUzaklık
            if (u > enİriUzaklık) {
                enİriUzaklık = u
                yeniYöneDönüşSüresi = sayı * dönüşSüresi
            }
        }

        robot.sağ(toplamDönüşSüresi)

        yineleİçin(1 to adımSayısı) { sayı =>
            robot.sağ(dönüşSüresi)
            val u = robot.engeleUzaklık
            if (u > enİriUzaklık) {
                sağaDönelimMi = doğru
                enİriUzaklık = u
                yeniYöneDönüşSüresi = sayı * dönüşSüresi
            }
        }
        if (sağaDönelimMi) {
            robot.sol(toplamDönüşSüresi - yeniYöneDönüşSüresi)
        }
        else {
            robot.sol(toplamDönüşSüresi)
            robot.sol(yeniYöneDönüşSüresi)
        }
        val fd = enUfağı(40, enİriUzaklık)
        robot.ileri(1000 * fd / robot.hız)
    }
}

/* todo
    /robosim/tr/robot.kojo
    /robosim/tr/environment1.kojo
*/

// Yüklediğimiz yazılımcıkları şuradan da okuyabilirsin:
//   https://github.com/litan/kojo/tree/master/src/main/resources/robosim/tr

silVeSakla()
//yaklaşmayaİzinVerme()

// Benzeşme alanı benim bilgisayarımda tuvale sığmadı. 
// Onun için biraz uzaklaşmakta fayda var:
zoom(0.6)
// ya da tüm ekran tuvale geç:
// tümEkran()

artalanıKur(renkler.khaki)
çiz(duvarlar) // duvarlar environment1.kojo adlı yazılımcık içinde tanımlanıyor

// duvarları biraz oynatmak ya da yeniden çizmek istersen, eksenleri ve gridi aç:
eksenleriGöster()
gridiGöster()

val robot = Robot(-400, -240, duvarlar)
robot.göster()

yineleDoğruysa(doğru) { // yani hep tekrar edecek...
    seçim match {
        case İlkAçıkYol => döngü1()
        case EnAçıkYol  => döngü2()
        case _       =>
    }
}
