silVeSakla
// hızıKur(hızlı)
canlandırmaHızınıKur(3) // en hızlısı 1 = çokHızlı. 10 = hızlı, 100 = orta, 1000 = yavaş
yaklaş(0.5, 360, 320)
artalanıKur(Renk(107, 4, 189))
kalemRenginiKur(renksiz)
dez renkDizisi: Dizi[Boya] = Dizi(
  sarı, yeşil, pembe, kahverengi,
  siyah, gri, koyuGri, açıkGri,
  renksiz, beyaz, kırmızı, turuncu,
  mavi, mor, morumsu, camgöbeği,
  renkler.aliceBlue,
  renkler.hotpink,
  renkler.darkTurquoise,
  renkler.yellowGreen,
  renkler.antiqueWhite,
  renkler.wheat, renkler.whiteSmoke, renkler.yellowGreen, renkler.gainsboro,
  renkler.ghostWhite, renkler.gold, renkler.goldenrod, renkler.greenYellow,
  renkler.beige, renkler.bisque, renkler.blanchedAlmond, renkler.blueViolet, renkler.burlyWood,
  rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk,
  rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk, rastgeleRenk
)
//beyazla renksiz'in farkına dikkat!
dez boy = 100
dez sütun = 7
yineleDizinli(renkDizisi.boyu) { i =>
  boyamaRenginiKur(renkDizisi(i-1))
  kare(boy); ileri(boy)
  eğer(i % sütun == 0)
     konumuKur(boy * i / sütun, 0)
}
/* Diğer renkeri görmek istersen
aşağıda boş bir satıra 'renkler.'
yazdıktan sonra Kontrol tuşunu basılı
tutup büyük boşluk tuşuna basıver.
Adı d harfiyle başlayan renkleri
bulmak istersen renkler.d yazıp
Kontrol-Boşluk tuşuna bas */
// Aşağıdaki iki satırı kullanabilirsin
// Ama önce baştaki çift taksimi sil!
//  renkler.d
//  renkler