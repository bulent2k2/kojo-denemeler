// Bu Scala'ya hızlı giriş kılavuzundaki hali.
// KS arttıkça oyun zorlaşır. Bir kenarda kaç tane nokta olsun?
dez KS = 4; dez AS = KS * KS
dez YÇ = 20 // bu da noktaların yarıçapı
durum sınıf Çizgi(n1: Nokta, n2: Nokta) { // her çizgi iki noktayı bağlar
    den çizgi = birDoğruÇiz(n1.x, n1.y, n2.x, n2.y) // bir doğru çizer
}
tanım birDoğruÇiz(llx: Kesir, lly: Kesir, urx: Kesir, ury: Kesir) = {
    dez (en, boy) = (urx - llx, ury - lly)
    dez r = götür(llx, lly) -> Resim.düz(en, boy)
    r.çiz
    r
}
// bütün çizgiler. boş küme olarak başlarız
den çizgiler = Yöney[Çizgi]()
// Noktayı tuvalde kaydıracağız. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
durum sınıf Nokta(den x: Kesir, den y: Kesir) {
    dez n = götür(x, y) * boyaRengi(mavi) -> Resim.daire(YÇ)
    n.çiz
    tanım yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        n.kondur(yeniX, yeniY)
    }
    // fareye tıklayıp çekince bu çalışacak
    n.fareyiSürükleyince { (mx, my) => { n.kondur(mx, my); x = mx; y = my; çizelim(çizgiler) } }
}
// Bütün noktaları (0,0) yani orijine üştüste koyalım. Merak etme birazdan dağıtacağız
silVeSakla()
dez noktalar = (0 |- AS).soldanKatla(Yöney[Nokta]())((v, i) => { v :+ Nokta(0, 0) })

// çizgileri tanımlar ve noktalara bağlarız. Bir balık ağı gibi. KS * KS düğümlü
çizgiler = (0 |- AS).soldanKatla(Yöney[Çizgi]())(
    (çv, i) => {
        dez (x, y) = (i / KS, i % KS)
        dez çzg = eğer (y < KS - 1) { çv :+ Çizgi(noktalar(i), noktalar(i + 1)) } yoksa çv
        eğer (x < KS - 1) { çzg :+ Çizgi(noktalar(i), noktalar(i + KS)) } yoksa çzg
    })
serpiştir(noktalar) // noktaları yerleştir ve çizgileri çiz

// noktaları rastgele yerleştir
tanım serpiştir(hepsi: Yöney[Nokta]) {
    hepsi.herbiriİçin(nkt => nkt.yeniKonum(KS * YÇ * 6 * (rasgele - 0.5), KS * YÇ * 6 * (rasgele - 0.5)))
    çizelim(çizgiler)
}

// noktalar arasındaki çizgileri çizelim. Her çizgi, iki noktasının çemberine kadar gelsin
tanım çizelim(hepsi: Yöney[Çizgi]) {
    hepsi.herbiriİçin(çzg => {
        dez (x1, y1) = (çzg.n1.x, çzg.n1.y)
        dez (x2, y2) = (çzg.n2.x, çzg.n2.y)
        dez boy = karekökü(karesi(x2 - x1) + karesi(y2 - y1))
        dez (xr, yr) = (YÇ / boy * (x2 - x1), YÇ / boy * (y2 - y1))
        çzg.çizgi.sil
        çzg.çizgi = birDoğruÇiz(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
    })
}

// kırmızı kareye basınca yeni bir düğümle baştan başlarız
tanım kare(x: Kesir, y: Kesir, en: Kesir) = {
    dez k = götür(x, y) * boyaRengi(kırmızı) -> Resim.dikdörtgen(en, en)
    k.çiz
    k
}
dez b = kare(-KS * 35, -KS * 35, 20)
b.fareyeTıklayınca { (x, y) => serpiştir(noktalar) }
