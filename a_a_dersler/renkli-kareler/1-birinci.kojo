// kojo kullanım kılavuzundan

silVeSakla
hızıKur(hızlı)
yaklaş(0.5, 240, 160)
artalanıKur(Renk(107, 4, 189))
kalemRenginiKur(renksiz)
dez renkDizisi = Dizi(
  sarı, yeşil, pembe, kahverengi,
  siyah, gri, koyuGri, açıkGri,
  renksiz, beyaz, kırmızı, turuncu,
  mavi, mor, morumsu, camgöbeği
)
//beyazla renksiz'in farkına dikkat!

yineleDizinli(renkDizisi.boyu) { i =>
  boyamaRenginiKur(renkDizisi(i-1))
  kare(100); ileri(100)
  eğer(i % 4 == 0)
     konumuKur(100 * i / 4, 0)
}
