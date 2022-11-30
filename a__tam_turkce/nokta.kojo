
den nkn = 0 // nokta kimlik numarası sayacı, bir sonraki nokta bunu kullanacak
durum sınıf Nokta(den x: Kesir, den y: Kesir) yayar Eşsizlik {
    // Eşsizlik özelliği aynı koordinatlardaki iki noktanın Küme içinde birbirinden ayırılabilmesi için gerekli
    dez no = nkn // bu noktanın kimlik numarası
    nkn += 1 // bir sonraki noktanın kimlik numarası
    // Eşsizlik özelliğinin soyut yöntemini bizim tanımlamamız gerekli
    // Anımsatma: iki nesnenin kıymaKodu farklıysa, nesneler de farklı görünür
    tanım kıymaKodu = no.kıymaKodu

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
        r.fareyiSürükleyince { (mx, my) => { yeniKonum(yuvarla(mx), yuvarla(my)); çizelim(çizgiler) } }
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
