// özyinelemeye güzel bir örnek

tanım koch(k: Kaplumbağa, düzey: Sayı, boy: Kesir): Birim =
    eğer (düzey <= 0) k.ileri(boy)
    yoksa {
        den çeyrek = 1
        tanım duruş = {
            eğer (düzey > 2)
                burdaDur(f"Kenarın $çeyrek. çeyreğini çizdik! düzey: $düzey doğrultu: ${k.doğrultu}%.0f")
            çeyrek += 1
        }
        koch(k, düzey - 1, boy / 3)
        duruş
        k.sol(60)
        koch(k, düzey - 1, boy / 3)
        duruş
        k.sağ(120)
        koch(k, düzey - 1, boy / 3)
        duruş
        k.sol(60)
        koch(k, düzey - 1, boy / 3)
        duruş
    }

silVeSakla
çıktıyıSil
dez k = yeniKaplumbağa(0, -100)
// k.gizle
k.hızıKur(orta)
dez renk = Renkler.altınbaşak
k.boyamaRenginiKur(renksiz) // renk yapıp yine çalıştır
k.kalemRenginiKur(renk)

k.konumuKur(-150, -100)
k.sağ(30)
koch(k, 3, 300)
k.sağ(120)
burdaDur("İlk kenarı çizdik!")
koch(k, 3, 300)
k.sağ(120)
burdaDur("İkinci kenarı çizdik!")
koch(k, 3, 300)
burdaDur("Üçüncü kenarı çizdik! Hizaya girmeden hemen önce.")
k.sağ(120)
satıryaz("Bitti.")