silVeSakla()
dez yç = 10 // topumuzun yarıçapı
dez top = kalemRengi(kırmızı) -> Resim.daire(yç) // resmi
dez t2 = kalemRengi(mavi) -> Resim.daire(yç)
dez t3 = kalemRengi(yeşil) -> Resim.daire(yç)
çiz(top, t2, t3) // bu da tuvale çiziveriyor topu
den x = 0; den y = 0 // topun konumunu bu değişkenlerle belirleyeceğiz
den x2 = 200; den y2 = 0
den x3 = 0; den y3 = 100
den dx = 0; den dy = 0 // topun hızını da x ve y'deki değişikliklerle belirleyeceğiz
den dx2 = 0; den dy2 = 0
den dx3 = 0; den dy3 = 0
// Bu dx ve dy'i birazcık değiştirip tekrar çalıştır. Hemen anlarsın ne oluyor.
// Bize bir de döngü gerek. Bu komut çok becerikli:
canlandır { // İçindeki komutlar saniyede yaklaşık 40 kere yinelenir.
    top.kondur(x, y) // bu yöntem resimlerin yani burda bizim topun yerini değiştirir
    t2.kondur(x2, y2)
    t3.kondur(x3, y3)
    // topun konumunu yani x ve y'yi günceliyoruz:
    x += dx; y += dy
    x2 += dx2; y2 += dy2
    x3 += dx3; y3 += dy3
}

tanım tx(k: Kesir, o1: Kesir, o2: Kesir) = {
    dez r1 = mutlakDeğer(o1 - k)
    dez r2 = mutlakDeğer(o2 - k)
    eğer(o1 > k) {
        -1 * 1/(r1 * r1) + 1/(r2 * r2)
}