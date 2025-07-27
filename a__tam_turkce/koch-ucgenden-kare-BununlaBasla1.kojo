// özyinelemeye güzel bir örnek

tanım koch(düzey: Sayı, boy: Kesir) {
    eğer (düzey == 0) ileri(boy)
    yoksa {
        koch(düzey - 1, boy / 3)
        sol(60)
        koch(düzey - 1, boy / 3)
        sağ(120)
        koch(düzey - 1, boy / 3)
        sol(60)
        koch(düzey - 1, boy / 3)
    }
}

silVeSakla
hızıKur(orta)
konumuKur(-150, -100)

sağ(30)
koch(3, 300)
dez gerisiniÇiz = yanlış // doğru yapın
eğer (gerisiniÇiz) {
    sağ(120)
    koch(3, 300)
    sağ(120)
    koch(3, 300)
    sağ(120)
}