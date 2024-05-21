// için deyişinden önce tanımlar gerekiyor
özellik İşleyici[F[_]] {
  tanım işle[A, B](fa: F[A])(f: A => B): F[B]
}
özellik Düzİşlem[F[_]] {
  tanım düzİşle[A, B](fa: F[A])(f: A => F[B]): F[B]
}
özellik Uygulayıcı[F[_]] {
  tanım arı[A](a: A): F[A]
}
sınıf Gelecekİşleyici(örtük dez i: İşletimBağlamı) yayar İşleyici[Gelecek] {
  tanım işle[A, B](fa: Gelecek[A])(f: A => B): Gelecek[B] = fa.işle(f)(i)
}
sınıf GelecekDüzİşlem(örtük dez i: İşletimBağlamı) yayar Düzİşlem[Gelecek] {
  tanım düzİşle[A, B](fa: Gelecek[A])(f: A => Gelecek[B]): Gelecek[B] = fa.düzİşle(f)(i)
}
örtük nesne GelecekUygulayıcı yayar Uygulayıcı[Gelecek] {
  tanım arı[A](a: A): Gelecek[A] = Gelecek.başarılı(a)
}
örtük dez _ibk = İşletimBağlamı.küresel
örtük dez _gi = yeni Gelecekİşleyici
örtük dez _gdi = yeni GelecekDüzİşlem

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
  tanım map[S](f: T => S)(örtük iş: İşleyici[X]): XİçindeBelki[S, X] =
    XİçindeBelki(iş.işle(ne)(_.işle(f)))
  
  tanım flatMap[S](f: T => XİçindeBelki[S, X])(örtük diş: Düzİşlem[X], ax: Uygulayıcı[X]): XİçindeBelki[S, X] =
    XİçindeBelki(
      diş.düzİşle(ne) {
        durum Biri(a) => f(a).ne
        durum Hiçbiri => ax.arı(Hiçbiri)
      }
    )
}
