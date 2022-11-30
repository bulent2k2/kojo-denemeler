// bellekGeri ve bellekİleri nesneleri

sınıf Bellek {
  durum sınıf BirAnı(dez yerler: Yerler, dez çizgiler: ÇizgiKümesi)
  tür Yerler = Eşlek[Nokta, Yer]
  durum sınıf Yer(dez x: Kesir, dez y: Kesir) {
    baskın tanım toString = s"Y(${yuvarla(x).sayıya},${yuvarla(y).sayıya})" // ttodo: toString??
  }
  tür NoktaKümesi = Küme[Nokta]
  tür ÇizgiKümesi = Küme[Çizgi]
  
  tanım koy(hepsi: NoktaKümesi, çizgiler: ÇizgiKümesi) = bellek.koy(
    BirAnı(
      hepsi.işle(n => (n, Yer(n.x, n.y))).eşleğe,
      çizgiler
    )
  )
  tanım al(): BirAnı = bellek.al()
  tanım boşMu() = boyu() == 0
  tanım boyu() = bellek.tane
  tanım boşalt() = yineleDoğruKaldıkça (!boşMu()) al()
  gizli dez bellek = Yığın.boş[BirAnı]
}
// gerideki anıları tutan bellek:
dez bellekGeri = yeni Bellek
// geri gittikten sonra ileri gidebilmek için bellek:
dez bellekİleri = yeni Bellek

