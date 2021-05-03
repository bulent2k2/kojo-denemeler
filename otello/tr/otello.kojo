//#include anaTanimlar
//#include eTahta
//#include araYuz
//#include alfabeta

çıktıyıSil
val çeşni = 0 // ya da 1
val odaSayısı = 8
val kimBaşlar = Siyah // Beyaz ya da Siyah başlayabilir. Seç :-)
val bilgisayar = Siyah // Siyah ya da Beyaz oynar ya da Yok (yani oynamaz)

val tahta = new ETahta(odaSayısı, kimBaşlar, çeşni)
val bellek = new Bellek(tahta)
val araYüz = new Arayüz(tahta, bellek, bilgisayar)

val düzey = Usta
//özdevin(0.02) // bilgisayar çabucak bir oyunla başlayabilir istersen
ABa.ustalık(düzey)
/*
zamanTut(s"$odaSayısı x $odaSayısı ustalık: $düzey") {
    özdevinimliOyun(abArama, 0.1)
}("sürdü")
*/
