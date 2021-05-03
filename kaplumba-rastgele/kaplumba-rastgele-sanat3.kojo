sil()
hızıKur(hızlı)
kalemKalınlığınıKur(4)
val yaprakSayısı = 12
var boya = yellow
yinele(yaprakSayısı) {
    boyamaRenginiKur(boya)
    yinele(4) {
        ileri()
        sağ()
        ileri()
        boya = hueMod(boya, 0.2)
        kalemRenginiKur(boya)
        ileri()
        sağ()
        ileri()
        ileri()
        ileri()
        ileri()
        sol()
    }
    ileri(80)
    sol(360 / yaprakSayısı)
}
görünmez()
