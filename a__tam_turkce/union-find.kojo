// #çalışma sayfası
// https://www.coursera.org/learn/algorithms-part1/assignment-submission/SCxqJ/interview-questions-union-find-ungraded/attempt
dez deneme = 0
// yukarıda ilk satırdaki çalışma ve sayfası sözcükleri arasındaki boşluğu silip tekrar çalıştır!
durum sınıf Öbekle(tane: Sayı) { // Öbek Bulma tarifi/algoritması (UnionFind algorithm)
    dez üstü = EsnekDizik.diziden(0 |- tane) // üstü(i) = i, yani başlangıçta üstü kendisi olsun
    dez boy = EsnekDizik.doldur(tane)(1) // bu ve altındaki elemanların sayısı, başta tek bir
    den hepsiBağlıMı = eğer (tane < 2) doğru yoksa yanlış
    dez büyük = EsnekDizik.diziden(0 |- tane) // bu ağacın içindeki en büyük eleman
    tanım yaz = üstü.ikileKonumla.herbiriİçin {
        durum (u, e) => satıryaz(f"elem $e: ustu -> $u kökü=${kök(e)} boyu=${boy(e)} büyük=${bul(e)}")
    }
    tanım bul(elem: Sayı) = büyük(kök(elem))
    gizli tanım belirtme(elem: Sayı) = belirt(elem >= 0 && elem < tane, "Yanlış eleman!")
    gizli tanım kök(elem: Sayı): Sayı = { // 
        belirtme(elem)
        den s = elem
        yineleDoğruKaldıkça (s != üstü(s)) {
            s = üstü(s)
        }
        s
    }
    tanım bağlıMı(a: Sayı, b: Sayı) = kök(a) == kök(b)
    tanım bağla2(a: Sayı, b: Sayı)(yaz2: İkil = yanlış) = {
        eğer (!hepsiBağlıMı) {
            eğer (bağla(a, b)) eğer (yaz2) yaz
        }
        yoksa satıryaz("Hepsi zaten bağlı")
    }
    tanım bağla(a: Sayı, b: Sayı): İkil = eğer (hepsiBağlıMı) { 
        satıryaz("Hepsi zaten bağlı!")
        yanlış
    }
    yoksa {
        dez (ka, kb) = (kök(a), kök(b))
        eğer (ka == kb) {
            satıryaz("ikisi zaten bağlı")
            geriDön yanlış
        }
        eğer (boy(ka) < boy(kb)) {
            üstü(ka) = kb
            boy(kb) += boy(ka)
            eğer (boy(kb) == tane) {
                eğer (!hepsiBağlıMı) satıryaz("Hepsi bağlandı!")
                hepsiBağlıMı = doğru
            }
            eğer (büyük(ka) > büyük(kb)) büyük(kb) = büyük(ka)
        }
        yoksa {
            üstü(kb) = ka
            boy(ka) += boy(kb)
            eğer (boy(ka) == tane) {
                eğer (!hepsiBağlıMı) satıryaz("Hepsi bağlandı!")
                hepsiBağlıMı = doğru
            }
            eğer (büyük(kb) > büyük(ka)) büyük(ka) = büyük(kb)
        }
        doğru
    }
    tanım öbekSayısı = eğer (hepsiBağlıMı) 1 yoksa
        (üstü.işle(kök(_)).ikileKonumla.ele { durum (k, e) => k == e }).boyu
}

dez o0 = Öbekle(0)
o0.hepsiBağlıMı

dez ufakGirdi = Dizi((4, 3), (3, 8), (6, 5), (0, 0), (9, 4), (2, 1), (8, 9), (5, 0), (7, 2), (6, 1), (1, 0), (6, 7), (4, 6), (6, 4))
// 10 tane eleman var:
dez o1 = Öbekle(10)
çıktıyıSil()
satıryaz("Başlayalım")
için ((a, b) <- ufakGirdi) {
    satıryaz(s"$a ile $b bağlanıyor")
    o1.bağla2(a, b)(doğru)
    satıryaz(s"${o1.öbekSayısı} öbek var")
}
satıryaz("Bitti")