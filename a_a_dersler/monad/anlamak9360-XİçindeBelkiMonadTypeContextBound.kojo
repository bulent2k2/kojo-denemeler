// #yükle dönüştürücüler

durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = XİçindeBelki[T, Gelecek]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun): Etki[Kurum] = için (
    kişi <- kişiAra(kimlik);
    kurum <- kurumAra(kişi)
  ) ver kurum
}

durum sınıf XİçindeBelki[T, X[_]: Monad](ne: X[Belki[T]]) {
  gizli dez mx = implicitly[Monad[X]] // ttodo
  tanım map[S](f: T => S): XİçindeBelki[S, X] = // ttodo
    XİçindeBelki(mx.işle(ne)(_.işle(f)))
  
  tanım flatMap[S](f: T => XİçindeBelki[S, X]): XİçindeBelki[S, X] = // ttodo
    XİçindeBelki(
      mx.düzİşle(ne) {
        durum Biri(a) => f(a).ne
        durum Hiçbiri => mx.arı(Hiçbiri)
      }
    )
}