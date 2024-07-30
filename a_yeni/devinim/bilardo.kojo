silVeSakla; gridiGöster; eksenleriGöster
dez yç = 10 // topumuzun yarıçapı
dez top = Resim.daire(yç) // topun resmi
top.çiz // bu da tuvale çiziveriyor topu
// bilardo masasını tanımlayıp çizelim
dez en = 400; dez boy = 200 // çizmesek de çalışır ama görmek faydalı:
çiz(kalemRengi(siyah) * götür(-en / 2, -boy / 2) -> Resim.dikdörtgen(en, boy))
// topun şu ufak ve iri sayılar arasında kalmasını istiyoruz
dez (ufakX, iriX) = (-en/2 + yç, en/2 - yç)
dez (ufakY, iriY) = (-boy/2 + yç, boy/2 - yç)
den x = 0; den y = 0 // topun konumunu bu değişkenlerle belirleyeceğiz
den dx = 2; den dy = 4 // topun hızını da x ve y'deki değişikliklerle belirleyeceğiz
// Bu dx ve dy'i birazcık değiştirip tekrar çalıştır. Hemen anlarsın ne oluyor.
// Bize bir de döngü gerek. Bu komut çok becerikli:
canlandır { // İçindeki komutlar saniyede yaklaşık 40 kere yinelenir.
    top.kondur(x, y) // bu yöntem bizim topun yerini değiştirir
    // Zıplama alanının dışına çıkmasın diye sınıra gelince
    // hızının doğrultusunu tersine çeviriyoruz:
    dx = eğer (x <= ufakX || x >= iriX) -dx yoksa dx
    dy = eğer (y <= ufakY || y >= iriY) -dy yoksa dy
    // topun konumunu yani x ve y'yi günceliyoruz
    x += dx
    y += dy
}
