//silVeSakla()
//görünür()
kalemKalınlığınıKur(1)
canlandırmaHızınıKur(20)
var toplamAdım = 0.0
// kalemRenginiKur(renkler.darkSeaGreen)  // soluk yeşil
// kalemRenginiKur(Color(0, 35, 180)) // lacivert
// kalemRenginiKur(Color(116, 23, 219))  // mor
kalemiKaldır(); ev(); kalemiİndir()
yinele(10000) {
    sağ(360.0 * rastgele)
    val adım = 10.0 * rastgele
    toplamAdım += adım
    ileri(adım)
    //kalemRenginiKur(rastgeleRenk)
}
val açı = 180 * tanjantınAçısı(mutlakDeğer(konum.y / konum.x)) / piSayısı
val uzaklık = karekökü(karesi(konum.x) + karesi(konum.y))
val toplamAçı =
    if (konum.x > 0) if (konum.y > 0) açı else 360 - açı
    else if (konum.y > 0) 180 - açı else 180 + açı
satıryaz(s"Kaplumbağa'nın konumu: x=${yuvarla(konum.x)} y=${yuvarla(konum.y)}")
satıryaz(s"Toplam adım          : ${yuvarla(toplamAdım)}")
satıryaz(s"Merkezden uzaklık    : ${yuvarla(uzaklık)}")
satıryaz(s"Toplam açı           : ${yuvarla(toplamAçı)}")