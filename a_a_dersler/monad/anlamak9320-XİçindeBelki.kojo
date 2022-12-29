
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

özellik İşleyici[F[_]] { // Functor
  tanım işle[A, B](fa: F[A])(f: A => B): F[B]  // map
}
özellik Düzİşlem[F[_]] { // FlatMap
  tanım düzİşle[A, B](fa: F[A])(f: A => F[B]): F[B]
}
özellik Uygulayıcı[F[_]] /* yayar İşleyici[F] */ { // Applicative
  tanım arı[A](a: A): F[A] // pure
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
getir scala.concurrent.ExecutionContext.Implicits.global
örtük dez Gİ = yeni Gelecekİşleyici
örtük dez GDİ = yeni GelecekDüzİşlem

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
  tanım map[S](f: T => S)(örtük fx: İşleyici[X]): XİçindeBelki[S, X] =
    XİçindeBelki(fx.işle(ne)(_.işle(f)))  // X
  
  tanım flatMap[S](f: T => XİçindeBelki[S, X])(örtük fmx: Düzİşlem[X], ax: Uygulayıcı[X]): XİçindeBelki[S, X] =
    XİçindeBelki(  // Y
      fmx.düzİşle(ne) {
        durum Biri(a) => f(a).ne
        durum Hiçbiri => ax.arı(Hiçbiri)
      }
    )
}
