//#include types

class MoveCount { // the count of the next move counting from 1, 2, 3...
    private var count: Int = _
    def apply() = count
    def reset() = count = 1
    def incr() = count += 1
    def decr() = count -= 1
    reset()
}

class Player(val firstToPlay: Stone) {
    require(firstToPlay != Empty, "White or Black needs to go first")
    private var player: Stone = _
    def apply() = player
    def opponent = if (player == White) Black else White
    def reset() = set(firstToPlay)
    def change() = set(opponent)
    def set(p: Stone) = player = p
    reset()
}

class EBoard(
    val size:           Int, // board is size x size
    val startingPlayer: Stone,
    val variant:        Int) extends CoreBoard {
    require(3 < size, "Board needs to be 4x4 or bigger")
    require(20 > size, "Board needs to be 19x19 or smaller")
    require(startingPlayer != Empty, "White or Black needs to go first")
    private val board = Array.fill[Stone](size, size)(Empty)
    val player = new Player(startingPlayer)
    val moveCount = new MoveCount
    var lastMove: Option[Room] = _

    def stone(y: Int, x: Int) = board(y)(x)
    def stone(room: Room) = board(room.y)(room.x)
    def setStone(y: Int)(x: Int)(s: Stone) = board(y)(x) = s
    def setStone(room: Room)(s: Stone) = board(room.y)(room.x) = s
    def print = for (y <- range.reverse) println(board(y).mkString(" "))
    def isGameOver =
        if (moves(player()).size > 0) false
        else moves(player.opponent).isEmpty
    def score(short: Boolean) = {
        val (p1, p2) = if (short) ("W", "B") else ("White", "Black")
        s"$p1: ${count(White)}\n$p2: ${count(Black)}"
    }

    def placeSeq(rooms: Seq[(Int, Int)])(stone: Stone): Unit =
        rooms.map(p => Room(p._1, p._2)).foreach { setStone(_)(stone) }

    def newBoard = {
        var b = new EBoard(size, startingPlayer, variant)
        CoreBoard.newBoard(b, size, variant)
        for (x <- range; y <- range) setStone(y)(x)(b.stone(y, x))
    }
    def resetBoard(header: String = "") = {
        newBoard
        player.reset()
        moveCount.reset()
        lastMove = None
        if (header.size > 0) println(header)
        print
    }

    resetBoard()
}

