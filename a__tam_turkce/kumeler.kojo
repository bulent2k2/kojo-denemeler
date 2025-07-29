//#çalışmasayfası

dez k: Küme[Sayı] = Küme(1, 3, 6) //> dez k: Küme[Sayı] = Küme(1, 3, 6)
dez k2 = Küme(1, 6, 10) //> dez k2: Küme[Sayı] = Küme(1, 6, 10)
dez k3 = Küme(10, 5, 2, 4) //> dez k3: Küme[Sayı] = Küme(10, 5, 2, 4)
k.kuyruğu //> dez sonuç18: Küme[Sayı] = Küme(3, 6)
k.bileşim(k2).bileşim(k3).öbekli(2).herbiriİçin { x =>
    satıryaz(x)
} //> dez sonuç19: Birim = ()
k.bileşim(k2) //> dez sonuç20: Küme[Sayı] = Küme(1, 3, 6, 10)
k.kesişim(k2) //> dez sonuç21: Küme[Sayı] = Küme(1, 6)
k2(3) //> dez sonuç22: İkil = yanlış
k2(10) //> dez sonuç23: İkil = doğru