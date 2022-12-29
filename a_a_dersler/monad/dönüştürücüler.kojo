özellik Kapsar[F[_]] { // Wrapper != Applicative
  tanım arı[A](a: A): F[A] // pure
}
örtük nesne GelecekKapsar yayar Kapsar[Gelecek] {
  tanım arı[A](a: A): Gelecek[A] = Gelecek.başarılı(a)
}

özellik İşler[F[_]] { // Functor
  tanım işle[A, B](fa: F[A])(f: A => B): F[B]  // map
}
sınıf Gelecekİşler(örtük dez i: İşletimBağlamı) yayar İşler[Gelecek] {
  tanım işle[A, B](fa: Gelecek[A])(f: A => B): Gelecek[B] = fa.işle(f)(i)
}

özellik Düzİşler[F[_]] { // FlatMap
  tanım düzİşle[A, B](fa: F[A])(f: A => F[B]): F[B]
}
sınıf GelecekDüzİşler(örtük dez i: İşletimBağlamı) yayar Düzİşler[Gelecek] {
  tanım düzİşle[A, B](fa: Gelecek[A])(f: A => Gelecek[B]): Gelecek[B] = fa.düzİşle(f)(i)
}

özellik Monad1[F[_]] {
  tanım arı[A](a: A): F[A] // pure
  tanım düzİşle[A, B](fa: F[A])(f: A => F[B]): F[B]
}
sınıf GelecekMonadı1(örtük dez i: İşletimBağlamı) yayar Monad1[Gelecek] {
  tanım arı[A](a: A): Gelecek[A] = Gelecek.başarılı(a)
  tanım düzİşle[A, B](fa: Gelecek[A])(f: A => Gelecek[B]): Gelecek[B] = fa.düzİşle(f)(i)
}

özellik Monad[F[_]] {
  tanım arı[A](a: A): F[A] // pure
  tanım işle[A, B](fa: F[A])(f: A => B): F[B]
  tanım düzİşle[A, B](fa: F[A])(f: A => F[B]): F[B]
}
sınıf GelecekMonadı(örtük dez i: İşletimBağlamı) yayar Monad[Gelecek] {
  tanım arı[A](a: A): Gelecek[A] = Gelecek.başarılı(a)
  tanım işle[A, B](fa: Gelecek[A])(f: A => B): Gelecek[B] = fa.işle(f)(i)  
  tanım düzİşle[A, B](fa: Gelecek[A])(f: A => Gelecek[B]): Gelecek[B] = fa.düzİşle(f)(i)
}

getir scala.concurrent.ExecutionContext.Implicits.global
örtük dez Gİ = yeni Gelecekİşler
örtük dez GDİ = yeni GelecekDüzİşler
örtük dez GM1 = yeni GelecekMonadı1
örtük dez GM = yeni GelecekMonadı
