silVeSakla
canlandırmaHızınıKur(100)
den renk = sarı
dez tane = 60
dez açı = 360 / tane
yaklaş(10.0 / tane, 600, 20)
den xİri = 0.0 // xUfak = 0
dez boyu = (tane * 0.8).sayıya
den sayı = 0
yinele(tane) {
    zıpla(tane)
    kalemRenginiKur(renk)
    renk = renk.çevir(açı)
    nokta(boyu); sayı += 1
    sağ(açı)
    eğer (konum.x > xİri) xİri = konum.x
}
satıryaz(s"$sayı nokta çizdik")
sayı = 0
yinele(tane * tane) {
    kalemRenginiKur(rastgeleRenk)
    dez x = rastgeleKesir(0, xİri)
    dez y = rastgeleKesir(-xİri/6 + boyu/2, xİri/6 + boyu/2)
    konumuKur(x, y)
    nokta(boyu); sayı += 1
}
yinele(tane * tane) {
    kalemRenginiKur(rastgeleRenk)
    dez x = rastgeleKesir(2*xİri/6, 4*xİri/6)
    dez y = rastgeleKesir(-xİri/2 + boyu/2, xİri/2 + boyu/2)
    konumuKur(x, y)
    nokta(boyu); sayı += 1
}
satıryaz(s"$sayı nokta daha çiziverdik")