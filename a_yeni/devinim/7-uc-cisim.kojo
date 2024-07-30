// http://ikojo.in/sf/ebMjXSV/7
// Fizikteki gerçek üç cisim problemi: https://www.youtube.com/watch?v=l2wnqlcOL9A
// Görünüşe göre, üçlü yıldız sistemleri var, ama çoğunlukla yakın dönen ikiz etrafında uzaktan dönen üçüncü olarak. 
// Yoksa işler çok karmaşıklaşıyor burada göreceğimiz gibi:
dez (yer, boy, ilkHız, kçKatsayısı, örnekleme) = (50, 5, 1.0, 100, 4)
dez fırçaBoyu = 0.6 // yörüngeyi çizer. Çizmemek için 0 yapın
dez (r1, r2, r3) = (kalemRengi(kırmızı) * boyaRengi(kırmızı), kalemRengi(mavi) * boyaRengi(mavi), kalemRengi(yeşil) * boyaRengi(yeşil))
dez (c1, c2, c3) = (r1 -> Resim.daire(boy), r2 -> Resim.daire(boy), r3 -> Resim.daire(boy))
silVeSakla()
tanım dürt = { dez r = rastgele(10); yaz(s"$r "); r } // başlangıç yerlerini biraz dürtelim ki farklı yörüngeler ortaya çıksın
çiz(götür(yer + dürt, yer + dürt) -> c1, götür(-yer + dürt, -yer + dürt) -> c2, götür(-yer + dürt, yer + dürt) -> c3)
den (dx1, dy1, dx2, dy2, dx3, dy3) = (ilkHız, 0.0, -ilkHız, 0.0, 0.0, 0.0)
tanım açı(p1: Nokta, p2: Nokta): Kesir = tanjantınAçısı(mutlakDeğer((p1.y - p2.y) / (p1.x - p2.x)))
tanım uzaklık(p1: Nokta, p2: Nokta): Kesir = karekökü(karesi(p1.x - p2.x) + karesi(p1.y - p2.y))
tanım dikey(köşegen: Kesir, açı: Kesir) = köşegen * sinüs(açı)
tanım yatay(köşegen: Kesir, açı: Kesir) = köşegen * kosinüs(açı)
tanım kuvvet(uzaklık: Kesir) = kçKatsayısı / kuvveti(enİrisi(boy*3, uzaklık), 2)
tanım dhız(p1: Point, p2: Point) = { // p1'in p2 üzerindeki etkisiyle hızdaki değişim
    dez a = açı(p1, p2)
    dez f = kuvvet(uzaklık(p1, p2))
    dez (fx, fy) = (yatay(f,a), dikey(f,a))
    (eğer (p1.x > p2.x) -fx yoksa fx, 
     eğer (p1.y > p2.y) -fy yoksa fy)
}
den adım = 0
canlandır {
    dez (p1, p2, p3) = (c1.konum, c2.konum, c3.konum)
    adım += 1
    eğer (fırçaBoyu > 0 && adım % örnekleme == 1) için ((p, c) <- Dizi((p1, r1), (p2, r2), (p3, r3))) { çiz(götür(p.x, p.y) * c -> Resim.daire(fırçaBoyu)) }
    için (p <- Dizi(p2, p3)) {
        dez (fx, fy) = dhız(p1, p)
        dx1 += fx; dy1 += fy
    }
    için (p <- Dizi(p3, p1)) {
        dez (fx, fy) = dhız(p2, p)
        dx2 += fx; dy2 += fy
    }
    için (p <- Dizi(p1, p2)) {
        dez (fx, fy) = dhız(p3, p)
        dx3 += fx; dy3 += fy
    }
    c1.konumuKur(p1.x + dx1, p1.y + dy1)
    c2.konumuKur(p2.x + dx2, p2.y + dy2)
    c3.konumuKur(p3.x + dx3, p3.y + dy3)
}