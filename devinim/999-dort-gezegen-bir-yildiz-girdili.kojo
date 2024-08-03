// ingilizcesi: http://ikojo.in/sf/i7piLDz/5
dez fırçaBoyu = 2.3 // yörüngeyi çizen fırçanın kalınlığı. Çizmemek için 0 yapın
dez örnekleme = 11 // peryot. En az 1 olsun. Büyüdükçe yörünge noktaları seyrekleşir
durum sınıf Cisim(kütle: Kesir, ilkKonum: Nokta, hız: Yöney, res: Res) {
  dez r = götür(ilkKonum.x, ilkKonum.y) * res.rengi -> Resim.daire(res.çap); r.çiz
  tanım konum = r.konum
  tanım kaydır = r.konumuKur(konum.x + hız.x, konum.y + hız.y)
  tanım hızlandır(artış: Yöney) = hız.ekle(artış) // hızı ve yönünü ayarla
}
durum sınıf Res(çap: Kesir, c: Color) { dez rengi = kalemRengi(c) * boyaRengi(c) }
durum sınıf Yöney(den x: Kesir, den y: Kesir) { // konumu hız ve kuvvetin hem boy hem de yönünü gösterir (temsil eder)
  tanım ekle(v2: Yöney) = { x += v2.x; y += v2.y; bu }
  tanım katla(c: Kesir) = { x *= c; y *= c; bu }
  tanım boyunKaresi = x * x + y * y
  tanım küpüneBöl = katla(1.0/enİrisi(0.0001, boyu * boyunKaresi)) // sıfıra bölemeyiz
  tanım boyu = karekökü(boyunKaresi)
}
dez (konum, çap, ilkHız) = (40.0, 3.0, 2.0) // ilk konum
silVeSakla; yaklaşXY(0.3, 0.3, konum, -konum) // çizim yapmadan tuvali temizle ve ayarla
dez cisimler = Diz( // Yıldız(kırmızı), gezegenler (mavi ve turuncu) ve kuyruklu yıldızlar (yeşil ve mor)
  Cisim(1000, Nokta(0, 0), Yöney(0, 0), Res(6 * çap, kırmızı)),
  Cisim(2.0, Nokta(konum, 2 * konum), Yöney(ilkHız, -1.5 * ilkHız), Res(2 * çap, renkler.darkGreen)),
  Cisim(3.0, Nokta(4 * konum, 2 * konum), Yöney(ilkHız / 2, -ilkHız), Res(2 * çap, mavi)),
  Cisim(5.0, Nokta(-6 * konum, 2 * konum), Yöney(-ilkHız / 3, ilkHız), Res(3 * çap, mor)),
  Cisim(6.0, Nokta(0, -7 * konum), Yöney(-ilkHız, 0), Res(3 * çap, turuncu)),
)
// cisimlerin hızı her adımda değişecek. yatay ve dikey parçalarına ayırıyor ve hesabediyoruz:
tanım dHız(k1: Nokta, k2: Nokta, kütle2: Kesir) = { // k2 konumunda bulunan ve kütlesi kütle2 olan cisim k1 konumundaki cismin hızını nasıl etkiler
  Yöney(k2.x - k1.x, k2.y - k1.y) // bu yöney ile (yani bu doğrultuda) çeker
    .küpüneBöl.katla(kütle2) // çekimin gücü de ikinci kütlenin büyüklüğüne ve aradaki uzaklığın karesine bakar
}
dez bilgi = götür(tuvalAlanı.x + 80, tuvalAlanı.y + 120) -> Resim.yazı("0000", Yazıyüzü("JetBrains Mono", 100), siyah)
bilgi.çiz
den girdi = ""
dez adımSayısı = 500 // bu kadar adımdan sonra dur ve girdi oku
den adım = 0; // döngünün her bir turuna bir adım diyelim ve adımları sayalım
yap { // canlandır {
  zamanTut(s"$adımSayısı adım atmak")(yinele(adımSayısı) {
    eğer (girdi == "1" && fırçaBoyu > 0 && adım % örnekleme == 0)
    için (c <- cisimler)
      (götür(c.konum.x, c.konum.y) * c.res.rengi -> Resim.daire(fırçaBoyu)).çiz
    adım += 1
    bilgi.güncelle(adım)
    için (c <- cisimler) {
      için (öbürü <- cisimler eğer öbürü != c)
      c.hızlandır(dHız(c.konum, öbürü.konum, öbürü.kütle))
      c.kaydır
    }
  })(f"tuttu. $adım%5d")
  girdi = satıroku("Devam etmek için return tuşuna basın (yörüngeleri çizmek için 1 girin)")
} yineleDoğruKaldıkça (doğru)