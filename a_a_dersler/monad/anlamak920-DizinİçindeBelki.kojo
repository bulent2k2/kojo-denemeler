// #yükle işle-yöntemli
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = DizinİçindeBelki[T]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun)(örtük çç: İşletimBağlamı): Etki[Kurum] = için (
    kişi <- kişiAra(kimlik);
    kurum <- kurumAra(kişi)
  ) ver kurum
}

durum sınıf DizinİçindeBelki[T](ne: Dizin[Belki[T]]) yayar İşleYöntemleri[T, DizinİçindeBelki] {
  tanım işle[S](f: T => S): DizinİçindeBelki[S] = DizinİçindeBelki(ne.işle(_.işle(f)))
  tanım düzİşle[S](f: T => DizinİçindeBelki[S]): DizinİçindeBelki[S] = DizinİçindeBelki(
    ne.düzİşle {
      durum Biri(a) => f(a).ne
      durum Hiçbiri => Dizin(Hiçbiri)
    }
  )
}