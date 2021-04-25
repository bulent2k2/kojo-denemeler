trait Taş
case object Beyaz extends Taş { override def toString() = "B" }
case object Siyah extends Taş { override def toString() = "S" }
case object Yok extends Taş { override def toString() = "." }
// satır ve sütün sırası yazılımda ters!
//case class Oda(val y: Sayı, val x: Sayı)
case class Oda(str: Sayı, stn: Sayı) {
  val y = str
  val x = stn
  override def toString() = s"${stn + 1}x${str + 1}"
}

trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön

case class Komşu(yön: Yön, oda: Oda)
