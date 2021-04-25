type Hane = (Sayı, Sayı) // satır, sütun
trait Taş
case class Beyaz extends Taş
case class Siyah extends Taş
type Oyun = (Hane, Taş)
type Sonuç = (Oyun, Sayı)  // hane, skor
class Durum(val tahta: Tahta, val oyuncu1: Oyuncu, val oyuncu2: Oyuncu) {
  def oyuncu = if (oyuncu1.sıraBenim) oyuncu1 else oyuncu2
  def skor = if (oyuncu1.sıraBenim) oyuncu1.skor - oyuncu2.skor
    else oyuncu2.skor - oyuncu1.skor
  def oyna(hamle: Oyun): Durum = {
    val yeni = tahta.oyna(hamle, oyuncu)
    new Durum(yeni, o
  }
}

object Aba { // alfa-beta arama

  private val aramaDerinliğiSınırı = 4

  def enİyiHamle(durum: Durum): Sonuç = (
    for( hamle <- durum.seçenekler ) yield hamle -> alfaBeta(durum.oyna(hamle))
  ).minBy(_._2)._1

  // karşı oyuncunun skorunu azaltmaya çalışalım
  private def alfaBeta(durum: Durum): Sayı = 
    azalt(durum, aramaDerinliğiSınırı, Sayı.MIN_VALUE, Sayı.MAX_VALUE)

  private def azalt(durum: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı = {
    if (durum.bitti || derinlik == 0) return durum.skor
    var yeniBeta = beta
    durum.seçenekler.foreach { hamle => 
      val yeniDurum = durum.oyna(hamle)
      yeniBeta = enUfağı(beta, artır(yeniDurum, derinlik - 1, alfa, yeniBeta))
      if (alfa >= yeniBeta) return alfa
    }
    yeniBeta
  }
  private def artır(durum: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı = {
    if (durum.bitti || derinlik == 0) return durum.skor
    var yeniAlfa = alfa
    durum.seçenekler.foreach { hamle => 
      val yeniDurum = durum.oyna(hamle)
      yeniAlfa = enİrisi(yeniAlfa, azalt(yeniDurum, derinlik - 1, yeniAlfa, beta))
      if (yenialfa >= beta) return beta
    }
    yeniAlfa
  }
}

class Oyuncu(val skor: Sayı, val sıraBenim: İkil) {
  def oyna(durum: Durum): Oyun = abs.oyna(durum)
}
