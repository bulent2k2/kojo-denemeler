silVeSakla()
çiz(götür(-200, -100) -> Resim.doğru(0, 200)) // Üç duvar çizelim. Bu dik ön duvar
çiz(götür(-200, -100) -> Resim.doğru(400, 0)) // Bu alt duvar
çiz(götür(-200, 100) -> Resim.doğru(400, 0)) // Bu da üst duvar
val rb = 80 // raketin boyu
val raket = kalemRengi(mavi) -> Resim.doğru(0, rb)
val top = kalemRengi(mavi) -> Resim.daire(5)
val skor = kalemRengi(siyah) * götür(-50, 150) -> Resim.yazı("Raketi fareyle yönet")
çiz(raket, top, skor)
var x = 0.0; var y = 0 // topun konumu
var dy = 8; var dx = -8.0 // topun hızı: d delta yani değişim ya da derivative yani türev demek
var rx = 0.0; var ry = 0.0 // raketin konumu
var ıska = 0 // top kaç kere kaçtı, saymak için
var vuruş = 0 // kaç kere raketle vurduğumuzu da sayalım
canlandır {
    // raketi fareyle kontrol ediyoruz -- ama raketin oyun alanını sınırlayalım
    val (fx, fy) = (fareKonumu.x, fareKonumu.y)
    rx = if (fx < -100) -100 else if (fx > 200) 200 else fx
    ry = if (fy < -100) -100 else if (fy > 20) 20 else fy
    raket.kondur(rx, ry)
    top.kondur(x, y) // topun yerini değiştirelim
    // top rakete çarpıyor mu?
    dx = if ((dx > 0) && (mutlakDeğer(rx - x) < 10) &&
        (y > ry) && (y < ry + rb)) { vuruş += 1; -1.1 * dx } else dx
    // topun konumunu güncelleyelim, duvarlara bakalım
    dx = if (x + dx < -200) -dx else dx // ön duvardan sekti mi?
    dy = if ((y + dy < -100) || (y + dy > 100)) -dy else dy // üst ve alt duvarlardan sekti mi?
    if (x + dx > 200) { x = -200; dx = 8; ıska += 1 } // ıskaladı
    x += dx; y += dy // topun gideceği noktayı hesaplayalım
    if (ıska > 0 || vuruş > 0) { // skoru güncelleyelim
        val mesaj = if (vuruş == 0) "Fareyi tuvale getir!" else s"$vuruş kere vurdun"
        skor.güncelle(s"$mesaj\n$ıska kere ıskaladın")
    }
}

oyunSüresiniGeriyeSayarakGöster(10, "Süre bitti", yeşil) // oyun 60 saniye sürsün