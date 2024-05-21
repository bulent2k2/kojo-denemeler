// üç kasıtlı hata var -- üç tanımsız tür var
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = XİçindeBelki[T, Gelecek] // Gelecek[Belki[T]]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun): Etki[Kurum] = için (
    kişi <- kişiAra(kimlik);
    kurum <- kurumAra(kişi)
  ) ver kurum
}
// eksik yöntemleri örtük girdilerle tamamlayalım. üç yeni tür gerek
durum sınıf XİçindeBelki[T, X[_]](ne: X[Belki[T]]) {
  tanım map[S](f: T => S)(örtük fx: İşleyici[X]): XİçindeBelki[S, X] =
    XİçindeBelki(fx.işle(ne)(_.işle(f)))  // X
  
  tanım flatMap[S](f: T => XİçindeBelki[S, X])
    (örtük fmx:Düzİşlem[X], ax: Uygulayıcı[X]): XİçindeBelki[S, X] =
    XİçindeBelki(  // Y
      fmx.düzİşle(ne) {
        durum Biri(a) => f(a).ne
        durum Hiçbiri => ax.arı(Hiçbiri)
      }
    )
}