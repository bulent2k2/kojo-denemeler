// özyinelemeye güzel bir örnek

tanım koch(k: Kaplumbağa, düzey: Sayı, boy: Kesir): Birim = 
    eğer (düzey == 0) k.ileri(boy) 
    yoksa {
        tanım küçüğüm = koch(k, düzey-1, boy/3)
        küçüğüm
        k.sol(60)
        küçüğüm
        k.sağ(120)
        küçüğüm
        k.sol(60)
        küçüğüm
    }

silVeSakla
dez k = yeniKaplumbağa(-100,-100)
tanım kenar = koch(k, 3, 300)

k.hızıKur(orta)
dez renk = Renkler.altınbaşak
k.boyamaRenginiKur(renk)
k.kalemRenginiKur(renk)
kenar
tanım dönVeÇiz = {
    k.sağ(120)
    kenar
}
yinele(2) { dönVeÇiz }