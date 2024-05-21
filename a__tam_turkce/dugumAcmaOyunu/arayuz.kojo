// Bu altdosya arayüzün tümü değil. Ama bütün düğmeler burada.
// nokta.kojo ve anadosyada da arayüz işlevleri var.

//#yükle temel.kojo
//#yükle bellek.kojo

/* template (ttodo)
 // d: düğme
 // N: satır numarası
 // X: sütun (a, b, c, d, e)
        tanım dNX() = {
            dez düğme = kare(sütnX, sıraN, dBoyu)
            yardımıKur(düğme, "<Bu düğme ne yapsın?>")
            düğme.boyamaRenginiKur(renkler.coral)
            düğme.fareyeTıklayınca { (x, y) =>
                satıryaz("dNX'e tıklandı")
            }
        }
 */

nesne arayüz_temel {
    dez dGrid = 40 // düğmeleri sol alt köşede bir gride yerleştirelim
    dez (kx, ky) = (0.9 * tuvalAlanı.x, 0.9 * tuvalAlanı.y) // sol alt köşe
    dez dBoyu = dGrid - 5
    dez (sıra1, sıra2, sıra3, sıra4, sıra5) = (ky, ky + dGrid, ky + 2 * dGrid, ky + 3 * dGrid, ky + 4 * dGrid)
    dez (sütn1, sütn2, sütn3, sütn4, sütn5) = (kx, kx + dGrid, kx + 2 * dGrid, kx + 3 * dGrid, kx + 4 * dGrid)
}

sınıf Düğmeler(kns: Sayı) {
    tanım kur() { // düğmeleri bir grid üzerine koyalım
        düğmelerinİlkSırası() // dört sıra düğmemiz var
        düğmelerinİkinciSırası() // nokta/çizgi ekleme komutları
        düğmelerinÜçüncüSırası() // nokta seçme komutları
        düğmelerinDördüncüSırası() // nokta devinim komutları
        // 4. ve 5. sütun işleri karıştırdı biraz..
    }
    tanım düğmelereDeğdiMi(x: Kesir, y: Kesir) = {
        dez (x1, y1) = (kx - yarıçap, ky - yarıçap)
        // DİKKAT 4x3'lük alandan fazla düğme olursa bunu da büyüt
        dez (x2, y2) = (kx + 3 * dGrid + yarıçap, ky + 4 * dGrid + yarıçap)
        x1 < x && x < x2 && y1 < y && y < y2
    }
    getir arayüz_temel._
  dez (kx, ky) = (arayüz_temel.kx, arayüz_temel.ky)
  dez dGrid = arayüz_temel.dGrid
    // yeni nokta konumu
    tanım ynkx = { // rastgele olmazsa seçiliNoktalar küme metodları hata veriyordu. Eşsizlik özelliğini ekleyerek bu sorunu çözdük.
        dez yeniZ = sütn5 + yarıçap
        yuvarla(rastgeleKesir(yeniZ, yeniZ + 2 * dGrid + yarıçap), 1) // todo: DRY nokta oluşturma ve kaydırma
    }
    tanım ynky = {
        dez yeniZ = ky - yarıçap
        yuvarla(rastgeleKesir(yeniZ, yeniZ + 4 * dGrid + yarıçap), 1) // todo: DRY nokta oluşturma ve kaydırma
    }
    den yardım = Resim.arayüz(ay.Tanıt("Yardım"))
    tanım yardımEt(x: Kesir, y: Kesir, m: Yazı) = {
        yardım = Resim.arayüz(ay.Tanıt(m))
        yardım.konumuKur(sütn1, sıra1 - dGrid/2)
        yardım.büyütmeyiKur(1.1) // yazıyı büyütelim
        eğer (!yardım.çizili) yardım.çiz
        yoksa yardım.sil()
    }
    tanım yardımıKapat() = { yardım.sil() }
    tanım yardımıKur(r: Resim, mesaj: Yazı) = {
        r.fareGirince { (x, y) => yardımEt(x, y, mesaj) }
        r.fareÇıkınca { (_, _) => yardımıKapat }
    }
    tanım yardımıKur2(r: Resim, işlev: () => Yazı) {
        r.fareGirince { (x, y) => yardımEt(x, y, işlev()) }
        r.fareÇıkınca { (_, _) => yardımıKapat }
    }
    tanım kare(x: Kesir, y: Kesir, en: Kesir) = {
        dez k = götür(x, y) * kalemRengi(renksiz) -> Resim.dikdörtgen(en, en)
        k.çiz
        k
    }
    tanım düğmelerinİlkSırası() {
        dez (b1, b2) = (kare(sütn1, sıra1, dBoyu), kare(sütn2, sıra1, dBoyu))
        b1.boyamaRenginiKur(kırmızı); b1.fareyeTıklayınca { (_, _) => serpiştir(noktalar, bu) }
        yardımıKur(b1, "Noktaları rastgele serpiştir")
        b2.boyamaRenginiKur(turuncu); b2.fareyeTıklayınca { (_, _) => { yardımıKapat; tümEkranTuval() } }
        yardımıKur(b2, "Tüm ekrana geç ya da tüm ekrandan çık")
        dez b3 = kare(sütn3, sıra1, dBoyu)
        b3.boyamaRenginiKur(siyah)
        b3.fareyeTıklayınca { (_, _) => noktalarYaDaTekBirÇizgiSil(seçiliNoktalar); yardımıKapat() }
        yardımıKur2(b3, () =>
            eğer (seçiliNoktalar.boşMu) "Seçili çizgiyi ya da noktaları sil"
            yoksa silmeBilgisi(seçiliNoktalar)
        )
        tanım d1d() = {
            dez düğme = kare(sütn4, sıra1, dBoyu)
            yardımıKur(düğme, "<Bu düğme ne yapsın?>")
            düğme.boyamaRenginiKur(renkler.coral)
            düğme.fareyeTıklayınca { (x, y) =>
                satıryaz("d1d'ye tıklandı")
            }
        }
        d1d()
    }
    tanım düğmelerinİkinciSırası() { // otomatik dış kenara (dışsal), d2b/c: seçili noktalara
        tanım d2a() = {
            tanım kümeler(d: Sayı) = {
                tanım iç4Kenar() = { // ilk kare çizitin kenarları
                    dez r1 = Yöney((0, 1), (d - 1, d), (d * d - 1, -1), (d * (d - 1), -d))
                    dez l0 = (için ((a, c) <- (için (i <- 0 |-| 3) ver r1(i))) ver (a, (a + c * d), c)).dizine
                    için ((a, b, c) <- l0) ver ((a |- b adım c).dizine)
                }
                // dış kenarı, iki listeyi fermuar gibi bir araya getirerek buluyoruz
                tanım dış(i1: Dizin[Sayı], i2: Dizin[Sayı]) = {
                    dez l3 = i2.ikile(i1.kuyruğu ::: Dizin(i1.başı)).düzİşle(p => Dizin(p._1, p._2))
                    dez l4 = l3 ::: Dizin(l3.başı)
                    için (i <- 0 |-| 7 adım 2) ver (l4.düşür(i).al(3))
                }
                dez d2 = d * d
                dez l1 = Dizin(0, d - 1, d2 - 1, d2 - d)
                dez l2 = Dizin(d2, d2 + 1, d2 + 2, d2 + 3)
                dez l3 = l2.işle(_ + 4)
                dez l4 = l3.işle(_ + 4)
                dez l5 = l4.işle(_ + 4)
                Dizin(iç4Kenar, dış(l1, l2), dış(l2, l3), dış(l3, l4), dış(l4, l5), dış(l5, l5.işle(_ + 4))).düzİşle(_.dizine)
            }
            dez düğme = kare(sütn1, sıra2, dBoyu) // mavi kare yeni bir nokta ekler
            den kümeTükendi = yanlış
            yardımıKur2(düğme, () =>
                eğer (noktaSilindiMi) "Nokta silmeden önce kullan"
                yoksa eğer (çizgiEklendiMi) "Çizgi eklemeden önce kullan"
                yoksa eğer (kümeTükendi) "Başka dışsal/içsel nokta ekleyemiyoruz"
                yoksa "Dışsal/içsel nokta ekle (çizge yüzeysel kalır)")
            düğme.kalemRenginiKur(mavi)
            düğme.boyamaRenginiKur(mavi)
            den yeniNokta = 0 // kk'nin kaçıncı kümesine bağlanacak bu yeni nokta?
            dez kk = kümeler(kns)
            satıryaz(kk.boyu + " dışsal/içsel nokta ekleyebilirsin")
            düğme.fareyeTıklayınca { (x, y) => // bu kareye her basışımızda yeni bir nokta ekleyelim
                dez msj = (ne: Yazı) => satıryaz(s"$ne var. Dışsal/içsel nokta ekleyemiyoruz artık!")
                eğer (noktaSilindiMi) msj("Silinen noktalar")
                yoksa eğer (çizgiEklendiMi) msj("Eklenen çizgiler")
                yoksa eğer (yeniNokta < kk.boyu) {
                    dez yn = Nokta(ynkx, ynky)
                    seç(yn)
                    noktalar += yn
                    noktalar2 = noktalar2 :+ yn
                    çizgiler = çizgiler ++ (için (i <- kk(yeniNokta)) ver (Çizgi(yn, noktalar2(i))))
                    yeniNokta += 1
                    satıryaz(kaçTane())
                    çizelim(çizgiler)
                }
                yoksa {
                    kümeTükendi = doğru
                    satıryaz("Başka dışsal/içsel nokta ekleyemiyoruz (şimdilik!)")
                }
                eğer (noktaSilindiMi || çizgiEklendiMi || kümeTükendi)
                    düğme.saydamlığıKur(0.2)
            }
        }
        tanım d2b() = {
            dez düğme = kare(sütn2, sıra2, dBoyu)
            yardımıKur(düğme, "Seçili noktalara bağlı yeni bir nokta ekle")
            düğme.boyamaRenginiKur(sarı)
            düğme.fareyeTıklayınca { (x, y) => // nokta ekle ve bütün kümeye bağla
                dez yn = Nokta(ynkx, ynky)
                noktalar += yn
                çizgiler = çizgiler ++ (için (en <- seçiliNoktalar) ver (Çizgi(yn, en)))
                seç(yn)
                satıryaz(kaçTane())
                çizelim(çizgiler)
            }
        }
        tanım çizgiEkle(çizgi: Çizgi) = { çizgiler += çizgi }
        tanım d2c() = { // seçili noktaları çizgiyle bağla
            tanım uyarı1(n1: Nokta, n2: Nokta) = satıryaz(s"DİKKAT: ${n1.ne} ve ${n2.ne} zaten bağlı")
            dez düğme = kare(sütn3, sıra2, dBoyu)
            yardımıKur(düğme, "Seçili noktaları bağla")
            düğme.boyamaRenginiKur(kahverengi)
            düğme.fareyeTıklayınca { (x, y) =>
                eğer (seçiliNoktalar.boyu < 2) satıryaz("Önce en az iki nokta seçmelisin")
                yoksa {
                        seçiliNoktalar.altKümeleri(2).işle(_.dizine).herbiriİçin {
                        durum Dizin(n1, n2) => eğer (!n1.komşuMu(n2)) çizgiEkle(Çizgi(n1, n2)) yoksa uyarı1(n1, n2)
                        durum xs            => satıryaz(s"YANLIŞ! $xs")
                    }
                    çizelim(çizgiler)
                    çizgiEklendiMi = doğru
                }
            }
        }
        tanım d2d() = {
            dez düğme = kare(sütn4, sıra2, dBoyu)
            yardımıKur(düğme, "<Bu düğme ne yapsın?>")
            düğme.boyamaRenginiKur(renkler.coral)
            düğme.fareyeTıklayınca { (x, y) =>
                satıryaz("d2d'ye tıklandı")
            }
        }
        d2a(); d2b(); d2c(); d2d()
    }
    tanım düğmelerinÜçüncüSırası() {
        tanım d3a() = { // seçim kümesini sil, ya da hepsini seç
            dez düğme = kare(sütn1, sıra3, dBoyu)
            yardımıKur(düğme, "Seçim kümesini sil ya da her noktayı seç")
            düğme.boyamaRenginiKur(yeşil)
            düğme.kalemKalınlığınıKur(4)
            düğme.fareyeTıklayınca { (x, y) =>
                eğer (!seçiliNoktalar.boşMu) {
                    seçiliNoktalar.işle(_.seçimiKur(yanlış)) // seçme(n)
                    seçiliNoktalar = seçiliNoktalar.boş
                }
                yoksa noktalar.işle(seç(_))
            }
        }
        tanım d3b() = { // seçili noktaları göster
            dez düğme = kare(sütn2, sıra3, dBoyu)
            yardımıKur2(düğme, () => {
                dez msj = s"${seçiliNoktalar.boyu} nokta seçili"
                eğer (seçiliNoktalar.boyu != 1) msj yoksa s"$msj. ${seçiliNoktalar.başı.komşular.boyu} komşusu var"
            })
            düğme.boyamaRenginiKur(renkler.darkGreen) // ttodo
            düğme.fareyeTıklayınca { (x, y) =>
                dez s = seçiliNoktalar.boyu
                eğer (s > 0) satıryaz(seçiliKümeyiYaz) yoksa satıryaz("Seçilmiş nokta yok")
            }
        }
        tanım d3c() = {
            dez düğme = kare(sütn3, sıra3, dBoyu)
            yardımıKur2(düğme, () => kaçTane(yanlış))
            düğme.boyamaRenginiKur(mor)
            düğme.fareyeTıklayınca { (x, y) =>
                satıryaz(kaçTane())
                eğer (!seçiliNoktalar.boşMu) {
                    dez komşular = (n: Nokta) => n.kim.yazıYap("{",", ","}")
                    seçiliNoktalar.işle(n => satıryaz(f"$n%11s: ${n.kim.boyu} komşusu var: ${komşular(n)}"))
                    eğer (seçiliNoktalar.boyu == 2) {
                        dez (n1, n2) = (seçiliNoktalar.başı, seçiliNoktalar.kuyruğu.başı)
                        dez msj = "seçili iki nokta komşu"
                        satıryaz(msj + (eğer (n1.komşuMu(n2)) "lar" yoksa " değiller"))
                    }
                    çelişkiVarMı(seçiliNoktalar)
                }
            }
        }
        tanım d3d() = {
            dez düğme = kare(sütn4, sıra3, dBoyu)
            yardımıKur(düğme, "Belleğe yaz")
            düğme.boyamaRenginiKur(morumsu)
            düğme.fareyeTıklayınca { (_, _) =>
                bellekGeri.koy(noktalar, çizgiler)
                satıryaz(s"Anı ${bellekGeri.boyu()}: ${noktalar.boyu} nokta ve ${çizgiler.boyu} çizgi var.")
            }
        }
        d3a(); d3b(); d3c(); d3d()
    }
    tanım düğmelerinDördüncüSırası() {
        tanım d4a() = {
            dez düğme = kare(sütn1, sıra4, dBoyu)
            yardımıKur(düğme, "Seçili noktayı komşularının ağırlık merkezine götür")
            düğme.boyamaRenginiKur(gri)
            düğme.fareyeTıklayınca { (x, y) =>
                eğer (seçiliNoktalar.boyu == 0) satıryaz("Nokta seç ki komşularının ağırlık merkezine götürelim")
                yoksa seçiliNoktalar.işle(_.merkezeGit())
            }
        }
        tanım d4b() = {
            dez düğme = kare(sütn2, sıra4, dBoyu)
            yardımıKur(düğme, "Seçili noktanın komşularını yanına topla")
            düğme.boyamaRenginiKur(açıkGri)
            düğme.fareyeTıklayınca { (x, y) =>
                eğer (seçiliNoktalar.boyu == 0) satıryaz("Nokta seç ki komşularını çağıralım")
                yoksa seçiliNoktalar.işle(_.merkezeGetir())
            }
        }
        tanım d4c() = {
            dez düğme = kare(sütn3, sıra4, dBoyu)
            yardımıKur(düğme, "Noktaları küçült ya da büyült")
            düğme.boyamaRenginiKur(renkler.turquoise)  // ttodo
            düğme.fareyeTıklayınca { (_, _) =>
                yarıçap = yarıçap eşle {
                    durum 10 => 1
                    durum 1  => 10
                    durum _  => 10
                }
                noktalar.işle(_.yineÇiz())
                çizelim(çizgiler)
            }
        }
        tanım d4d() = {
            dez düğme = kare(sütn4, sıra4, dBoyu)
            yardımıKur(düğme, "Bir önceki anıya dön")
            düğme.boyamaRenginiKur(pembe)
            düğme.fareyeTıklayınca { (_, _) =>
                dez geçmiş = bellekGeri.al()
                println(geçmiş)
            }
        }
        d4a(); d4b(); d4c(); d4d()
    }
}
