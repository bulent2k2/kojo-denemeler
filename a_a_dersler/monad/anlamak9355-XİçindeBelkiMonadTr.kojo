// #yükle dönüştürücüler

durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = XİçindeBelki[T, Gelecek]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun): Etki[Kurum] = için (
    kişi <- kişiAra(kimlik);  // todo: hata! stackoverflow'a
    kurum <- kurumAra(kişi)
  ) ver kurum
}

durum sınıf XİçindeBelki[T, X[_]](ne: X[Belki[T]])(örtük mx: Monad[X]) yayar İşleYöntemleri[T, XİçindeBelki]{
  tür BU[S] = XİçindeBelki[S, X]
  tanım işle[S](f: T => S): BU[S] = XİçindeBelki(mx.işle(ne)(_.işle(f)))
  tanım düzİşle[S](f: T => BU[S]): BU[S] = XİçindeBelki(
    mx.düzİşle(ne) {
      durum Biri(a) => f(a).ne
      durum Hiçbiri => mx.arı(Hiçbiri)
    }
  )
  //tanım map[S](f: T => S): BU[S] = işle(f)  
  //tanım flatMap[S](f: T => BU[S]): BU[S] = düzİşle(f)
}

özellik İşleYöntemleri[B, A[_,_]] {
    tanım map[C](f:      B => C): A[C,_] = işle(f)
    tanım işle[C](işlev: B => C): A[C,_]

    tanım flatMap[C](f:     B => A[C,_]): A[C,_] = düzİşle(f)
    tanım düzİşle[C](işlev: B => A[C,_]): A[C,_]
}