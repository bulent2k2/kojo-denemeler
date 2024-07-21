silVeSakla()
başlangıçNoktasıAltSolKöşeOlsun()

dez tuvalEni = tuvalAlanı.eni.sayıya
dez tuvalBoyu = tuvalAlanı.boyu.sayıya

den x = 0;  den y = 0
den dx = 1; den dy = 2
dez kenarSağ = 100; dez kenarÜst = 180

tanım evreyiGüncelle() {
   x += dx
   y += dy
   eğer (x >= kenarSağ || x <= 0) dx *= -1
   eğer (y >= kenarÜst || y <= 0) dy *= -1
}

tanım evreyiGöster(tuval: TuvalÇizim) {
    tuval.boya(0, 0, 0, 30)
    tuval.fırçasız()
    tuval.dikdörtgen(0, 0, tuvalEni, tuvalBoyu)
    tuval.fırça(sarı)
    tuval.fırçaAğırlığı(4)
    tuval.nokta(x, y)
    tuval.fırçaAğırlığı(2)
    tuval.dikdörtgen(0, 0, kenarSağ, kenarÜst)
    tuval.fırça(kırmızı)
    tuval.yay(200, 100, 100, 50, piSayısı/2, 3*piSayısı/2)
}

canlandırTuvalÇizimle { tuval =>
    evreyiGüncelle()
    evreyiGöster(tuval)
}
