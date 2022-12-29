
durum sınıf Kişi(no: Uzun, adı: Yazı, soyadı: Yazı)
durum sınıf Kurum(no: Uzun, adı: Yazı)

sınıf enBaştan {
    tür Etki[T] = Belki[T] // yeni - arama sonuç vermez belki de

    gizli tanım kişiAra(kimlik: Uzun): Etki[Kişi] = ???
    gizli tanım kurumAra(kim: Kişi): Etki[Kurum] = ???

    tanım kimliktenKurumBul(no: Uzun): Etki[Kurum] = 
        kurumAra(kişiAra(no)) // hata verdi
}
