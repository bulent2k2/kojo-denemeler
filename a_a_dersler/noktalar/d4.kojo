silVeSakla
canlandırmaHızınıKur(10)
den renk = sarı
dez tane = 60
dez açı = 360 / tane
yaklaş(10.0 / tane)
yinele(tane) {
    zıpla(tane)
    kalemRenginiKur(renk)
    renk = renk.çevir(açı)
    nokta((tane * 0.8).sayıya)
    sağ(açı)
}
