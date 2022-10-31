// Gaz lambası yazılımcığımızı kaplumbağanın resim yöntemlerini ve
// geçiş nesnesini kullanarak yeniden yazalım

silVeSakla()
artalanıKur(renkler.darkSlateBlue) // eflatuna yakın koyu mavi

val alev = Resim {
    boyamaRenginiKur(cm.linearGradient(0, 0, kırmızı, 0, 130, sarı))
    kalemRenginiKur(sarı)
    kalemKalınlığınıKur(3)
    sol(45)
    sağ(90, 100)
    sağ(90)
    sağ(90, 100)
}

val lamba = Resim {
    setFillColor(cm.linearGradient(0, 10, kırmızı, 0, -25, kahverengi))
    kalemKalınlığınıKur(2)
    kalemRenginiKur(siyah)
    sol(120)
    sağ(60, 100)
    sağ(180)
    sağ(30)
    sol(120, 115)
    sağ(180)
    sağ(30)
    sağ(60, 100)
}

çiz(lamba)

def büyütme(dizi: Dizi[Kesir]) = dizi(0)

def alevlendir(dizi: Dizi[Kesir]) = {
    val resim = büyüt(büyütme(dizi)) -> alev
    resim.p // todo
}

/* todo: Çeviri
    Transition Geçiş
    easing kolayca
    QuadInOut DörtlüGirdiÇıktı
    animSeq canlandırmaDizisi
    repeatedForever sonsuzYineleme
    */ 
val canlandırma = Transition(1, Dizi(1), Dizi(0.8), easing.QuadInOut, alevlendir, doğru)
val canlandırma2 = animSeq(canlandırma, canlandırma.reversed)
oynat(canlandırma2.repeatedForever)
