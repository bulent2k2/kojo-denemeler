// #include load-dictionary
// Now in: ~/src/kojo/git/kojo/installer/examples/anagram/anagram_tr.kojo.installed
çıktıyıSil()

// 42 satırla sözcük evirmece (Anagrams)
// Açıklamalar: ~/Desktop/COPYA/src/scala/scala-master/forcomp/src/main/scala/forcomp/Anagrams.scala
//    Yanıtlar: ~/Desktop/COPYA/src/scala/course/forcomp/src/main/scala/forcomp/Anagrams.scala

type Sözcük = Yazı // "Başaran"
type Tümce = Dizin[Sözcük]
type HarfSayıları = Dizin[(Harf, Sayı)] // Dizin('a' -> 3, 'b' -> 1, 'n' -> 1, 'r' -> 1, 'ş' -> 1)
val sözlük: Dizin[Sözcük] = sözcükDizininiYükle
def sözcüğünHarfSayıları(s: Sözcük): HarfSayıları = s.öbekle(_.küçükHarfe).işle { case (a, d) => (a, d.boyu) }.dizine.yinelemesiz
def tümceninHarfSayıları(t: Tümce): HarfSayıları = sözcüğünHarfSayıları(t.yazıYap)
lazy val harfSayılarındanSözcüklere: Eşlek[HarfSayıları, Dizin[Sözcük]] = sözlük.öbekle(sözcüğünHarfSayıları(_)) varsayılanDeğerle Boş
def evirmeceler(sözcük: Sözcük): Dizin[Sözcük] = harfSayılarındanSözcüklere(sözcüğünHarfSayıları(sözcük))
def kombinasyonlar(harfSayıları: HarfSayıları): Dizin[HarfSayıları] = { // todo: tailRecursion
    def tekHarfDurumu(harfVeSayısı: (Harf, Sayı)): Dizin[HarfSayıları] = Dizin() :: (harfVeSayısı match {
        case (harf, kaçTane) => (for (s <- 1 |-| kaçTane) yield Dizin(harf -> s)).dizine
    })
    harfSayıları match {
        case Boş         => Dizin(Dizin())
        case başı :: Boş => tekHarfDurumu(başı)
        case başı :: kuyruğu => for {
            öge <- tekHarfDurumu(başı)
            altKüme <- kombinasyonlar(kuyruğu)
        } yield öge ++ altKüme
    }
}
def çıkar(birinciDizin: HarfSayıları, ikinciDizin: HarfSayıları): HarfSayıları =
    ikinciDizin.soldanKatla(birinciDizin.eşleğe) { (eşlek, öge) =>
        öge match { // çift ikinciDizinin ögelerinden biri
            case (harf, kaçTane) => {
                val ilkDeğer = eşlek(harf)
                if (ilkDeğer == kaçTane) eşlek - harf else eşlek.değiştirilmiş(harf, ilkDeğer - kaçTane)
            }
        }
    }.dizine.yinelemesiz
def tümceselEvirmeceler(tümce: Tümce): Dizin[Tümce] = { // todo: tailRecursion
    def özyineleme(hs: HarfSayıları): Dizin[Tümce] =
        hs match {
            case Boş => Dizin(Boş)
            case _ => for {
                biri <- kombinasyonlar(hs)
                ilkSözcük <- harfSayılarındanSözcüklere(biri)
                tümceninGerisi <- özyineleme(çıkar(hs, biri))
            } yield ilkSözcük :: tümceninGerisi
        }
    özyineleme(tümceninHarfSayıları(tümce))
}

for (
    tümce <- Dizin(Dizin("al", "ver"), Dizin("gel", "lütfen"))
) {
    val evrik = zamanTut("") { tümceselEvirmeceler(tümce).işle(_.işle(_.küçükHarfe)) }("sürdü.")
    satıryaz(tümce.yazıYap(
        "tümce: ", " ", s"\n${evrik.boyu} evirmecesi var: ${evrik.işle(_.yazıYap(" ")).yazıYap("", ", ", ".")}"
    ))
}
