// 1980li yıllarda televizyondaki Monty Hall adlı yarışma programında oynanan oyun
// Kapalı üç kapıdan birinin arkasında araba yani değerli bir ödül var
kojoÇalışmaSayfalıBakışaçısınıKur()
tanım ilkSeçim = rastgeleDiziden(1 |-| 3) // hangi kapıyı seçelim
tanım araba = rastgeleDiziden(1 |-| 3) // araba hangi kapının arkasında olsun?  
dez kapılar = Küme(1, 2, 3) // diğer iki kapının arkası boş (ya da keçi olsun)!
tanım sunucununAçtığıKapı(arabanınOlduğuKapı: Sayı, ilkTercihEdilenKapı: Sayı): Küme[Sayı] = 
    kapılar - arabanınOlduğuKapı - ilkTercihEdilenKapı // Sunucu arkası boş olan kapıyı (ya da kapılardan birini) açar
tanım örnekVerelim = {
    dez (a, s) = (araba, ilkSeçim)
    dez sunucuAçar = sunucununAçtığıKapı(a, s)
    dez mesaj = eğer (sunucuAçar.boyu == 2) s"${sunucuAçar.yazıYap(". ya da ")}" yoksa sunucuAçar.başı.yazıya
    satıryaz(s"Rastgele bir örnek:\n  Araba $a. kapının arkasında olsun.")
    satıryaz(s"  Seçtiğimiz kapı $s. kapı olsun.\n  Sunucunun açtığı kapı $mesaj. kapı olacak.")
    satıryaz(s"  Seçtiğimiz kapıda kalırsak ${eğer (a == s) "kazanırız" yoksa "kaybederiz"}.")
    satıryaz(s"  Öbür kapalı kapıya geçersek ${eğer (a == s) "kaybederiz" yoksa "kazanırız"}.")
}
tanım ikinciAdım = {
    tanım kal: İkil = araba == ilkSeçim // tercihimizde sabit kalırsak kazanır mıyız?
    tanım geç: İkil = sunucununAçtığıKapı(araba, ilkSeçim).boyu == 1 // öbür kapıya geçersek kazanır mıyız?
    tanım bolcaDeneyYapalım(deneySayısı: Sayı = 3000) = {
        dez kalarakKazanmaSayısı = (için (i <- 1 |-| deneySayısı eğer kal) ver (1)).boyu
        dez geçerekKazanmaSayısı = (için (i <- 1 |-| deneySayısı eğer geç) ver (1)).boyu
        satıryaz(s"  İlk seçtiğimiz kapıda kalınca $kalarakKazanmaSayısı kere kazandık.")
        satıryaz(s"  Öbür kapalı kapıya geçince $geçerekKazanmaSayısı kere kazandık.")
    }
    satıryaz("Önce hep kalarak, sonra da hep geçerek 3000'er kere oynayalım:")
    bolcaDeneyYapalım()
}
dez örnekVer = doğru
eğer (/*!*/doğru) örnekVerelim
yoksa ikinciAdım