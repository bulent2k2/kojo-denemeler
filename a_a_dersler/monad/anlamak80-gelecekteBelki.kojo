
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = GelecekteBelki[T]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun)(örtük çç: İşletimBağlamı)
    : Etki[Kurum] = için (
      kişi <- kişiAra(kimlik);
      kurum <- kurumAra(kişi)
    ) ver kurum
}

durum sınıf GelecekteBelki[T](ne: Gelecek[Belki[T]]) {
    tanım map[S](f: T => S): GelecekteBelki[S] = ???
    tanım flatMap[S](f: T => GelecekteBelki[S]): GelecekteBelki[S] = ???
}