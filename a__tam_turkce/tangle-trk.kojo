// yazıdanÇizgeye komutu ve örnek çizgeler:
//#yükle tangle-ornek-cizgeler.kojo
// bellekGeri ve bellekİleri nesneleri:
//#yükle bellek.kojo
//#yükle nokta.kojo
//#yükle dugmeler.kojo

// ingilizce anahtar sözcükler kullanan eski yazılım şurada: ~/src/kojo/tangle/tangle.kojo
silVeSakla(); çıktıyıSil()  // yeni türkçe anahtar sözcüklerle!
// Kare grid çizgesi kuracağız. düzlemseldir.
dez KNS = 1 // Karenin bir kenarında kaç tane nokta olsun? kns arttıkça oyun zorlaşır. Başlangıçtaki nokta sayısı = kns*kns.
den yarıçap = 10 // bu da noktanın yarıçapı
den çizgiler = Küme[Çizgi]() // boş küme olarak başlarız
den noktalar = Küme[Nokta]()
den noktalar2 = Yöney[Nokta]() // dışsal (ya da içsel) nokta eklemek için gerekli

durum sınıf Çizgi(n1: Nokta, n2: Nokta) { // iki noktayı bağla
    gerekli(n1 != n2, s"YANLIŞ! döngü ${n1.yaz}")
    gerekli(!n1.komşuMu(n2), s"YANLIŞ! ${n1.yaz} ve ${n2.yaz} zaten bağlı")
    den resim = doğruÇiz(n1.x, n1.y, n2.x, n2.y)
    n1.komşu(n2)
    n2.komşu(n1)
}

den noktaSilindiMi = yanlış
den çizgiEklendiMi = yanlış
tanım çelişkiVarMı(nktlar: Küme[Nokta]) = {
    için (n1 <- nktlar) {
        için (n2 <- nktlar) {
            eğer (n1 != n2 &&
                n1.komşuMu(n2) != n2.komşuMu(n1)) {
                satıryaz(hataMesajı(n1, n2))
            }
        }
    }
}
tanım hataMesajı(n1: Nokta, n2: Nokta) = s"\ntek yanlı komşuluk! $n1 ve $n2." +
    s"\n${n1.no}->${n2.no}: ${n1.komşuMu(n2)}" +
    s"\n${n2.no}->${n1.no}: ${n2.komşuMu(n1)}"
tanım tekÇizgiMiSilelim(snk: Küme[Nokta]): İkil = {
    eğer (snk.boyu != 2) yanlış yoksa {
        dez (n1, n2) = (snk.başı, snk.kuyruğu.başı)
        dez komşuMu = n1.komşuMu(n2)
        gerekli(komşuMu == n2.komşuMu(n1), hataMesajı(n1, n2))
        komşuMu
    }
}
tanım noktalarYaDaTekBirÇizgiSil(silinecekNoktalar: Küme[Nokta]) = {
    eğer (silinecekNoktalar.boşMu) {
        satıryaz("Önce en az bir nokta seçmelisin")
    }
    yoksa {
        dez tekÇizgiSilinsin = tekÇizgiMiSilelim(silinecekNoktalar)
        eğer (!tekÇizgiSilinsin) {
            noktaSilindiMi = doğru
            dez kalanNoktalar = noktalar.eleDeğilse { silinecekNoktalar(_) }
            noktalar = kalanNoktalar
            noktalar2 = noktalar.yöneye
            silinecekNoktalar.işle { n =>
                n.komşular.işle(_.komşular -= n)
                seçme(n)
                n.resim.sil
            }
        }
        yoksa {
            dez snk = silinecekNoktalar
            dez (n1, n2) = (snk.başı, snk.kuyruğu.başı)
            n1.komşular -= n2
            n2.komşular -= n1
        }
        dez (silinecek, gerisi) = çizgiler.böl { çzg =>
            çzg eşle {
                durum Çizgi(n1, n2) => eğer (tekÇizgiSilinsin) {
                    silinecekNoktalar(n1) && silinecekNoktalar(n2)
                }
                yoksa {
                    silinecekNoktalar(n1) || silinecekNoktalar(n2)
                }
                durum _ => yanlış
            }
        }
        satıryaz(s"${silinecek.boyu} çizgi silinecek ")
        silinecek.işle { çzg =>
            çzg.resim.sil()
        }
        çizgiler = gerisi
    }
}
tanım silmeBilgisi(silinecekNoktalar: Küme[Nokta]) = {
    dez tekÇizgiSilinsin = tekÇizgiMiSilelim(silinecekNoktalar)
    eğer (tekÇizgiSilinsin) s"Seçili çizgiyi sil" yoksa {
        dez (silinecek, gerisi) = çizgiler.böl { çzg =>
            çzg eşle {
                durum Çizgi(n1, n2) => silinecekNoktalar(n1) || silinecekNoktalar(n2)
                durum _             => yanlış
            }
        }
        s"Seçili ${silinecekNoktalar.boyu} nokta ve ${silinecek.boyu} çizgiyi sil"
    }
}

tanım baştan(kns: Sayı) = { // Her nokta (0,0) yani orijine konuyor başta. Merak etme birazdan dağıtacağız
    // tümEkranTuval() //; eksenleriGöster(); gridiGöster()
    dez düğmeler = yeni Düğmeler(kns) { kur() }
    den s = 0 // yoksa komşu seti yanlış çalışıyor!
    dez bns = kns * kns // başlangıçtaki nokta sayısı
    dez noktalar3 = (0 |- bns).soldanKatla(Yöney[Nokta]())((v, i) => { s += 1; v :+ Nokta(s, 0); })
    // her bir çizgiyi tanımlar ve iki noktasına bağlarız. Bir balık ağı gibi. kns * kns düğümlü
    dez çizgiler2 = (0 |- bns).soldanKatla(Yöney[Çizgi]())(
        (çv, i) => {
            dez (x, y) = (i / kns, i % kns)
            dez çzg = eğer (y < kns - 1) { çv :+ Çizgi(noktalar3(i), noktalar3(i + 1)) } yoksa çv
            eğer (x < kns - 1) { çzg :+ Çizgi(noktalar3(i), noktalar3(i + kns)) } yoksa çzg
        })
    çizgiler = çizgiler2.kümeye
    noktalar2 = noktalar3
    noktalar = noktalar2.kümeye
    serpiştir(noktalar, düğmeler) // rasgele dağıt ve çizgileri çiz
}

den seçiliNoktalar = Küme[Nokta]()
tanım seç(n: Nokta) = {
    seçiliNoktalar += n
    n.seçimiKur(doğru)
}
tanım seçme(n: Nokta) = {
    seçiliNoktalar -= n
    n.seçimiKur(yanlış)
}
tanım seçiliKümeyiYaz() = {
    dez küme = seçiliNoktalar // hepsi seçilince, bütün çizgeyi yaz
    eğer (küme.boyu == noktalar.boyu) {
      satıryaz("Bütün noktalar seçili. Komşuluk biçiminde yazalım:")
      dez noktalarSıralı = küme.dizine.sırala { n => n.no }
      dez çizge = için(n <- noktalarSıralı) ver (n, n.komşular.boyu, n.komşular)
      satıryaz(çizge.işle {
        durum(nokta, derece, komşular) => {
            dez başı = f"  \"$nokta"
            f"$başı%-15s $derece%2s ${komşular.yazıYap("", " ", "\"")}"
        }
      }
      .yazıYap("dez çizge1 = Dizin(\n", ",\n", "\n)"))
      //yazıdanÇizgeye(çizgeGI1)  // todo: deneme..
      "Her bir satırda noktanın bilgisi, komşularının sayısı ve bütün komşuları var."
    } yoksa {
      s"${seçiliNoktalar.boyu} nokta seçili: " +
      seçiliNoktalar.işle(n => n.ne).yazıYap("{", " ", "}")
    }
}

tanım doğruÇiz(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir) = {
    dez (en, boy) = (x2 - x1, y2 - y1)
    dez r = götür(x1, y1) -> Resim.düz(en, boy)
    r.çiz
    r
}
tanım serpiştir(hepsi: Küme[Nokta], düğmeler: Düğmeler) {
    // noktaları tuvalde rasgele yerleştirelim ama düğmelerin üstüne gelmesinlar
    dez (mx, my) = (tuvalAlanı.x, tuvalAlanı.y)
    dez en = tuvalAlanı.eni - 2 * yarıçap
    dez boy = tuvalAlanı.boyu - 2 * yarıçap
    tanım deney() = { // düğmelere değmesin!
        den deney = doğru
        den (x, y) = (0.0, 0.0)
        yineleDoğruKaldıkça (deney) {
            x = mx + yarıçap + en * rasgele
            y = my + yarıçap + boy * rasgele
            deney = düğmeler.düğmelereDeğdiMi(x, y)
        }
        (x, y)
    }
    hepsi.herbiriİçin(nkt => { dez (x, y) = deney(); nkt.yeniKonum(x, y) })
    çizelim(çizgiler)  // çizgileri de yeniden çizelim
}
tanım çizelim(hepsi: Küme[Çizgi]) { // Her çizgi iki noktasının çemberine kadar gelsin
    //den nk = Küme.boş[Nokta]
    hepsi.herbiriİçin(çzg => {
        dez (x1, y1) = (çzg.n1.x, çzg.n1.y)
        dez (x2, y2) = (çzg.n2.x, çzg.n2.y)
        dez boy = karekökü(karesi(x2 - x1) + karesi(y2 - y1))
        dez (xr, yr) = (yarıçap / boy * (x2 - x1), yarıçap / boy * (y2 - y1))
        çzg.resim.sil
        çzg.resim = doğruÇiz(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
        /*nk += çzg.n1
        nk += çzg.n2*/
    })
}
tanım kaçTane(düzenli: İkil = doğru) = {
    eğer (düzenli) f"${noktalar.boyu}%2d nokta ve ${çizgiler.boyu}%2d çizgi var"
    yoksa s"${noktalar.boyu} nokta ve ${çizgiler.boyu} çizgi var"
}

baştan(KNS)
