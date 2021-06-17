kojoVarsayılanİkinciBakışaçısınıKur()
çıktıyıSil()
silVeSakla()
yaklaşmayaİzinVerme()

satıryaz("Oyunu kazanmak için kırmızı yuvarlağı bul ve al. Sonra da başa dönüp yeşil yuvarlağı al.")
satıryaz(s"\nKendi mağaranı kurmak için, '$kurulumDizini/examples/tiledgame/level1.tmx' dosyasını www.mapeditor.org sitesindeki Tiled (çini) düzenleyicisiyle değiştirebilirsin.")
satıryaz("Kendi mağaranı ikinci düzey olarak (level2.tmx) yazılımcığa ekleyebilir misin?")
scroll(-tuvalAlanı.x, tuvalAlanı.y)  // todo: tuvaliKaydır
artalanıKur(renkler.hsl(189, 0.03, 0.45))

// çini dünyasının düzeyleri var. Hangi düzeydeki çinilerle çarpışma olacak belirtelim:
val çarpışmaDüzeyi = 1

class Oyuncu(çiniX: Sayı, çiniY: Sayı, dünya: ÇiniDünyası) {
    val oyuncununKonumu = dünya.çinidenKojoya(ÇiniXY(çiniX, çiniY))
    val sayfa = BirSayfaKostüm("player.png", 30, 42)

    // oyuncunun kostüm resimlerinin boyutları 30x40 (nokta kare)
    // biraz küçültelim ki 24x24 boyutlarındaki çinilere sığsın
    def oyuncununResmi(img: İmge) = büyüt(0.8) -> Resim.imge(img)

    val sağaDönükDuruş = Resim.küme(oyuncununResmi(sayfa.resimSeç(0, 0)))
    val solaDönükDuruş = Resim.küme(oyuncununResmi(sayfa.resimSeç(0, 1)))

    val sağaKoşu = Resim.küme(Dizin(
        sayfa.resimSeç(0, 2),
        sayfa.resimSeç(1, 2),
        sayfa.resimSeç(2, 2),
        sayfa.resimSeç(3, 2),
        sayfa.resimSeç(4, 2)
    ).map(oyuncununResmi))

    val solaKoşu = Resim.küme(Dizin(
        sayfa.resimSeç(0, 3),
        sayfa.resimSeç(1, 3),
        sayfa.resimSeç(2, 3),
        sayfa.resimSeç(3, 3),
        sayfa.resimSeç(4, 3)
    ).map(oyuncununResmi))

    val sağaZıplayış = Resim.küme(Dizin(
        sayfa.resimSeç(0, 0),
        sayfa.resimSeç(1, 0),
        sayfa.resimSeç(2, 0),
        sayfa.resimSeç(3, 0)
    ).map(oyuncununResmi))

    val solaZıplayış = Resim.küme(Dizin(
        sayfa.resimSeç(0, 1),
        sayfa.resimSeç(1, 1),
        sayfa.resimSeç(2, 1),
        sayfa.resimSeç(3, 1)
    ).map(oyuncununResmi))

    var ancılResim = sağaDönükDuruş  // bu andaki resim
    ancılResim.konumuKur(oyuncununKonumu)

    var sağaMıBakıyor = doğru
    val yerçekimi = -0.1
    val yatayHız = 3.0
    var dikeyHız = -1.0
    var zıplıyorMu = yanlış

    def birAdımİleri() {
        çarpışmalarBirAdımİleri()
        hedefYuvarlaklarBirAdımİleri()
    }

    var hedefEtkinMi = yanlış
    def hedefYuvarlaklarBirAdımİleri() {
        if (ancılResim.çarptıMı(ilkHedef)) {
            ilkHedef.sil()
            hedef.saydamlığıKur(1)
            hedefEtkinMi = doğru
        }
        if (hedefEtkinMi) {
            if (ancılResim.çarptıMı(hedef)) {
                hedef.sil()
                durdur()  // todo: varsa ikinci mağaraya geç!
                çizMerkezdeYazı("Tebrikler!", yeşil, 30)
            }
        }
    }

    def çarpışmalarBirAdımİleri() {
        if (tuşaBasılıMı(tuşlar.VK_RIGHT)) {
            sağaMıBakıyor = doğru
            resmiGüncelle(sağaKoşu)
            ancılResim.götür(yatayHız, 0)
            if (dünya.sağdaÇiniVarMı(ancılResim, çarpışmaDüzeyi)) {
                dünya.soldakiÇiniyeTaşı(ancılResim)
            }
        }
        else if (tuşaBasılıMı(tuşlar.VK_LEFT)) {
            sağaMıBakıyor = yanlış
            resmiGüncelle(solaKoşu)
            ancılResim.götür(-yatayHız, 0)
            if (dünya.soldaÇiniVarMı(ancılResim, çarpışmaDüzeyi)) {
                dünya.sağdakiÇiniyeTaşı(ancılResim)
            }
        }
        else {
            if (sağaMıBakıyor) {
                resmiGüncelle(sağaDönükDuruş)
            }
            else {
                resmiGüncelle(solaDönükDuruş)
            }
        }

        if (tuşaBasılıMı(tuşlar.VK_UP)) {
            if (!zıplıyorMu) {
                dikeyHız = 5
            }
        }

        dikeyHız += yerçekimi
        dikeyHız = math.max(dikeyHız, -10)
        ancılResim.götür(0, dikeyHız)

        if (dünya.aşağıdaÇiniVarMı(ancılResim, çarpışmaDüzeyi)) {
            zıplıyorMu = yanlış
            dünya.yukarıdakiÇiniyeTaşı(ancılResim)
            dikeyHız = 0
        }
        else {
            zıplıyorMu = doğru
            if (dünya.yukarıdaÇiniVarMı(ancılResim, çarpışmaDüzeyi)) {
                dünya.aşağıdakiÇiniyeTaşı(ancılResim)
                dikeyHız = -1
            }
        }

        if (zıplıyorMu) {
            if (sağaMıBakıyor) {
                resmiGüncelle(sağaZıplayış)
            }
            else {
                resmiGüncelle(solaZıplayış)
            }
            ancılResim.sonrakiniGöster(200)
        }
        else {
            ancılResim.sonrakiniGöster()
        }
        gerektikçeTuvaliKaydır()
    }

    var ta = tuvalAlanı
    def gerektikçeTuvaliKaydır() {
        val eşik = 200
        val konum = ancılResim.konum
        if (ta.x + ta.eni - konum.x < eşik) {
            scroll(yatayHız, 0)  // todo
            ta = tuvalAlanı
        }
        else if (konum.x - ta.x < eşik) {
            scroll(-yatayHız, 0)
            ta = tuvalAlanı
        }
    }

    def resmiGüncelle(yeniResim: Resim) {
        if (yeniResim != ancılResim) {
            ancılResim.gizle()
            yeniResim.göster()
            yeniResim.konumuKur(ancılResim.konum)
            ancılResim = yeniResim
        }
    }

    def çiz() {
        çizVeSakla(solaDönükDuruş, sağaKoşu, solaKoşu, sağaZıplayış, solaZıplayış)
        ancılResim.çiz()
    }
}

class İnenÇıkanTaşlar(çiniX: Sayı, çiniY: Sayı, dünya: ÇiniDünyası) {
    val oyuncununKonumu = dünya.çinidenKojoya(ÇiniXY(çiniX, çiniY))
    val sayfa = BirSayfaKostüm("tiles.png", 24, 24)
    // inen çıkan taşları biraz küçültelim. Yoksa yan çinideki oyuncuya da çarparlar:
    def taşınResmi(img: Image) = büyüt(0.98) * götür(0.2, 0.2) -> Resim.imge(img)

    var ancılResim = Resim.küme(Dizin(
        sayfa.resimSeç(0, 6),
        sayfa.resimSeç(1, 6)
    ).map(taşınResmi))

    ancılResim.konumuKur(oyuncununKonumu)

    val yerçekimi = -0.03
    //    var yatayHız = 0.0
    var dikeyHız = -2.0

    def birAdımİleri() {
        dikeyHız += yerçekimi
        dikeyHız = math.max(dikeyHız, -10)
        ancılResim.götür(0, dikeyHız)
        ancılResim.sonrakiniGöster()
        if (dünya.aşağıdaÇiniVarMı(ancılResim, çarpışmaDüzeyi)) {
            dünya.yukarıdakiÇiniyeTaşı(ancılResim)
            dikeyHız = 5
        }
        else if (dünya.yukarıdaÇiniVarMı(ancılResim, çarpışmaDüzeyi)) {
            dünya.aşağıdakiÇiniyeTaşı(ancılResim)
            dikeyHız = -2
        }
    }

    def resmiGüncelle(yeniResim: Resim) {
        if (yeniResim != ancılResim) {
            ancılResim.gizle()
            yeniResim.göster()
            yeniResim.konumuKur(ancılResim.konum)
            ancılResim = yeniResim
        }
    }

    def çiz() {
        ancılResim.çiz()
    }
}

val çiniDünyası =
    new ÇiniDünyası("level1.tmx")

// Yeni bir oyuncu kuralım ve hangi düzeyde olduğunu belirtelim
val oyuncu = new Oyuncu(9, 5, çiniDünyası)
val inenÇıkanTaşlar = Dizin(
    new İnenÇıkanTaşlar(14, 2, çiniDünyası),
    new İnenÇıkanTaşlar(17, 3, çiniDünyası),
    new İnenÇıkanTaşlar(22, 9, çiniDünyası),
    new İnenÇıkanTaşlar(32, 2, çiniDünyası),
    new İnenÇıkanTaşlar(35, 3, çiniDünyası)
)

val hedef = götür(12, 12) * boyaRengi(cm.greenYellow) * kalemRengi(black) -> Resim.daire(10)
hedef.konumuKur(çiniDünyası.çinidenKojoya(ÇiniXY(9, 2)))
hedef.saydamlığıKur(0.2)
çiz(hedef)

val ilkHedef = götür(12, 12) * boyaRengi(cm.red) * kalemRengi(black) -> Resim.daire(10)
ilkHedef.konumuKur(çiniDünyası.çinidenKojoya(ÇiniXY(41, 15)))
çiz(ilkHedef)

çiniDünyası.çiz()
oyuncu.çiz()
inenÇıkanTaşlar.foreach { inenÇıkanTaş =>
    inenÇıkanTaş.çiz()
}

canlandır {
    çiniDünyası.birAdımİleri()
    oyuncu.birAdımİleri()
    inenÇıkanTaşlar.foreach { inenÇıkanTaş =>
        inenÇıkanTaş.birAdımİleri()
        if (oyuncu.ancılResim.çarptıMı(inenÇıkanTaş.ancılResim)) {
            oyuncu.ancılResim.döndür(30)
            durdur()
            çizMerkezdeYazı("Çarptın. Tekrar dene!", kırmızı, 30)
        }
    }
}

tuvaliEtkinleştir()

// oyun kaynaklarını şuradan aldık: https://github.com/pricheal/pygame-tiled-demo
