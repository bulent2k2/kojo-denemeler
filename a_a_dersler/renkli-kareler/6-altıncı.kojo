silVeSakla
hızıKur(hızlı)
yaklaş(0.5, 240, 160)
artalanıKur(Renk(107, 4, 189))
kalemRenginiKur(renksiz)
// 20. satır hata veriyor. Onarmak için dizini türünü açıkça belirlemek yetecek.
// Derleyiciler de hata yapar (;-) Mükemmellik için Evren'in sonunu beklemek mi gerek?
dez renkDizisi /* : Dizi[Boya] */ = Dizi(
  sarı, yeşil, pembe, kahverengi,
  siyah, gri, koyuGri, açıkGri,
  renksiz, beyaz, kırmızı, turuncu,
  mavi, mor, morumsu, camgöbeği,
  renkler.aliceBlue,
  renkler.hotpink,
  renkler.darkTurquoise,
  renkler.yellowGreen
)
//beyazla renksiz'in farkına dikkat!
dez boy = 100
yineleDizinli(renkDizisi.boyu) { i =>
  boyamaRenginiKur(renkDizisi(i-1))
  kare(boy); ileri(boy)
  eğer(i % 4 == 0)
     konumuKur(boy * i / 4, 0)
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
