// todo: atlamasız yap. tiling?

//sil
def tekrar(rc: Sayı, rb: => Her){
  for (i <- 1 to rc) rb
}

// yavaş 100 adım   / 1 saniye
// orta  1000 adım  / 1 saniye
// hızlı 10000 adım / 1 saniye

// üç seçenek de yaklaşık 5 saniye alıyor...
val seç = 2
val (hız, sayı, kay) = seç match {
  case 0  => (yavaş, 1, yanlış)
  case 1 => (orta, 10, yanlış)
  case _ => (hızlı, 60, doğru)
}
hızıKur(hız)
val boy = 100
var i = 0
val a1 = buAn
tekrar(sayı, {
  tekrar(4, { ileri(boy); sağ() } )
  ileri(boy)
  i += 1
  if (kay && i % 10 == 0) {
    atla(i*boy/10,0)
  }
})
satıryaz(f"geçen zaman: ${(buAn - a1)/1000.0}%2.2f saniye")
ev
