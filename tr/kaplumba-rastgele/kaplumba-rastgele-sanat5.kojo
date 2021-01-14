sil()
zoomXY(1, 0.8, -150, 50)
hızıKur(hızlı)
kalemKalınlığınıKur(4)
val yaprakSayısı = 12
var boya = rastgeleRenk
yinele(yaprakSayısı) {
    boyamaRenginiKur(boya)
    kalemRenginiKur(boya)
    yinele(4) {
        ileri()
        sağ()
        ileri()
        ileri()
        sağ()
        ileri()
        ileri()
        ileri()
        ileri()
        sol()
    }
    boya = hueMod(boya, 0.05)
    ileri(80)
    sol(360 / yaprakSayısı)
}
görünmez()
