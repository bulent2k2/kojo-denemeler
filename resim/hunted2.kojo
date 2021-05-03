// https://kojoenv.wordpress.com/ =>
// https://docs.kogics.net/tutorials/lessons-level2/hunted8-export.html#published-game

silVeSakla()
rastgeleTohumunuKur(34)

val adres = "https://kojofiles.netlify.app/hunted7"
resimİndir(s"$adres/player_run_sheet.png")
resimİndir(s"$adres/robot_walk_sheet.png")
müzikİndir(s"$adres/Cave.mp3")
müzikİndir(s"$adres/DrumBeats.mp3")
resimİndir(s"$adres/bg-patch.jpg")

var tümEkran = yanlış

def tuvaliResimleDöşe(resimDosyası: Yazı, en: Sayı, boy: Sayı) {
    val ta = tuvalAlanı
    val sayıYatay = ta.en.toInt / en + 1
    val sayıDikey = ta.boy.toInt / boy + 1
    yineleİçin(0 until sayıYatay) { gx =>
        yineleİçin(0 until sayıDikey) { gy =>
            val res = Resim.imge(resimDosyası)
            res.konumuKur(ta.x + gx * en, ta.y + gy * boy)
            çiz(res)
        }
    }
}

class Oyun {
    çizSahne(renkler.hsl(120, 1.00, 0.05))
    val ta = tuvalAlanı

    val ölçek = if (ta.en < 500 || ta.boy < 500) 0.5 else 1.0

    müzikMp3üÇalDöngülü(s"$adres/Cave.mp3")

    if (tümEkran) {
        tuvaliResimleDöşe(s"$adres/bg-patch.jpg", 128, 128)
    }

    def dosyadanResimler(imgeler: ArrayBuffer[ResimDosyası], env: Resim) = {
        val res = ArrayBuffer.empty[Resim]
        yineleİçin(imgeler) { img =>
            res.append(Resim.imge(img, env))
        }
        resimKümesi(res)
    }

    // spritesheet bir sayfa dolusu resim demek. 
    // Bu metod resimlerin yatay sıralanmış olduğunu varsayar ve o sayede çalışır
    def birSayfaResimler(dosyaAdı: Yazı, resimSayısı: Sayı, resminEni: Sayı, resminBoyu: Sayı): ArrayBuffer[ResimDosyası] = {
        val sayfa = SpriteSheet(dosyaAdı, resminEni, resminBoyu)
        val imgeler = ArrayBuffer.empty[ResimDosyası]
        yineleİçin(rangeTill(0, resimSayısı)) { n =>
            imgeler.append(sayfa.imageAt(n, 0))
        }
        imgeler
    }

    val oyuncuResimleri = birSayfaResimler(s"$adres/player_run_sheet.png", 3, 96, 128)

    val oyuncuZarfı = Resim {
        sağ(90)
        zıpla(30)
        sol(90 + 40)
        ileri(45)
        sağ(90)
        ileri(40)
        sol(55)
        ileri(30)
        sağ(75)
        ileri(55)
        sağ(90)
        ileri(23)
        sağ(47)
        ileri(22)
        sol(70)
        ileri(5)
        sağ(30)
        ileri(40)
        sağ(45)
        ileri(30)
        sağ(60)
        ileri(47)
    }
    çizVeSakla(oyuncuZarfı)

    val oyuncu = dosyadanResimler(oyuncuResimleri, oyuncuZarfı)
    oyuncu.konumuKur(ta.x + ta.en / 2, ta.y + ta.boy / 2 - 100)
    oyuncu.büyüt(ölçek)
    çiz(oyuncu)

    val avcılar = ArrayBuffer.empty[Resim]
    val avcıHızı = HashMap.empty[Resim, Vector2D]

    val avcıZarfı = Resim {
        sağ(90)
        zıpla(25)
        sol(90 + 45)
        ileri(35)
        sağ(90)
        ileri(35)
        sol(90)
        ileri(10)
        sağ(70)
        ileri(38)
        sağ(65)
        ileri(30)
        sağ(55)
        ileri(25)
        sağ(30)
        ileri(50)
        sağ(30)
        ileri(23)
        sağ(65)
        ileri(48)
    }
    çizVeSakla(avcıZarfı)

    val resimDosyaları = birSayfaResimler(s"$adres/robot_walk_sheet.png", 8, 96, 128)

    yineleİçin(1 to avcıSayısı) { n =>
        val res = dosyadanResimler(resimDosyaları, avcıZarfı)
        res.konumuKur(ta.x + ta.en / (avcıSayısı + 2) * n, ta.y + 100 + rastgeleKesir(0, ta.boy - 200))
        res.büyüt(ölçek)
        avcılar.append(res) // ekle demek
        val hv = Vector2D(rastgele(1, 4), rastgele(1, 4))
        avcıHızı(res) = hv
        çiz(res)
    }

    def oyunBitti(avcıyaYakalandıMı: İkil) {
        durdur()
        çizMerkezdeYazı(
            if (avcıyaYakalandıMı) "Yakalandın!" else "Duvara çarptın!",
            kırmızı, 30
        )
    }

    val jsYÇ = 70 * ölçek
    val js = joystick(jsYÇ) // todo
    val aralık = if (ta.en < ta.boy) ta.en / 10 else ta.boy / 10
    js.setPosition(ta.x + ta.en - jsYÇ - aralık, ta.y + jsYÇ + aralık)
    js.setPerimeterColor(renkler.rgb(120, 120, 120).fadeOut(0.2))
    js.setPerimeterPenColor(siyah)
    js.setControlColor(beyaz.fadeOut(0.2))
    js.draw()

    val hız = 5
    canlandır {
        yineleİçin(avcılar) { avcı =>
            var aHızı = avcıHızı(avcı)
            avcı.götür(aHızı)
            avcı.sonrakiniGöster(200)

            if (avcı.çarptıMı(Resim.tuvalSınırları)) {
                aHızı = sahneKenarındanYansıtma(avcı, aHızı)
                avcıHızı(avcı) = aHızı
            }

            if (avcı.çarptıMı(oyuncu)) {
                oyunBitti(doğru)
            }

            yineleİçin(avcılar) { avcı2 =>
                if (avcı.çarptıMı(avcı2)) {
                    aHızı = engeldenYansıtma(avcı, aHızı, avcı2)
                    avcıHızı(avcı) = aHızı
                }
            }
        }

        if (tuşaBasılıMı(tuşlar.VK_RIGHT)) {
            oyuncu.götür(hız, 0)
            oyuncu.sonrakiniGöster(200)
        }
        if (tuşaBasılıMı(tuşlar.VK_LEFT)) {
            oyuncu.götür(-hız, 0)
            oyuncu.sonrakiniGöster(200)
        }
        if (tuşaBasılıMı(tuşlar.VK_UP)) {
            oyuncu.götür(0, hız)
            oyuncu.sonrakiniGöster(200)
        }
        if (tuşaBasılıMı(tuşlar.VK_DOWN)) {
            oyuncu.götür(0, -hız)
            oyuncu.sonrakiniGöster(200)
        }
        else {
            js.movePlayer(oyuncu.p, 0.2 * ölçek)
        }
        if (oyuncu.uzaklık(Resim.tuvalSınırları) < 50) {
            if (!müzikMp3üÇalıyorMu) {
                müzikMp3üÇal(s"$adres/DrumBeats.mp3")
            }
        }
        else {
            müzikMp3üKapat()
        }

        if (oyuncu.çarptıMı(Resim.tuvalSınırları)) {
            oyunBitti(yanlış)
        }
    }
    oyunSüresiniGöster(60, "Tebrikler", beyaz, 15)
    ekranTazelemeHızınıGöster(beyaz, 15)
    tuvaliEtkinleştir()
}

val oyununAmacı = kalemRengi(siyah) -> Resim.yazı("Bir dakika boyunca avcılardan kaçabilecek misin?\nKenarlara da çok yaklaşma sakın!", 20)

val başlamaTuşu = boyaRengi(renkler.hsl(0, 1.00, 0.70)) -> Resim.dikdörtgen(100, 100)
val msg = kalemRengi(black) -> Resim.yazı("Başla", 20)
val res1 = resimDikeyDüzenliDizi(msg, Resim.dikeyBoşluk(10), başlamaTuşu)

val başlamaTuşu2 = boyaRengi(renkler.hsl(0, 1.00, 0.30)) -> Resim.dikdörtgen(100, 100)
val msg2 = kalemRengi(black) -> Resim.yazı("Tüm ekran başla", 20)
val res2 = resimDikeyDüzenliDizi(msg2, Resim.dikeyBoşluk(10), başlamaTuşu2)

val res = resimDikeyDüzenliDizi(
    resimYatayDüzenliDizi(res1, Resim.yatayBoşluk(100), res2),
    Resim.dikeyBoşluk(50),
    oyununAmacı
)

var avcıSayısı = 3
    
çizMerkezde(res)
res1.fareyeTıklayınca { (x, y) =>
    res.sil()
    sırayaSok(1.0) {
        new Oyun
    }
}

res2.fareyeTıklayınca { (x, y) =>
    res.sil()
    tümEkranTuval()
    tümEkran = doğru
    avcıSayısı = 10
    sırayaSok(1.0) {
        new Oyun
    }
}
