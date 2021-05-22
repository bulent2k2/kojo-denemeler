//#include anaTanimlar
//#include eTahta
//#include araYuz
//#include alfabeta

val açıklama = <html> 
 <em> KojOtello'ya hoşgeldin! </em> Bu güzel oyun hakkında bilgi edinmek için
 <br/>
 internette "wiki reversi" diye arama yapabilirsin.
  </html>.toString

val ay_çeşniler = ay.Salındıraç("geleneksel", "bir çeşni")
val ay_tahta = ay.Salındıraç(8, 4, 5, 6, 7, 8, 9, 10, 11, 12)
val ay_tahta2 = ay.Salındıraç(7, 8, 9, 10, 11, 12) // todo
val ay_ilk = ay.Salındıraç("Siyah", "Beyaz")
val ay_düzey = ay.Salındıraç("Er", "Çırak", "Kalfa", "Usta", "Doktor", "Deha")
val ay_bilgisayar = ay.Salındıraç(
    "Oynamasın",
    "Siyahları oynasın",
    "Beyazları oynasın"
)

def menu: Birim = {
    silVeSakla
    çizMerkezde(büyüt(1.8) ->
        Resim.arayüz(
            ay.Sütun(
                ay.Sıra(ay.Tanıt(açıklama)),
                ay.Sıra(ay.Tanıt("Başlangıç taşları:"), ay_çeşniler),
                ay.Sıra(ay.Tanıt("Tahta:"), ay_tahta),
                ay.Sıra(ay.Tanıt("Kim başlar:"), ay_ilk),
                ay.Sıra(ay.Tanıt("Düzey:"), ay_düzey),
                ay.Sıra(ay.Tanıt("Bilgisayar:"), ay_bilgisayar),
                ay.Sıra(ay.Düğme("Oyna!") {
                    val odaSayısı = ay_tahta.value
                    val çeşni = ay_çeşniler.value match {
                        case "geleneksel" => 0
                        case _            => 1
                    }
                    var sorunYok = doğru
                    if (çeşni != 0) {
                        if (odaSayısı < 7) {
                            satıryaz("Çeşnili başlangıç taşları için 7x7 ya da daha iri bir tahta seç!")
                            sorunYok = yanlış
                        }
                    }
                    val kimBaşlar = ay_ilk.value match {
                        case "Siyah" => Siyah
                        case _       => Beyaz
                    }
                    val düzey = ay_düzey.value match {
                        case "Er"     => Er
                        case "Çırak"  => Çırak
                        case "Kalfa"  => Kalfa
                        case "Usta"   => Usta
                        case "Doktor" => Doktor
                        case "Deha"   => Deha
                        case _        => Kalfa
                    }
                    val bilgisayar = ay_bilgisayar.value match {
                        case "Siyahları oynasın" => Siyah
                        case "Beyazları oynasın" => Beyaz
                        case "Oynamasın"         => Yok
                        case _                   => Yok
                    }
                    if (sorunYok) {
                        val tahta = new ETahta(odaSayısı, kimBaşlar, çeşni)
                        val bellek = new Bellek(tahta)
                        çıktıyıSil
                        ABa.ustalık(düzey)
                        new Arayüz(tahta, bellek, bilgisayar)
                    }
                })
            )
        )
    )
}
menu
