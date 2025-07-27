// Üst üste katlamanın süpriz sonucu...
//   http://ikojo.in/sf/GIvSHYW/3
//   https://onlinegdb.com/bYNzfW9qB

dez k1: Kesir = 1.0e-4 // kağıdın kalınlığı 0.1mm olsun: 10^-4 metre (1 santimlik tomarda 100 kağıt olsun)
dez k2: Kesir = 300 // 100 katlı gökdelenin boyu
dez k3: Kesir = 300000.0e3 // ayın ortalama uzaklığı 270bin kilometre
dez k4 = k3 * 60 * 8.5 // güneşin uzaklığı (aya 1 saniyede giden ışık, güneşe 8.5 dakikada gidiyor)
dez k5 = k4 * 60 * 24 * 365 * 4 / 8.5 // 4 ışık yılı mesafe
dez k6 = k5 * 2.5e6 / 4 // 2.5 milyon ışık yılı
dez k7 = k6 * 2e4 // 50 milyar ışık yılı

dez hedefler = Eşlem(
    k2 -> "gökdelenin tepesi",
    k3 -> "ay",
    k4 -> "güneş",
    k5 -> "sentauri",
    k6 -> "andromeda",
    k7 -> "evrenin ucu",
)

satıryaz(f"  ${1000 * k1}%1.1fmm kalınlığında bir kağıdı kaç kere katlarsak çok yükseklere")
satıryaz("  varır (ve o zaman kağıdın genişliği ne olur)?")
satıryaz("  " + "=" * 63)
den k = k1;
den gen = 0.25 // kağıdın kenar uzunluğu, genişlik
den katSayısı = 0
için (hedef <- hedefler.anahtarlar.dizine.sıralı) {
    yineleDoğruKaldıkça (k < hedef) {
        katSayısı += 1
        eğer (katSayısı % 2 == 1) gen /= 2
        k *= 2
    }
    satıryaz(f"  ${hedefler(hedef)} için $katSayısı kere (${gen}%.2gm)")
}
