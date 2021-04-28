//#include anaTanimlar
//#include eTahta
//#include araYuz
//#include alfabeta

çıktıyıSil
val odaSayısı = 8
val kimBaşlar = Siyah // Beyaz ya da Siyah başlayabilir. Seç :-)
val çeşni = 1
val tahta = new ETahta(odaSayısı, kimBaşlar, çeşni)
val bellek = new Bellek(tahta)
val araYüz = new Arayüz(tahta, bellek)

val düzey = Er
//özdevin(0.02) // bilgisayar çabucak bir oyunla başlayabilir istersen
ABa.ustalık(düzey)
/*
zamanTut(s"$odaSayısı x $odaSayısı ustalık: $düzey") {
    özdevinimliOyun(abArama, 0.1)
}("sürdü")
*/

