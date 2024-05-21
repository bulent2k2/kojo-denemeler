// ışıkGeçirmeme en az 0 en çok da 255 olmalı. 
// 0 yani tamamen saydam, şeffaf ya da renksiz yapar
// 255 ise tamamen kesif yani hiç ışık geçirmez yapar
dez ışıkGeçirmeme = 150
// Renk(kırmızı, yeşil, mavi, kesiflik miktarı)
dez kırmızı = Renk(255, 0, 0, ışıkGeçirmeme)
dez yeşil = Renk(0, 255, 0, ışıkGeçirmeme)
dez mavi = Renk(0, 110, 255, ışıkGeçirmeme)
dez sarı = Renk(255, 255, 0, ışıkGeçirmeme)
dez siyah = Renk(0, 0, 0, ışıkGeçirmeme)
silVeSakla
dez yarıÇap = 100
dez pen = kalemRengi(gri)
tanım daire(r: Renk) = boyaRengi(r) * pen -> Resim.daire(yarıÇap)
çiz(
  daire(kırmızı),
  götür(yarıÇap, 0) -> daire(sarı),
  götür(yarıÇap/2, yarıÇap) -> daire(mavi),
  götür(-2*yarıÇap, 0) * boyaRengi(siyah) * kalemRengi(açıkGri) * kalemBoyu(5)
    -> Resim.daire(yarıÇap/2),
  götür(-2*yarıÇap, yarıÇap) * boyaRengi(beyaz) * kalemRengi(koyuGri) * kalemBoyu(5)
    -> Resim.daire(yarıÇap/2)
)