//#include types

// this board is used only in alpha-beta search
class Board(val size: Int, val board: Vector[Int]) {
    val lastRoom = size - 1
    val last = lastRoom
    val range = 0 to last

    def s2n(s: Stone) = s match {
        case Black => 2
        case White => 1
        case _     => 0
    }
    def n2s(n: Int) = n match {
        case 2 => Black
        case 1 => White
        case _ => Empty
    }
    def stone(r: Room) = n2s(board(r.y * size + r.x))
    def place(s: Stone, r: Room) = new Board(
        size, board.updated(r.y * size + r.x, s2n(s)))
    def place(s: Stone, rooms: Seq[Room]) = {
        var newBoard = board
        for (r <- rooms) newBoard = newBoard.updated(r.y * size + r.x, s2n(s))
        new Board(size, newBoard)
    }

    def score(turn: Stone) = {
        count(turn) // + 4 * countIf(turn)(isCorner) + 2 * countIf(turn)(isTwoAwayFromCorner)
        // + countIf(turn)(isInnerCorner) - 2 * countIf(turn)(isTrapCorner)
        // - countIf(turn)(isTrapSide)
    }
    def count(turn: Stone) = countIf(turn) { room => true }
    def countIf(turn: Stone)(f: Room => Boolean) =
        (for (
            x <- range; y <- range;
            if stone(Room(y, x)) == turn && f(Room(y, x))
        ) yield 1
        ).size

    def moves(turn: Stone) =
        (for (y <- range; x <- range; if stone(Room(y, x)) == Empty)
            yield Room(y, x)
        ) filter { neighborsToFlip(turn, _).size > 0 }

    def move(turn: Stone, room: Room): Board = {
        val rooms = ArrayBuffer(room)
        val opponent = if (turn == White) Black else White
        neighborsToFlip(turn, room).foreach { n =>
            rooms += n.room
            theRestOfTheLine(n).takeWhile(stone(_) == opponent) foreach {
                r => rooms += r
            }
        }
        place(turn, rooms.toSeq)
    }
    def neighborsToFlip(turn: Stone, room: Room): Seq[Neighbor] =
        findTheNeighbors(room) filter { n =>
            val opponent = if (turn == White) Black else White
            stone(n.room) == opponent && isTheEndOfLineLegal(n, turn)._1
        }
    def isTheEndOfLineLegal(n: Neighbor, turn: Stone): (Boolean, Int) = {
        val theRest = theRestOfTheLine(n)
        val line = theRest.dropWhile { r => stone(r) != turn && stone(r) != Empty }
        if (line.isEmpty) (false, 0)
        else (stone(line.head) == turn, 1 + theRest.size - line.size)
    }

    def findTheNeighbors(r: Room): Seq[Neighbor] = Seq(
        Neighbor(E, Room(r.y, r.x + 1)),
        Neighbor(W, Room(r.y, r.x - 1)),
        Neighbor(N, Room(r.y + 1, r.x)),
        Neighbor(S, Room(r.y - 1, r.x)),
        Neighbor(NE, Room(r.y + 1, r.x + 1)),
        Neighbor(NW, Room(r.y + 1, r.x - 1)),
        Neighbor(SE, Room(r.y - 1, r.x + 1)),
        Neighbor(SW, Room(r.y - 1, r.x - 1))
    ) filter { n => isValid(n.room) }
    def isValid: Room => Boolean = {
        case Room(y, x) => 0 <= y && y < size && 0 <= x && x < size
    }
    def theRestOfTheLine(n: Neighbor): Seq[Room] = {
        val line = ArrayBuffer.empty[Room]
        val (x, y) = (n.room.x, n.room.y)
        n.dir match {
            case E => for (i <- x + 1 to last) /*   */ line += Room(y, i)
            case W => for (i <- x - 1 to 0 by -1) /**/ line += Room(y, i)
            case N => for (i <- y + 1 to last) /*   */ line += Room(i, x)
            case S => for (i <- y - 1 to 0 by -1) /**/ line += Room(i, x)
            case NE => // both x and y increase
                if (x >= y) for (i <- x + 1 to last) /*       */ line += Room(y + i - x, i)
                else for (i <- y + 1 to last) /*              */ line += Room(i, x + i - y)
            case SW => // both x and y decrease
                if (x >= y) for (i <- y - 1 to 0 by -1) /*    */ line += Room(i, x - y + i)
                else for (i <- x - 1 to 0 by -1) /*           */ line += Room(y - x + i, i)
            case NW => // x decreases as y increases (and vice versa)
                if (x + y >= last) for (i <- y + 1 to last) /**/ line += Room(i, x + y - i)
                else for (i <- x - 1 to 0 by -1) /*           */ line += Room(y + x - i, i)
            case SE => // x increases as y decreases
                if (x + y >= last) for (i <- x + 1 to last) /**/ line += Room(y + x - i, i)
                else for (i <- y - 1 to 0 by -1) /*           */ line += Room(i, x + y - i)
        }
        line.toSeq
    }

    def print(msg: String = "", lineHeader: String = "") = {
        for (y <- range.reverse) {
            val row = for (x <- range) yield n2s(board(y * size + x))
            println(row.mkString(lineHeader, " ", ""))
        }
        if (msg.size > 0) println(lineHeader + msg)
        for (p <- List(White, Black))
            println(s"$lineHeader ${p.name.capitalize}: ${count(p)} score: ${score(p)}")
    }
}

def test_board = {
    clearOutput
    val size = 6
    var b = new Board(size, Vector.fill(size * size)(0))
    assert(b.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "1")
    val foo = b.place(White, Room(1, 1))
    assert(b.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "2")
    b = b.place(White, Seq(Room(2, 2), Room(3, 3)))
    b = b.place(Black, Seq(Room(2, 3), Room(3, 2)))
    val newBoard = b
    // newBoard.print("t1")
    assert(newBoard.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "3")
    var turn: Stone = Black
    for (
        (i, moves, finalBoard) <- List(
            (0, List(Room(2, 1), Room(3, 1), Room(4, 1)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (1, List(Room(1, 2), Room(1, 3), Room(1, 4)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (2, List(Room(4, 3), Room(4, 2), Room(4, 1)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0)),
            (3, List(Room(3, 4), Room(2, 4), Room(1, 4)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        )
    ) {
        for (move <- moves) {
            b = b.move(turn, move)
            turn = if (turn == Black) White else Black
        }
        //println(b.board)
        //b.print()
        assert(b.board == finalBoard, i)
        b = newBoard
        turn = Black
    }
}
test_board

def newBoard(size: Int, variant: Int = 0): Board = {
    var b = new Board(size, Vector.fill(size * size)(0))
    def placeSeq(rooms: Seq[(Int, Int)])(stone: Stone) =
        b = b.place(stone, rooms.map(p => Room(p._1, p._2)))
    def place4: Room => Unit = {
        case Room(y, x) =>
            placeSeq(Seq((y, x), (y + 1, x + 1)))(White)
            placeSeq(Seq((y + 1, x), (y, x + 1)))(Black)
    }
    val mid: Int = size / 2
    val end = size - 1
    variant match {
        case 2 => // empty board. How to start the game?
        case 1 =>
            require(size > 6, "Board for this variant needs to be 7x7 or bigger")
            place4(Room(1, 1))
            place4(Room(end - 2, end - 2))
            place4(Room(1, end - 2))
            place4(Room(end - 2, 1))
        case 0 =>
            val even = size % 2 == 0
            if (even) place4(Room(mid - 1, mid - 1))
            else {
                val (a, b) = (mid - 1, mid + 1)
                val (ap, am, bp, bm) = (a + 1, a - 1, b + 1, b - 1)
                placeSeq(Seq(a -> a, b -> b))(White)
                placeSeq(Seq(a -> b, b -> a))(Black)
                placeSeq(Seq(ap -> a, ap -> b, bp -> bm, am -> bm))(White)
                placeSeq(Seq(a -> ap, b -> ap, ap -> am, ap -> bp))(Black)
            }
    }
    b
}
