silVeSakla
canlandırmaHızınıKur(100)
den renk = sarı
dez tane = 30
dez açı = 360 / tane
yinele(tane) {
    zıpla(tane)
    kalemRenginiKur(renk)
    renk = renk.çevir(açı)
    nokta((tane * 0.8).sayıya)
    sağ(açı)
}
