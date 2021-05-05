// elektronik tahta
class ETahta() {
  /* 
   başaAl resetBoard
   başlangıçTaşlarınıKur newBoard
   hamleSayısı moveCount (not the count of moves available now, that is moves.size. This is the count of the next move, first being 1.
   hamleyiDene tryMove
   hamleYoksa moves(player()).isEmpty
   !hamleYoksa moves(player()).size > 0
   kaçkaç score
   odaMı isValid
   oyunBitti -> isGameOver (not a var anymore!)
   oyuncu player
   satırAralığı range
   satırAralığıSondan range.reverse
   say count
   sonHamle lastMove
   sonOda end
   taş stone
   taşıKur setStone
   yasallar -> moves
   yaz print
   */

}

/*
 * do these later if needed
 */

class Tahta(val tane: Sayı, val tahta: Sayılar) {
    def tuzakKenarMı: Oda => İkil = {
        case Oda(str, stn) => str == 1 || stn == 1 || str == sonOda - 1 || stn == sonOda - 1
    }
    def tuzakKöşeMi: Oda => İkil = {
        case Oda(y, x) => (x == 1 && (y == 1 || y == sonOda - 1)) ||
            (x == sonOda - 1 && (y == 1 || y == sonOda - 1))
    }
    def köşeMi: Oda => İkil = {
        case Oda(str, stn) => if (str == 0) stn == 0 || stn == sonOda else
            str == sonOda && (stn == 0 || stn == sonOda)
    }
    def köşeyeİkiUzakMı: Oda => İkil = {
        case Oda(y, x) =>
            ((y == 0 || y == son) && (x == 2 || x == son - 2)) ||
                ((y == 2 || y == son - 2) &&
                    (x == 0 || x == 2 || x == son - 2 || x == son))
    }
}
class Arayüz {
    def özdevin(süre: Kesir = 0.0) = zamanTut("Özdevinimli oyun") {
        özdevinimliOyun(
            // abArama,  // yavaş! bütün Kojo'yu donduruyor!
            köşeYaklaşımı,
            süre)
    }("sürdü")
    def özdevinimliOyun( // özdevinim ve bir kaç hamle seçme yöntemi/yaklaşımı (heuristic)
        yaklaşım:        İşlev1[Dizi[Oda], Belki[Oda]],
        duraklamaSüresi: Kesir /*saniye*/
    ) = {
        val dallanma = EsnekDizim.boş[Sayı]
        var oyna = doğru
        while (oyna) yaklaşım(tahta.yasallar) match {
            case Biri(oda) =>
                dallanma += tahta.yasallar.size
                hamleyiYap(tahta.hamleyiDene(oda), oda, duraklamaSüresi)
            case _ =>
                sırayıÖbürOyuncuyaGeçir
                yaklaşım(tahta.yasallar) match {
                    case Biri(oda) =>
                        satıryaz(s"Yasal hamle yok. Sıra yine ${tahta.oyuncu().adı}ın")
                        dallanma += tahta.yasallar.size
                        hamleyiYap(tahta.hamleyiDene(oda), oda, duraklamaSüresi)
                    case _ =>
                        bittiKaçKaç(tahta)
                        if (dallanma.sayı > 0) {
                            val d = dallanma.dizi
                            satıryaz(s"Oyun ${d.size} kere dallandı. Dal sayıları: ${d.mkString(",")}")
                            satıryaz(s"Ortalama dal sayısı: ${yuvarla(d.sum / (1.0 * d.size), 1)}")
                            satıryaz(s"En iri dal sayısı: ${d.max}")
                        }
                        oyna = yanlış
                }
        }
    }

    def abArama(yasallar: Dizi[Oda]): Belki[Oda] = // yasallar yerine tahtadanTahta işlevini girdi olarak kullanıyor
        ABa.hamleYap(new Durum(tahtadanTahta, tahta.oyuncu()))

    def köşeYaklaşımı(yasallar: Dizi[Oda]): Belki[Oda] = rastgeleSeç(yasallar.filter(tahta.köşeMi(_))) match {
        case Biri(oda) => Biri(oda) // köşe bulduk!
        case _ => rastgeleSeç(yasallar.filter(tahta.içKöşeMi(_))) match {
            case Biri(oda) => Biri(oda)
            case _ => { // tuzakKenarlar tuzakKöşeleri içeriyor
                val tuzakKenarOlmayanlar = yasallar.filterNot(tahta.tuzakKenarMı(_))
                enİriGetirililerArasındanRastgele(
                    if (!tuzakKenarOlmayanlar.isEmpty) tuzakKenarOlmayanlar
                    else { // tuzak kenarlardan getirisi en iri olanlardan seçiyoruz
                        val tuzakKöşeOlmayanlar = yasallar.filterNot(tahta.tuzakKöşeMi(_))
                        if (tuzakKöşeOlmayanlar.isEmpty) yasallar else tuzakKöşeOlmayanlar
                    }
                )
            }
        }
    }
    def rastgeleSeç[T](dizi: Dizi[T]): Belki[T] = if (dizi.isEmpty) Hiçbiri else
        Biri(dizi.drop(rastgele(dizi.size)).head)
    def enİriGetirililerArasındanRastgele(yasallar: Dizi[Oda]): Belki[Oda] =
        rastgeleSeç(enGetirililer(yasallar))
    def enGetirililer(yasallar: Dizi[Oda]): Dizi[Oda] = {
        def bütünEnİriler[A, B: Ordering](d: Dizin[A])(iş: A => B): Dizin[A] = {
            d.sortBy(iş).reverse match {
                case Dizin()       => Dizin()
                case baş :: kuyruk => baş :: kuyruk.takeWhile { oda => iş(oda) == iş(baş) }
            }
        }
        bütünEnİriler(yasallar.toList) { tahta.hamleGetirisi(_) }
    }
}

