// tek hata kaldı
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
  tür Etki[T] = Gelecek[Belki[T]]

  gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
  gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???
  
  tanım kimliktenKurumuBul(kimlik: Uzun)(örtük çç: İşletimBağlamı): Etki[Kurum] =
    için (
      kişi <- kişiAra(kimlik);
      kurum <- kurumAra(kişi) // kişi türü uyumsuz
    ) ver kurum
}