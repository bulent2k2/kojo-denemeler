Color(255, 128, 30, 216)

def renk(i: Sayı, x: Kesir, y: Kesir) = {
    val yumuşak = log2(log2(x * x + y * y) / 2)
    val renkİ = sayıya(karekökü(i + 10 - yumuşak) * 256) % renkler.size
    renkler(renkİ)
}

def log2(x: Kesir) = math.log(x) / math.log(2)
log2(16)

lazy val renkler = Dizi(0, 1, 2, 3)

// Dizi.doldur(2048){ i => }
/*
    gerekli(x < 1.0, "1'den küçük olmalı")
    gerekli(x >= 0, "0'dan büyük olmalı")
    */
// todo: parameterize the five points
/*  konum    rgb/kym
    0.0      0  7 100
    0.16     32 107 203
    0.42     237 255 255
    0.6425   255 170 0
    0.8575   0 2 0 
    */

def k2kym(x: Kesir) = (k2d(x, kd), k2d(x, yd), k2d(x, md))

val konum = Dizi(0.0, 0.16, 0.42, 0.6425, 0.8575)
val kd = Dizi(0, 32, 237, 255, 0)
val yd = Dizi(7, 107, 255, 170, 2)
val md = Dizi(100, 203, 255, 0, 0)
// önce doğrusal olarak arayı dolduralım
def k2d(x: Kesir, rd: => Dizi[Sayı], kk: => Dizi[Kesir] = konum) = sayıya(
    if (x <= kk(0)) rd(0)
    else if (x <= kk(1)) rd(0) + (x - kk(0)) * (rd(1) - rd(0)) / (kk(1) - kk(0))
    else if (x <= kk(2)) rd(1) + (x - kk(1)) * (rd(2) - rd(1)) / (kk(2) - kk(1))
    else if (x <= kk(3)) rd(2) + (x - kk(2)) * (rd(3) - rd(2)) / (kk(3) - kk(2))
    else if (x <= kk(4)) rd(3) + (x - kk(3)) * (rd(4) - rd(3)) / (kk(4) - kk(3))
    else rd(4))

assert((for(x <- konum) yield(k2d(x, kd))).toList == kd.toList, "0 kırmızı beş nokta")
assert((for(x <- konum) yield(k2d(x, yd))).toList == yd.toList, "1 yeşil beş nokta")
assert((for(x <- konum) yield(k2d(x, md))).toList == md.toList, "2 mavi beş nokta")
val orta = Dizi.doldur(4){i => 0.5 * (konum(i+1) + konum(i))}
assert( (for(x <- orta) yield(k2d(x, kd))).toList == List(16, 134, 246, 127), 
    "0 kırmızı dört orta nokta")
assert( (for(x <- orta) yield(k2d(x, yd))).toList == List(57, 180, 212, 86), 
    "1 yeşil dört orta nokta")
assert( (for(x <- orta) yield(k2d(x, md))).toList == List(151, 229, 127, 0), 
    "1 mavi dört orta nokta")