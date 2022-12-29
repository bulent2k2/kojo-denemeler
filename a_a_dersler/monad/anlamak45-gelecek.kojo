
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = Gelecek[Belki[T]] // veri tabanına eşzamansız çağrı yapılır

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

  tanım kimliktenKurumuBul(kimlik: Uzun): Etki[Kurum] =
    için (
      kişi <- kişiAra(kimlik);  // hata verdi. İşletimBağlamı eksik
      kurum <- kurumAra(kişi)   // hata verdi. Tür uymadı
    ) ver kurum
}