// yerçekimi farklı çalışsaydı ne olurdu?
silVeSakla()

dez n1 = Nokta(-200, -200)
dez n2 = Nokta(300, 0)
dez n3 = Nokta(0, 200)

dez k1 = yeniKaplumbağa(n1.x, n1.y); çiz( götür(n1.x, n1.y) -> Resim.daire(10))
dez k2 = yeniKaplumbağa(n2.x, n2.y); çiz( götür(n2.x, n2.y) -> Resim.daire(10))
dez k3 = yeniKaplumbağa(n3.x, n3.y); çiz( götür(n3.x, n3.y) -> Resim.daire(10))

den h1 = 1.0
den h2 = 1.0
den h3 = 1.0

yaklaş(0.8)
// davran komuduyla çalışan komut dizisi arkadan gelen komutlarla paralel çalışır
// bu sayede iki kaplumbağa eş zamanlı hareket ederler
k1.davran { k => 
    k.kalemRenginiKur(Renk(0, 0, 255, 120))
    k.kalemKalınlığınıKur(4)
    yineleDoğruysa(doğru) {
        dez (kuvvet, yön) = topla(k, k3, k2)
        k.noktayaDön(yön)
        h1 = h1 + kuvvet
        k.ileri(kuvvet)
    } 
}

k2.davran { o =>
    o.kalemRenginiKur(Renk(0, 255, 0, 120))
    yineleDoğruysa(doğru) {
        dez (kuvvet, yön) = topla(o, k3, k1)
        o.noktayaDön(yön)
        o.ileri(kuvvet)
    }
}

k3.davran { o => 
    o.kalemRenginiKur(Renk(255, 0, 0, 120))
    yineleDoğruysa(doğru) {
        dez (kuvvet, yön) = topla(o, k1, k2)
        o.noktayaDön(yön)
        o.ileri(kuvvet)
    }
}

tanım ortası(k1: Kaplumbağa, k2: Kaplumbağa) = Nokta( (k1.konum.x + k2.konum.x)/2, (k1.konum.y + k2.konum.y)/2) 
tanım topla(o: Kaplumbağa, o1: Kaplumbağa, o2: Kaplumbağa): (Kesir, Nokta) = {
    dez u1 = o.uzaklık(o1)
    dez u2 = o.uzaklık(o2)
    (
        enİrisi(1, 100 * (1/u1 + 1/u2)),
        ortası(o1, o2)
    )
}
