
trait Stone {
    val name = "stone"
}
case object White extends Stone {
    override val name = "white"
    override def toString() = "W"
}
case object Black extends Stone {
    override val name = "black"
    override def toString() = "B"
}
case object Empty extends Stone {
    override val name = "empty"
    override def toString() = "."
}
case class Room(row: Int, col: Int) {
    val y = row
    val x = col
    override def toString() = s"${x + 1}x${y + 1}"
}
trait Direction
case object N extends Direction; case object NE extends Direction
case object E extends Direction; case object SE extends Direction
case object S extends Direction; case object SW extends Direction
case object W extends Direction; case object NW extends Direction
case class Neighbor(dir: Direction, room: Room)
trait Level
case object Beginner extends Level
case object LessThanBeginner extends Level
case object Apprentice extends Level
case object Bachelor extends Level
case object Master extends Level
case object Doctor extends Level
case object Genius extends Level
case object MoreThanGenius extends Level
