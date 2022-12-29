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

durum sınıf XİçindeBelki[T, X[_]](ne: X[Belki[T]]) {
  tanım map[S](f: T => S)(örtük iş: İşler[X]): XİçindeBelki[S, X] =
    XİçindeBelki(iş.işle(ne)(_.işle(f)))
  
  tanım flatMap[S](f: T => XİçindeBelki[S, X])(örtük monad: Monad1[X]): XİçindeBelki[S, X] =
    XİçindeBelki(
      monad.düzİşle(ne) {
        durum Biri(a) => f(a).ne
        durum Hiçbiri => monad.arı(Hiçbiri)
      }
    )
}