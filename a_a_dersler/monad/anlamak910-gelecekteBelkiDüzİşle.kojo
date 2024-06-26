// #yükle işle-yöntemli
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = GelecekteBelki[T]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun)(örtük çç: İşletimBağlamı): Etki[Kurum] = için (
    kişi <- kişiAra(kimlik);
    kurum <- kurumAra(kişi)
  ) ver kurum
}

durum sınıf GelecekteBelki[T](ne: Gelecek[Belki[T]]) yayar İşleYöntemleri[T, GelecekteBelki] {
  örtük dez ib: İşletimBağlamı = İşletimBağlamı.küresel
  tanım işle[S](f: T => S): GelecekteBelki[S] = GelecekteBelki(ne.işle(_.işle(f)))
  tanım düzİşle[S](f: T => GelecekteBelki[S]): GelecekteBelki[S] = 
    GelecekteBelki(
      ne.düzİşle {
        durum Biri(a) => f(a).ne
        durum Hiçbiri => Gelecek.başarılı(Hiçbiri)
      }
    )
}