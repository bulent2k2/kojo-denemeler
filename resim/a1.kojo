// https://docs.kogics.net/concepts/turtle-picture-basics.html

silVeSakla() // tuvali tamamen sil ve kaplumbağacığı sakla

// Renk konusunda: Pek çok renk tanımlanmış durumda. Türkçe olarak:
//   açıkGri beyaz camgöbeği gri kahverengi koyuGri kırmızı mavi mor morumsu pembe
//   rastgeleRenk renksiz sarı siyah turuncu yeşil
// Ama daha çok var ingilizce adlarıyla. Örneğin renkler.gold
// renkler'den sonra nokta koyup arkasından CONTROL-BOŞLUK'la
// tamamlatarak hepsini görebilirsin...

// ilk resim bir altıgen olsun
val altıgen = Resim {
    kalemRenginiKur(morumsu)
    kalemKalınlığınıKur(6)
    boyamaRenginiKur(renkler.honeydew)
    yinele(6) {
        ileri(100)
        sağ(60)
    }
}

val çember = Resim {  // ikinci resim bir çember
    kalemRenginiKur(rastgeleRenk)
    kalemKalınlığınıKur(4)
    sağ(360, 50)
}

val altyazı = Resim { // üçüncü "resim" resmimizin alt yazısı olacak
    yazıBoyunuKur(30)
    kalemRenginiKur(renkler.darkRed)
    yazı("Resim Çizmek Ne Güzel!")
}

val aralık = Resim { // iki resim nesnesi arasında ufak bir boşluk olsun
    kalemRenginiKur(renksiz)
    yinele(4) {
        ileri(10)
        sağ(90)
    }
}

val ikisi = resimDüzenliDizi(altıgen, çember)
val hepsi = resimDikeyDüzenliDizi(altyazı, aralık, ikisi)

çizMerkezde(hepsi)


