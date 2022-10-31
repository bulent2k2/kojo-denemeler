// #worksheet

// asalsa bir yoksa da en küçük bölenini verelim
def asalMı(n: Sayı): Sayı = n match {
    case 2 => 1
    case _ => if (n <= 1) n // eksi sayı girenlere de kendisini ver
    else if (n % 2 == 0) 2
    else {
        var p = 3
        while (p <= karekökü(n).sayıya + 1)
            if (asalMı(p) > 1) p += 2
            else if (n % p == 0) return p
            else p += 2
        return 1
    }
} //> def asalMı(n: TurkishAPI.Sayı): TurkishAPI.Sayı
for (a <- Dizi(2, 3, 5, 7, 11, 13, 23))
    gerekli(asalMı(a) == 1, s"$a")
for (ad <- Dizi(4, 6, 10, 15)) gerekli(asalMı(ad) > 1, s"$ad")
asalMı(119) //> val res34: TurkishAPI.Sayı = 7
asalMı(2 * 7 * 17 + 1) //> val res35: TurkishAPI.Sayı = 1
doğru //> val res36: Boolean = true