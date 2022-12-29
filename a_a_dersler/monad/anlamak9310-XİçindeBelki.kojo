
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = XİçindeBelki[T, Gelecek]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun): Etki[Kurum] = için (
    kişi <- kişiAra(kimlik); // hata: örtük değerler eksik
    kurum <- kurumAra(kişi)
  ) ver kurum
}

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

// https://typelevel.org/cats/typeclasses/İşleyici.html
özellik İşleyici[F[_]] { // Functor
  tanım işle[A, B](fa: F[A])(f: A => B): F[B]  // map
}
özellik Düzİşlem[F[_]] { // FlatMap
  tanım düzİşle[A, B](fa: F[A])(f: A => F[B]): F[B]
}
özellik Uygulayıcı[F[_]] yayar İşleyici[F]{ // Applicative
  tanım arı[A](a: A): F[A] // pure
}
