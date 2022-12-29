silVeSakla
canlandırmaHızınıKur(10)
den renk = sarı
dez tane = 60
dez açı = 360 / tane
yaklaş(10.0 / tane)
den xİri = 0.0 // xUfak = 0
dez boyu = (tane * 0.8).sayıya
yinele(tane) {
    zıpla(tane)
    kalemRenginiKur(renk)
    renk = renk.çevir(açı)
    nokta(boyu)
    sağ(açı)
    eğer (konum.x > xİri) xİri = konum.x
}
yinele(tane * tane) {
    kalemRenginiKur(rastgeleRenk)
    dez x = rastgeleKesir(0, xİri)
    dez y = rastgeleKesir(-xİri/6 + boyu/2, xİri/6 + boyu/2)
    konumuKur(x, y)
    nokta(boyu)
}
yinele(tane * tane) {
    kalemRenginiKur(rastgeleRenk)
    dez x = rastgeleKesir(2*xİri/6, 4*xİri/6)
    dez y = rastgeleKesir(-xİri/2 + boyu/2, xİri/2 + boyu/2)
    konumuKur(x, y)
    nokta(boyu)
}