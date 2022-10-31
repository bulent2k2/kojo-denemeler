silVeSakla
val yç = 100 // saatin yarıçapı. Büyültmek ister misin?
val pi2 = 2.0 * piSayısı // 2*Pi radyan tam 360 derece dönüş demek
// saati çizelim
def saat = {
    Resim.sil() // eski saati silelim
    çiz(kalemRengi(kırmızı) -> Resim.daire(yç))
    for (i <- 0 |-| 59) { // // dakika ve saat çentikleri
        val ra = pi2 * i / 60
        val (x, y) = (yç * sinüs(ra), yç * kosinüs(ra))
        val çentikBoyu = if (i % 5 == 0) 0.9 else 0.95
        val (llx, lly, urx, ury) = (çentikBoyu * x, çentikBoyu * y, x, y)
        val (en, boy) = (urx - llx, ury - lly)
        çiz(kalemRengi(kırmızı) * götür(llx, lly) -> Resim.doğru(en, boy))
    }
}

canlandır { // bu döngü her saniyede yaklaşık 40 kere yinelenir
    var buan = BuAn()
    saat; çiz(kalemRengi(siyah) * götür(-yç - 5, -yç - 20) -> Resim.yazı(buan.yazıya))
    val s = pi2 * buan.saniye / 60 // saniyeKolu
    çiz(kalemRengi(mavi) -> Resim.doğru(0.9 * yç * sinüs(s), 0.9 * yç * kosinüs(s)))
    val m = pi2 * buan.dakika / 60 // dakika kolu
    çiz(kalemRengi(yeşil) -> Resim.doğru(0.8 * yç * sinüs(m), 0.8 * yç * kosinüs(m)))
    val h = pi2 * buan.saat / 12 + m / 12 // saat kolu
    çiz(kalemRengi(turuncu) -> Resim.doğru(0.6 * yç * sinüs(h), 0.6 * yç * kosinüs(h)))
}
