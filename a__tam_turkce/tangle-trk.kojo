// ingilizce anahtar sözcükler kullanan eski yazılım şurada: ~/src/kojo/tangle/tangle.kojo
silVeSakla(); çıktıyıSil()  // yeni türkçe anahtar sözcüklerle!
// Kare grid çizgesi kuracağız. düzlemseldir.
dez KNS = 4 // Karenin bir kenarında kaç tane nokta olsun? kns arttıkça oyun zorlaşır.
// başlangıçtaki nokta sayısı = kns*kns.
den yarıçap = 10 // bu da noktanın yarıçapı
den çizgiler = Küme[Çizgi]() // boş küme olarak başlarız
den noktalar = Küme[Nokta]()
den noktalar2 = Yöney[Nokta]() // dışsal nokta eklemek için gerekli

durum sınıf Çizgi(n1: Nokta, n2: Nokta) { // iki noktayı bağla
    gerekli(n1 != n2, s"YANLIŞ! döngü ${n1.yaz}")
    gerekli(!n1.komşuMu(n2), s"YANLIŞ! ${n1.yaz} ve ${n2.yaz} zaten bağlı")
    den resim = doğruÇiz(n1.x, n1.y, n2.x, n2.y)
    n1.komşu(n2)
    n2.komşu(n1)
}

den nkn = 0 // nokta kimlik numarası
durum sınıf Nokta(den x: Kesir, den y: Kesir) {
    // no (numara) ve equals/hashCode yöntemlerini yeniden tanımlamak Küme'nin noktaları birbirinden ayırabilmesi için gerekli
    dez no = nkn
    nkn += 1
    // ttodo: iki nesnenin "kıyma kodu" farklıysa, nesneler de farklıdır. Değilse, o zaman daha yavaş olan equals yöntemi kullanılır
    baskın tanım hashCode = no.kıymaKodu // hashCode: kıyma-kodu -- her nesnenin kendine özgü bir sayıya çevrilmesinde fayda var
    baskın tanım equals(ne: Her) = ne eşle { // equals: eşit mi? -- bu nokta verilen nesneyle aynı mı?
        durum o: Nokta => o.no == no
        durum _        => yanlış
    }
    dez buNokta = bu
    den resim: Resim = başla(); seçimiKur(yanlış)
    gizli tanım başla() = {
        dez res = götür(x, y) -> Resim.daire(yarıçap)
        res.çiz
        fareyiTanımla(res)
        res
    }
    den komşular = Küme[Nokta]()
    tanım komşuMu(n: Nokta) = komşular(n)
    tanım komşu(n: Nokta) =
        eğer (n != buNokta) komşular += n
        yoksa satıryaz("YANLIŞ! " + buNokta.yaz)
    tanım kim() = komşular
    tanım yineÇiz() {
        resim.sil
        resim = başla()
        seçimiKur(seçiliNoktalar(buNokta))
    }
    tanım seçimiKur(s: İkil) = { // seçilmiş Noktalar farklı görünsün
        resim.kalemKalınlığınıKur(eğer (s) 4 yoksa 2)
        resim.kalemRenginiKur(mavi)
        resim.boyamaRenginiKur(eğer (s) kırmızı yoksa (renksiz))
    }
    tanım yeniKonum(yeniX: Kesir, yeniY: Kesir) {
        x = yeniX; y = yeniY
        resim.konumuKur(yeniX, yeniY)
    }
    baskın tanım toString = ne  // yazıya çeviren 'toString' yöntemini yeniden tanımlıyoruz
    tanım yaz = s"$ne d=${kim.boyu}"
    tanım ne = s"N$no@${yuvarla(x).sayıya},${yuvarla(y).sayıya}"

    tanım fareyiTanımla(r: Resim) = {
        r.fareyeTıklayınca { (mx, my) =>
            eğer (seçiliNoktalar(buNokta)) seçme(buNokta) yoksa seç(buNokta)
            satıryaz(s"$yaz. $seçiliKümeyiYaz")
        }
        // çekince bu çalışacak. Yeri değişince ona bağlı çizgileri tekrar çizmemiz gerek
        r.fareyiSürükleyince { (mx, my) => { yeniKonum(mx, my); çizelim(çizgiler) } }
    }
    tanım merkezeGit() = { // komşuların ağırlık merkezine gidelim
        dez s = komşular.boyu
        eğer (s == 0) satıryaz("Komşum yok!") yoksa {
            dez nx = komşular.soldanKatla(0.0)((d, n) => d + n.x) / s.kesire
            dez ny = komşular.soldanKatla(0.0)((d, n) => d + n.y) / s.kesire
            yeniKonum(nx, ny); çizelim(çizgiler)
        }
    }
    tanım merkezeGetir() = { // komşumuz olan noktaları yakın yörüngeye çekelim.
        dez k = komşular
        dez (s, g) = (komşular.boyu, 2 * yarıçap)
        dez tg = 2 * g
        eğer (s == 0) satıryaz("Komşum yok!") yoksa {
            dez ikik = 24 // bundan çok komşu varsa, iki kere çevirsek de yetmez
            dez evre = Dizin((g, g), (g, 0), (g, -g), (0, -g), (-g, -g), (-g, 0), (-g, g), (0, g))
            dez e2 = eğer (s < 9) evre yoksa { //                                          21012
                eğer (s > ikik) satıryaz(s"$ikik'ten çok komşu var! " + //                 g...g
                    s"${s - ikik} tanesini elinle yap, ne olur!") //                       0.x.0
                evre ::: Dizin((tg, tg), (tg, g), (tg, 0), (tg, -g), (tg, -tg), //         -...-
                    (-tg, tg), (-tg, g), (-tg, 0), (-tg, -g), (-tg, -tg), //               21012
                    (-g, tg), (0, tg), (g, tg), (-g, -tg), (0, -tg), (g, -tg))
            }
            k.al(ikik).ikile(e2).işle { durum (k, (nx, ny)) => k.yeniKonum(x + nx, y + ny) }
            eğer (s > ikik)
                k.düşür(ikik).al(ikik).ikile(e2).işle { durum (k, (nx, ny)) => k.yeniKonum(x + nx, y + ny) }
            eğer (s > 2 * ikik)
                k.düşür(2 * ikik).al(ikik).ikile(e2).işle { durum (k, (nx, ny)) => k.yeniKonum(x + nx, y + ny) }
            çizelim(çizgiler)
        }
    }
}

/* todo Gelecekte geliştirmek için belki yaparız :-)
   noktaları bir grid üstüne koysak? Üstüste hiç getirmesek? Ama ya yaklaşınca uzaklaşınca?
   sahneyi sınırlarız olur biter...
dez GNS = 100
dez grid = Dizim.boş[Nokta](GNS, GNS)*/

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
    tümEkranTuval() //; eksenleriGöster(); gridiGöster()
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
      yazıdanÇizgeye(çizgeGI1)  // todo: deneme..
      "Her bir satırda noktanın bilgisi, komşularının sayısı ve bütün komşuları var."
    } yoksa {
      s"${seçiliNoktalar.boyu} nokta seçili: " +
      seçiliNoktalar.işle(n => n.ne).yazıYap("{", " ", "}")
    }
}
tanım yazıdanÇizgeye(girdi: Dizin[Yazı]): Dizin[(Nokta, (Sayı, Sayı))] = {
    dez deneme = için(satır <- girdi) ver(satır.böl(" ").eleDeğilse(_.boyu == 0))
    dez bilgi = deneme.yazıYap("", "\n", "")
    // satıryaz(bilgi)
    // todo: şimdilik..
    dez n = eğer (noktalar.boyu == 0) {
        Nokta(10, 20) // yeni bir nokta oluştur
    } yoksa noktalar.başı
    Dizin( (n, (10, 20)), (n, (20, 10)) )
}
// ilginç birkaç çizge:
dez çizge0 = Dizin(
  "N0@77,123     2 N1@-290,91 N3@10,-74",
  "N1@-290,91    3 N0@77,123 N2@78,-38 N4@113,126",
  "N2@78,-38     2 N1@-290,91 N5@-7,-26",
  "N3@10,-74     3 N0@77,123 N4@113,126 N6@110,95",
  "N4@113,126    4 N1@-290,91 N3@10,-74 N5@-7,-26 N7@62,79",
  "N5@-7,-26     3 N2@78,-38 N4@113,126 N8@294,-127",
  "N6@110,95     2 N3@10,-74 N7@62,79",
  "N7@62,79      3 N4@113,126 N6@110,95 N8@294,-127",
  "N8@294,-127   2 N5@-7,-26 N7@62,79"
)
dez çizge1 = Dizin(
  "N0@513,131    4 N1@563,223 N3@461,264 N9@614,171 N12@380,211",
  "N1@563,223    4 N0@513,131 N2@666,254 N4@506,296 N9@614,171",
  "N2@666,254    4 N1@563,223 N5@560,332 N9@614,171 N10@608,367",
  "N3@461,264    4 N0@513,131 N4@506,296 N6@332,316 N12@380,211",
  "N4@506,296    4 N1@563,223 N3@461,264 N5@560,332 N7@444,353",
  "N5@560,332    4 N2@666,254 N4@506,296 N8@493,421 N10@608,367",
  "N6@332,316    4 N3@461,264 N7@444,353 N11@393,392 N12@380,211",
  "N7@444,353    4 N4@506,296 N6@332,316 N8@493,421 N11@393,392",
  "N8@493,421    4 N5@560,332 N7@444,353 N10@608,367 N11@393,392",
  "N9@614,171    3 N0@513,131 N1@563,223 N2@666,254",
  "N10@608,367   3 N2@666,254 N5@560,332 N8@493,421",
  "N11@393,392   3 N8@493,421 N7@444,353 N6@332,316",
  "N12@380,211   3 N6@332,316 N3@461,264 N0@513,131"
)
dez çizge1b = Dizin(
  "N0@513,131    5 N1@563,223 N9@614,171 N12@380,211 N3@450,257 N16@514,81",
  "N1@563,223    6 N0@513,131 N5@560,332 N9@614,171 N2@666,254 N3@450,257 N4@506,296",
  "N2@666,254    5 N5@560,332 N10@608,367 N1@563,223 N9@614,171 N13@733,249",
  "N3@450,257    6 N0@513,131 N1@563,223 N6@332,316 N12@380,211 N7@444,353 N4@506,296",
  "N4@506,296    4 N1@563,223 N3@450,257 N5@560,332 N7@444,353",
  "N5@560,332    6 N10@608,367 N1@563,223 N2@666,254 N7@444,353 N8@493,421 N4@506,296",
  "N6@332,316    5 N12@380,211 N7@444,353 N3@450,257 N11@393,392 N15@272,321",
  "N7@444,353    6 N5@560,332 N6@332,316 N3@450,257 N11@393,392 N8@493,421 N4@506,296",
  "N8@493,421    5 N5@560,332 N10@608,367 N14@494,486 N7@444,353 N11@393,392",
  "N9@614,171    5 N0@513,131 N1@563,223 N13@733,249 N2@666,254 N16@514,81",
  "N10@608,367   5 N5@560,332 N14@494,486 N13@733,249 N2@666,254 N8@493,421",
  "N11@393,392   5 N14@494,486 N6@332,316 N7@444,353 N8@493,421 N15@272,321",
  "N12@380,211   5 N0@513,131 N6@332,316 N3@450,257 N16@514,81 N15@272,321",
  "N13@733,249   3 N9@614,171 N2@666,254 N10@608,367",
  "N14@494,486   3 N10@608,367 N8@493,421 N11@393,392",
  "N15@272,321   3 N11@393,392 N6@332,316 N12@380,211",
  "N16@514,81    3 N12@380,211 N0@513,131 N9@614,171"
)
dez çizge1c = Dizin( // 17 nokta, 44 çizgi. Fotolara bak!
  "N0@513,131    5 N1@563,223 N9@614,171 N12@380,211 N3@450,257 N16@479,32",
  "N1@563,223    6 N0@513,131 N5@560,332 N9@614,171 N2@666,254 N3@450,257 N4@506,296",
  "N2@666,254    5 N5@560,332 N10@608,367 N1@563,223 N9@614,171 N13@784,261",
  "N3@450,257    6 N0@513,131 N1@563,223 N6@332,316 N12@380,211 N7@444,353 N4@506,296",
  "N4@506,296    4 N1@563,223 N3@450,257 N5@560,332 N7@444,353",
  "N5@560,332    6 N10@608,367 N1@563,223 N2@666,254 N7@444,353 N8@493,421 N4@506,296",
  "N6@332,316    5 N12@380,211 N7@444,353 N3@450,257 N11@393,392 N15@214,319",
  "N7@444,353    6 N5@560,332 N6@332,316 N3@450,257 N11@393,392 N8@493,421 N4@506,296",
  "N8@493,421    5 N5@560,332 N10@608,367 N14@513,508 N7@444,353 N11@393,392",
  "N9@614,171    5 N0@513,131 N1@563,223 N13@784,261 N2@666,254 N16@479,32",
  "N10@608,367   5 N5@560,332 N14@513,508 N13@784,261 N2@666,254 N8@493,421",
  "N11@393,392   5 N14@513,508 N6@332,316 N7@444,353 N8@493,421 N15@214,319",
  "N12@380,211   5 N0@513,131 N6@332,316 N3@450,257 N16@479,32 N15@214,319",
  "N13@784,261   5 N10@608,367 N14@513,508 N9@614,171 N2@666,254 N16@479,32",
  "N14@513,508   5 N10@608,367 N13@784,261 N11@393,392 N8@493,421 N15@214,319",
  "N15@214,319   5 N14@513,508 N6@332,316 N12@380,211 N16@479,32 N11@393,392",
  "N16@479,32    5 N0@513,131 N9@614,171 N13@784,261 N12@380,211 N15@214,319"
)
dez çizgeGI1 = Dizin( // graph iso examples
  "N0@-48,163    3 N1@137,164 N2@-43,64 N3@138,64",
  "N1@137,164    2 N0@-48,163 N3@138,64",
  "N2@-43,64     2 N0@-48,163 N3@138,64",
  "N3@138,64     3 N1@137,164 N2@-43,64 N0@-48,163"
)
dez çizgeGI2 = Dizin(
  "N0@-69,10     2 N1@61,-102 N2@-37,155",
  "N1@61,-102    2 N0@-69,10 N4@201,5",
  "N2@-37,155    2 N0@-69,10 N3@143,155",
  "N3@143,155    2 N2@-37,155 N4@201,5",
  "N4@201,5      2 N1@61,-102 N3@143,155"
)
dez çizgeMaksimal1 = Dizin(  // 2x2'ye 24 dışsal nokta ekledim
  "N0@-1,-399    5 N1@601,2 N2@-601,0 N7@-178,-118 N11@0,-134 N4@178,-117",
  "N1@601,2      5 N0@-1,-399 N5@178,120 N3@0,401 N8@203,1 N4@178,-117",
  "N2@-601,0     5 N0@-1,-399 N10@-203,1 N6@-178,119 N7@-178,-118 N3@0,401",
  "N3@0,401      5 N5@178,120 N1@601,2 N6@-178,119 N9@0,136 N2@-601,0",
  "N4@178,-117   5 N0@-1,-399 N1@601,2 N11@0,-134 N8@203,1 N15@86,-56",
  "N5@178,120    5 N1@601,2 N9@0,136 N12@86,58 N3@0,401 N8@203,1",
  "N6@-178,119   5 N10@-203,1 N9@0,136 N13@-86,58 N2@-601,0 N3@0,401",
  "N7@-178,-118  5 N0@-1,-399 N10@-203,1 N14@-86,-56 N2@-601,0 N11@0,-134",
  "N8@203,1      6 N5@178,120 N1@601,2 N12@86,58 N19@88,1 N4@178,-117 N15@86,-56",
  "N9@0,136      6 N5@178,120 N6@-178,119 N13@-86,58 N12@86,58 N3@0,401 N16@0,60",
  "N10@-203,1    6 N14@-86,-56 N6@-178,119 N13@-86,58 N2@-601,0 N17@-89,1 N7@-178,-118",
  "N11@0,-134    6 N0@-1,-399 N14@-86,-56 N7@-178,-118 N18@0,-58 N4@178,-117 N15@86,-56",
  "N12@86,58     6 N5@178,120 N9@0,136 N16@0,60 N23@47,32 N8@203,1 N19@88,1",
  "N13@-86,58    6 N10@-203,1 N20@-47,32 N6@-178,119 N9@0,136 N17@-89,1 N16@0,60",
  "N14@-86,-56   6 N10@-203,1 N21@-47,-30 N17@-89,1 N7@-178,-118 N18@0,-58 N11@0,-134",
  "N15@86,-56    6 N22@47,-30 N18@0,-58 N11@0,-134 N8@203,1 N19@88,1 N4@178,-117",
  "N16@0,60      6 N20@-47,32 N9@0,136 N13@-86,58 N27@0,42 N12@86,58 N23@47,32",
  "N17@-89,1     6 N10@-203,1 N24@-61,1 N14@-86,-56 N20@-47,32 N21@-47,-30 N13@-86,58",
  "N18@0,-58     6 N25@0,-39 N14@-86,-56 N21@-47,-30 N22@47,-30 N11@0,-134 N15@86,-56",
  "N19@88,1      6 N22@47,-30 N12@86,58 N8@203,1 N15@86,-56 N26@61,1 N23@47,32",
  "N20@-47,32    5 N24@-61,1 N13@-86,58 N17@-89,1 N27@0,42 N16@0,60",
  "N21@-47,-30   5 N14@-86,-56 N17@-89,1 N18@0,-58 N24@-61,1 N25@0,-39",
  "N22@47,-30    5 N25@0,-39 N18@0,-58 N26@61,1 N19@88,1 N15@86,-56",
  "N23@47,32     5 N27@0,42 N12@86,58 N16@0,60 N26@61,1 N19@88,1",
  "N24@-61,1     3 N20@-47,32 N17@-89,1 N21@-47,-30",
  "N25@0,-39     3 N21@-47,-30 N18@0,-58 N22@47,-30",
  "N26@61,1      3 N22@47,-30 N19@88,1 N23@47,32",
  "N27@0,42      3 N23@47,32 N16@0,60 N20@-47,32"
)

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
    hepsi.herbiriİçin(çzg => {
        dez (x1, y1) = (çzg.n1.x, çzg.n1.y)
        dez (x2, y2) = (çzg.n2.x, çzg.n2.y)
        dez boy = karekökü(karesi(x2 - x1) + karesi(y2 - y1))
        dez (xr, yr) = (yarıçap / boy * (x2 - x1), yarıçap / boy * (y2 - y1))
        çzg.resim.sil
        çzg.resim = doğruÇiz(x1 + xr, y1 + yr, x2 - xr, y2 - yr)
    })
}
tanım kaçTane(düzenli: İkil = doğru) = {
    eğer (düzenli) f"${noktalar.boyu}%2d nokta ve ${çizgiler.boyu}%2d çizgi var"
    yoksa s"${noktalar.boyu} nokta ve ${çizgiler.boyu} çizgi var"
}

sınıf Düğmeler(kns: Sayı) {
    tanım kur() { // düğmeleri bir grid üzerine koyalım
        düğmelerinİlkSırası() // dört sıra düğmemiz var
        düğmelerinİkinciSırası() // nokta/çizgi ekleme komutları
        düğmelerinÜçüncüSırası() // nokta seçme komutları
        düğmelerinDördüncüSırası() // nokta devinim komutları
    }
    tanım düğmelereDeğdiMi(x: Kesir, y: Kesir) = {
        dez (x1, y1) = (kx - yarıçap, ky - yarıçap)
        // DİKKAT 4x3'lük alandan fazla düğme olursa bunu da büyüt
        dez (x2, y2) = (kx + 3 * dGrid + yarıçap, ky + 4 * dGrid + yarıçap)
        x1 < x && x < x2 && y1 < y && y < y2
    }
    dez dGrid = 40 // düğmeleri sol alt köşede bir gride yerleştirelim
    dez (kx, ky) = (0.9 * tuvalAlanı.x, 0.9 * tuvalAlanı.y) // sol alt köşe
    dez dBoyu = dGrid - 5
    dez (sıra1, sıra2, sıra3, sıra4) = (ky, ky + dGrid, ky + 2 * dGrid, ky + 3 * dGrid)
    dez (sütn1, sütn2, sütn3, sütn4) = (kx, kx + dGrid, kx + 2 * dGrid, kx + 3 * dGrid)
    // yeni nokta konumu
    tanım ynkx = { // rastgele olmazsa seçiliNoktalar küme metodları hata veriyor
        dez yeniZ = kx + 3 * dGrid + yarıçap
        rastgeleKesir(yeniZ, yeniZ + 2 * dGrid + yarıçap)
    }
    tanım ynky = {
        dez yeniZ = ky - yarıçap
        rastgeleKesir(yeniZ, yeniZ + 4 * dGrid + yarıçap)
    }
    den yardım = Resim.arayüz(ay.Tanıt("Yardım"))
    tanım yardımEt(x: Kesir, y: Kesir, m: Yazı) = {
        yardım = Resim.arayüz(ay.Tanıt(m))
        yardım.konumuKur(sütn1, sıra4 + dGrid)
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
                yoksa eğer (kümeTükendi) "Başka dışsal nokta ekleyemiyoruz"
                yoksa "Dışsal nokta ekle (çizge yüzeysel kalır)")
            düğme.kalemRenginiKur(mavi)
            düğme.boyamaRenginiKur(mavi)
            den yeniNokta = 0 // kk'nin kaçıncı kümesine bağlanacak bu yeni nokta?
            dez kk = kümeler(kns)
            satıryaz(kk.boyu + " dışsal nokta ekleyebilirsin")
            düğme.fareyeTıklayınca { (x, y) => // bu kareye her basışımızda yeni bir nokta ekleyelim
                dez msj = (ne: Yazı) => satıryaz(s"$ne var. Dışsal nokta ekleyemiyoruz artık!")
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
                    satıryaz("Başka dışsal nokta ekleyemiyoruz (şimdilik!)")
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
        d2a(); d2b(); d2c()
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
            düğme.boyamaRenginiKur(renkler.darkGreen)
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
        d3a(); d3b(); d3c()
    }
    tanım düğmelerinDördüncüSırası() {
        tanım d4a() = {
            dez düğme = kare(sütn1, sıra4, dBoyu)
            yardımıKur(düğme, "Seçili noktayı komşularının ağırlık merkezine götür")
            düğme.boyamaRenginiKur(gri)
            düğme.fareyeTıklayınca { (x, y) =>
                eğer (seçiliNoktalar.boyu == 0) satıryaz("Nokta seçimiKur ki komşularının ağırlık merkezine götürelim")
                yoksa seçiliNoktalar.işle(_.merkezeGit())
            }
        }
        tanım d4b() = {
            dez düğme = kare(sütn2, sıra4, dBoyu)
            yardımıKur(düğme, "Seçili noktanın komşularını yanına topla")
            düğme.boyamaRenginiKur(açıkGri)
            düğme.fareyeTıklayınca { (x, y) =>
                eğer (seçiliNoktalar.boyu == 0) satıryaz("Nokta seçimiKur ki komşularını çağıralım")
                yoksa seçiliNoktalar.işle(_.merkezeGetir())
            }
        }
        tanım d4c() = {
            dez düğme = kare(sütn3, sıra4, dBoyu)
            yardımıKur(düğme, "Noktaları küçült ya da büyült")
            düğme.boyamaRenginiKur(renkler.turquoise)
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
        d4a(); d4b(); d4c()
    }
}
baştan(KNS)
