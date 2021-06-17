// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3
//    http://www.gnu.org/copyleft/gpl.html

// Bülent Başaran Türkçe'ye çevirirken ufak tefek değişiklikler yaptı.

// Enter tuşuyla tümEkranı aç kapa yapın
// d tuşuyla deneme yapmak için bütün satır ve sütünları evle doldurun
// Büyük boşluk tuşuyla soruyu rastgele değiştirin
// Escape tuşuyla pes edin ve iki dakika bitmeden oyuna son verin

val oyunSüresi = 120  // saniye
// bu iki sayıyı azaltarak oyunu kolaylaştırabilir, artırarak da zorlaştırabilirsin:
val enÇokKaçSatır = 10
val enÇokKaçSütun = 10

var tümEkran = yanlış
tümEkranTuval(); tümEkran = doğru
// yaklaşmayaİzinVerme()
val ta = tuvalAlanı
satıryaz(ta.x, ta.y)
val evBoyu = enUfağı(250 / enÇokKaçSatır, 400 / enÇokKaçSütun)
def arayüzTanımı(sayılar: (Sayı, Sayı, Sayı)) = Resim.diziDikey(
    götür(-ta.x - 300, ta.y + 60) -> Resim.arayüz(yanıtPenceresi),
    götür(ta.x + 50, -ta.y + 100) -> evResmi(sayılar._1, sayılar._2, sayılar._3),
    götür(ta.x + 20, -ta.y - 340) * kalemRengi(mavi) -> Resim.yazı("Kaç ev var?", 30)
)
def yeniArayüz(sayılar: (Sayı, Sayı, Sayı)) {
    arayüz.sil()
    arayüz = arayüzTanımı(sayılar)
    çiz(arayüz)
    arayüz.ardaAl() // yoksa oyun bittiğinde yazdığımız yazı altta kalıyor. Neden?
    yanıtPenceresi.takeFocus() // klayve girdisini üstüne alsın
}
var arayüz: Resim = Resim.yatay(1) // şimdilik

def ev() {
    kalemRenginiKur(siyah)
    boyamaRenginiKur(Renk(204, 102, 0))
    yinele(4) {
        ileri(evBoyu)
        sağ()
    }
    zıpla(evBoyu)
    sol()
    zıpla(5)
    boyamaRenginiKur(kırmızı)
    sağ(120)
    yinele(3) {
        ileri(evBoyu + 10)
        sağ(120)
    }
    sağ(60)
    zıpla(5)
    sol() // şimdi kuzeye bakıyor
    zıpla(-evBoyu)
}

def yazıYaz(y: Sayı, dx: Kesir = 0.0, dy: Kesir = 0.0) = {
    val k = konum
    konumuKur(k.x + dx, k.y + dy)
    biçimleriBelleğeYaz()
    kalemRenginiKur(yeşil)
    yazı(f"$y")
    biçimleriGeriYükle()
    konumuKur(k.x, k.y)
}

def evResmi(satırSayısı: Sayı, sütunSayısı: Sayı, artıkSayısı: Sayı) = Resim {
    var str = 0
    var stn = 0
    yinele(satırSayısı) {
        yinele(sütunSayısı) {
            ev()
            if (str == 0) {
                yazıYaz(stn, -14, 20); stn += 1
            }
            sağ()
            zıpla(evBoyu + 25)
            sol()
        }
        yazıYaz(str, -12, 10); str += 1
        sol()
        zıpla(sütunSayısı * (evBoyu + 25))
        sol()
        zıpla(2.6 * evBoyu)
        sol(180)
    }
    yinele(artıkSayısı) {
        ev()
        sağ()
        zıpla(evBoyu + 25)
        sol()
    }
}

val artalanRengi = Renk(208, 144, 73)

var yanıt = 0
var yanıtUzunluğu = 0
var süreBittiMi = yanlış

def farklıBirSayı(n: Sayı, m: Sayı) = {
    def sayı = 2 + rastgele(m - 1)
    var n2 = 0
    do {
        n2 = sayı
    } while (n2 == n)
    n2
}

val yy = yazıyüzü("Sans Serif", 60)
val yanıtPenceresi = new ay.Yazıgirdisi(0) {
    // daha çok bilgi için, google: swing textfield api
    setFont(yy)
    setColumns(5)
    setHorizontalAlignment(ay.değişmez.merkez)
    setBackground(artalanRengi)
    setBorder(ay.çerçeveci.çizgiKenar(siyah))
}

def yeniSoru(s1: Sayı, s2: Sayı) = {
    val sayı1: Sayı = farklıBirSayı(s1, enÇokKaçSatır - 1)
    val sayı2: Sayı = farklıBirSayı(s2, enÇokKaçSütun)
    val sayı3: Sayı = rastgeleDiziden(1 to sayı2 - 1)
    val sayılar = (sayı1, sayı2, sayı3)
    yanıtıKur(sayılar)
    sayılar
}

def yanıtıKur(s: (Sayı, Sayı, Sayı)) = {
    yanıt = s._1 * s._2 + s._3
    yanıtPenceresi.setText("")
    yanıtUzunluğu = yanıt.toString.length
}

var doğruYanıtSayısı = 0
var yanlışYanıtSayısı = 0

var sonSorununSorulduğuZaman = buAn
def yeterinceZamanVarMı = {
    val fark = buAn - sonSorununSorulduğuZaman
    if (fark > 100) {
        sonSorununSorulduğuZaman = buAn
        doğru
    }
    else yanlış
}

val s = yeniSoru(0, 0)
import java.awt.event.{ KeyAdapter, KeyEvent }
yanıtPenceresi.addKeyListener(new KeyAdapter {
    var sayılarAnımsa = s
    def yanıtıDenetle(x: Sayı) {
        if (x == yanıt) {
            yanıtPenceresi.setForeground(yeşil)
            doğruYanıtSayısı += 1
            if (!süreBittiMi && yeterinceZamanVarMı) {
                sırayaSok(0.3) {
                    val sayılar = yeniSoru(sayılarAnımsa._1, sayılarAnımsa._2)
                    sayılarAnımsa = sayılar
                    yeniArayüz(sayılar)
                    yanıtPenceresi.setForeground(siyah)
                }
            }
        }
        else {
            yanıtPenceresi.setForeground(kırmızı)
            yanlışYanıtSayısı += 1
            if (!süreBittiMi) {
                yeniArayüz(sayılarAnımsa)
            }
        }
    }

    def yanıtHazırMı(e: KeyEvent) = {
        yanıtPenceresi.getText.length >= yanıtUzunluğu
    }

    // escape tuşuna basınca oyuna son verelim:
    override def keyPressed(e: KeyEvent) {
        if (e.getKeyCode == tuşlar.VK_ESCAPE) {
            e.consume()
            if (!oyunBitti) {
                oyunSüresineBak(doğru)
            }
            durdur()
            if (tümEkran) {
                tümEkranTuval(); tümEkran = yanlış // tüm ekran modunu kapatalım
            }
        } // d tuşu yazılımcığımızı test etmek için:
        else if (e.getKeyCode == tuşlar.VK_D) {
            val sayılar = (enÇokKaçSatır, enÇokKaçSütun, enÇokKaçSütun)
            yanıtıKur(sayılar)
            yeniArayüz(sayılar)
        } // büyük boşluk tuşuna basarak soruyu değiştirebiliriz:
        else if (e.getKeyCode == tuşlar.VK_SPACE) {
            val sayılar = yeniSoru(0, 0)
            yanıtıKur(sayılar)
            yeniArayüz(sayılar)
        }
        else if (e.getKeyCode == tuşlar.VK_ENTER) {
            tümEkran = !tümEkran
            tümEkranTuval() // tüm ekran modunu aç/kapat
        }
    }

    // sayı dışındaki girdileri yok sayalım
    override def keyTyped(e: KeyEvent) {
        if (!e.getKeyChar.isDigit) {
            e.consume()
        }
    }

    override def keyReleased(e: KeyEvent) {
        if (yanıtHazırMı(e)) {
            val x = yanıtPenceresi.value
            yanıtıDenetle(x)
        }
        else {
            yanıtPenceresi.setForeground(siyah)
        }
    }
})

def mesajıYaz(m: Yazı, renk: Renk) {
    val yç = yazıÇerçevesi(m, 30)
    val resim = kalemRengi(renk) * götür(ta.x + (ta.eni - yç.width) / 2, 0) ->
        Resim.yazı(m, 30)
    çiz(resim)
}

var oyunBitti = yanlış
def oyunSüresineBak(escapeTuşunaBasıldıMı: İkil = yanlış) {
    def sonuç(dy: Sayı, yy: Sayı) = dy - yy
    val geçenSüreResmi = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(
        geçenSüre,
        20, mavi)
    çiz(geçenSüreResmi)
    geçenSüreResmi.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        geçenSüre += 1
        geçenSüreResmi.güncelle(geçenSüre)

        if (geçenSüre == oyunSüresi || escapeTuşunaBasıldıMı) {
            oyunBitti = doğru
            süreBittiMi = doğru
            val durum = if (escapeTuşunaBasıldıMı) s"Oyun $geçenSüre saniye sonra durduruldu."
            else "Oyun bitti!"
            val mesaj = s"""      $durum
            |Doğrular: $doğruYanıtSayısı
            |Yanlışlar: $yanlışYanıtSayısı
            |Skor: ${sonuç(doğruYanıtSayısı, yanlışYanıtSayısı)}
            """
            // arayüz.sil()
            mesajıYaz(mesaj.stripMargin, yeşil) // stripMargin: boşlukları temizleme metodu
            durdur()
        }
    }
}

silVeSakla()
artalanıKur(artalanRengi)

yeniArayüz(s)
var geçenSüre = 0
oyunSüresineBak()
sırayaSok(1) {
    yanıtPenceresi.takeFocus() // klayve girdisini üstüne alsın
}
