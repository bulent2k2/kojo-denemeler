sınıf Bellek {
  durum sınıf BirAnı(dez yerler: Yerler)
  tür Yerler = Eşlek[Nokta, Yer]
  durum sınıf Yer(dez x: Kesir, dez y: Kesir)
  tür NoktaKümesi = Küme[Nokta]
  
  tanım koy(hepsi: NoktaKümesi) = bellek.koy(
    BirAnı(hepsi.işle(n => (n, Yer(n.x, n.y))).eşleğe)
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

