sil()
//hızıKur(hızlı)
canlandırmaHızınıKur(1)
kalemRenginiKur(gri)
// Biraz şeffaf bır sarıyla başlayalım. Sarı = Kırmızı + Yeşil (Mavi yok)
// Renk işlevi dört sayı girdisi alır:
// kırmızı, yeşil, mavi, saydamlık.
// Hepsi de 0..255 arasında olmalılar.
var renk = Renk(255, 255, 0, 127).fadeOut(0.4)
val öbekSayısı = 20 // 8 öbek çizelim. Bu sayıyı değiştirmeyi dene!
yaklaşXY(5.0/öbekSayısı, 5.0/öbekSayısı, öbekSayısı * 30, öbekSayısı * 10)
yinele(öbekSayısı) {
    val kaçTane = 50 // 18 tane beşgen çizelim. Bu sayıyı değiştirmeyi dene!
    yinele(kaçTane) {
        boyamaRenginiKur(renk)
        yinele(5) {
            ileri(100)
            sağ(72)
        }
        val birSonraki = 360.0 / kaçTane // bir sonraki açı ve renk için
        renk = renk.çevir(birSonraki)
        sağ(birSonraki)
    }
    ileri(200)
    sağ(360 / öbekSayısı)
}
