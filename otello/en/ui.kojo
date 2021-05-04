//#include history
//#include alphaBetaSearch

class UI(
    board:         EBoard,
    history:       History,
    computerPlays: Stone) {

    val length = 50 // size of squares on the board
    val (llx, lly) = (-board.size / 2 * length, -board.size / 2 * length)
    val (l2, l3, l4) = (length / 2, length / 3, length / 4)

    val emptySquareColor = Color(10, 111, 23) // dark greenish
    def stone2color: Stone => Color = {
        case Empty => emptySquareColor
        case White => white
        case Black => black
    }
    def room2point(room: Room, ll: Boolean = true) =
        if (ll) Point(llx + room.x * length, lly + room.y * length)
        else Point(llx + room.x * length + l2, lly + room.y * length + l2)
    val pic2room = collection.mutable.Map.empty[Picture, Room]
    val room2pic = collection.mutable.Map.empty[Room, Picture]
    def paint(r: Room, s: Stone) = room2pic(r).setFillColor(stone2color(s))

    def drawTheBoard = {
        cleari(); clearOutput()
        //toggleFullScreenCanvas()
        setBackground(darkGrayClassic)
        val innerCorners = ArrayBuffer.empty[Picture]
        val innerCornerColor = Color(255, 215, 85, 101) // pale yellowish color
        for (x <- board.range; y <- board.range) {
            val room = Room(y, x)
            val edgeColor =
                if (board.isInnerCorner(room)) innerCornerColor else purple
            val pic = penColor(edgeColor) *
                fillColor(stone2color(board.stone(y, x))) ->
                Picture.rectangle(length, length)
            pic.setPosition(room2point(room))
            pic.draw()
            pic2room += (pic -> room)
            room2pic += (room -> pic)
            if (board.isInnerCorner(room))
                innerCorners += pic
            defineBehavior(pic)
        }
        innerCorners.map { _.moveToFront() }
    }

    def makeTheMove(room: Room, pauseDuration: Double = 0.0): Boolean = {
        if (board.pretendMove(room).size < 2) {
            println(s"${board.player()} can't move on $room.")
            false
        }
        else {
            println(s"Move ${board.moveCount()}. ${board.player()} on $room:")
            history.saveBoard(true, board.lastMove)
            board.lastMove = Some(room)
            val player = board.player() // move changes the player, in general
            board.move(room).foreach { paint(_, player) }
            board.print
            redrawPossibleMoves
            updateScore
            drawLastMove
            if (pauseDuration > 0) pause(pauseDuration)
            true
        }
    }

    def newGame = if (board.moveCount() != 1) {
        board.reset("Yeni oyun:")
        for (x <- board.range; y <- board.range)
            paint(Room(y, x), board.stone(y, x))
        history.reset()
        resetScore
        clearLastMove
        if (board.player() == computerPlays &&
            board.moves(board.player()).size > 0) computerMoves
    }
    def defineBehavior(pic: Picture) = {} // todo kareyiTanımla

    def computerMoves = {} // todo öneri
    def redrawPossibleMoves = {} // todo seçenekleriGöster
    def resetScore = {} // todo skorBaşlangıç
    def updateScore = {} // todo skoruGüncelle

    def drawLastMove = {} // todo hamleyiGöster (use board.lastMove? instead of room?)
    def clearLastMove = {} // hamleResminiSil
    drawTheBoard
}

def test_basic_board_drawing(dur: Double): Unit = {
    val board = new EBoard(8, Black, 0)
    val history = new History(board)
    val computerPlays = Black
    val ui = new UI(board, history, computerPlays)
    board.print
    pause(dur)
    ui.makeTheMove(Room(3, 2)); pause(dur)
    ui.makeTheMove(Room(4, 2)); pause(dur)
    assert(ui.makeTheMove(Room(2, 2)) == false, "illegal move")
    assert(ui.makeTheMove(Room(5, 2)) == true, "good move"); pause(dur)
    assert(ui.makeTheMove(Room(4, 2)) == false, "another kind of illegal move")
    assert(ui.makeTheMove(Room(2, 2)) == true, "good move 2"); pause(dur)
    ui.newGame
    println("Tests passed")
}
test_basic_board_drawing(1.0)

