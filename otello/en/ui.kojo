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
    def moves = board.moves(board.player())

    def drawTheBoard = {
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

    def defineBehavior(pic: Picture) = { // pic is a square on the board
        val room = pic2room(pic)
        var stonesToBeFlipped: Seq[Room] = Seq()
        pic.onMouseEnter { (_, _) =>
            board.stone(room) match {
                case Empty =>
                    val payOff = board.movePayoff(room, board.player())
                    if (payOff > 0) hiliteMoveOutcome
                    hintPics.update(payOff, room)
                case _ => hintPics.update(0, room)
            }
            hintPics.refresh(pic, room2point(room, false) - Point(l2, -l2))
        }
        pic.onMouseExit { (_, _) =>
            clearHilites
            hintPics.toggleV
            if (computerInPlay) computerToMove
        }
        pic.onMouseClick { (_, _) =>
            board.stone(room) match {
                case Empty =>
                    if (makeTheMove(room)) {
                        clearHilites
                        if (board.isGameOver) endTheGame
                        else if (computerInPlay) searchScore
                        else updateScore
                    }
                case _ =>
            }
        }
        def roomColor = stone2color(board.stone(room))
        def playerColor = stone2color(board.player())
        def oppColor = stone2color(board.player.opponent)
        def hiliteMoveOutcome = {
            stonesToBeFlipped = board.pretendMove(room)
            stonesToBeFlipped.map { r =>
                room2pic(r).setFillColor(playerColor)
                room2pic(r).setOpacity(0.6)
            }
        }
        def clearHilites = {
            stonesToBeFlipped.map { r =>
                room2pic(r).setFillColor(oppColor)
                room2pic(r).setOpacity(1)
            }
            pic.setFillColor(roomColor) // room of pic is in stonesToBeFlipped
            stonesToBeFlipped = Seq()
        }
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
            updateScore
            showMoves
            drawLastMove
            if (pauseDuration > 0) pause(pauseDuration)
            if (board.isGameOver) endTheGame
            true
        }
    }

    def newGame = if (board.moveCount() != 1) {
        board.reset("New game:")
        for (x <- board.range; y <- board.range)
            paint(Room(y, x), board.stone(y, x))
        history.reset()
        resetScore
        clearLastMove
        showMoves
        endedTheGame = false
        if (computerInPlay) computerToMove
    }

    var movePics: Seq[Picture] = Seq()
    var showMovesOn = true
    def showMoves = {
        movePics.foreach { _.erase() }
        if (showMovesOn) {
            val ordered = moves.map { r =>
                (r, board.movePayoff(r, board.player()))
            }.sortBy { p => p._2 }.reverse
            if (ordered.size > 0) {
                val maxPayOff = ordered.head._2
                movePics = ordered map {
                    case (r, payOff) =>
                        val color = if (payOff == maxPayOff) yellow else orange
                        val pic = penColor(color) * penThickness(3) * fillColor(noColor) ->
                            Picture.circle(l4)
                        pic.setPosition(room2point(r, false))
                        pic.forwardInputTo(room2pic(r))
                        pic.draw()
                        pic
                }
            }
        }
    }

    var endedTheGame = false
    def endTheGame = if (!endedTheGame) {
        endedTheGame = true
        assert(board.isGameOver == true)
        println(s"The game is over.")
        for (s <- Seq(White, Black)) println(s"${s.name.capitalize}: ${board.count(s)}")
        finishScore
    }
    def resetScore = {} // todo skorBaşlangıç
    def updateScore = {} // todo skoruGüncelle
    def searchScore = {} // todo skorBilgisayarHamleArıyor
    def finishScore = {} // todo skorBitiş

    def drawLastMove = {} // todo hamleyiGöster (use board.lastMove? instead of room?)
    def clearLastMove = {} // hamleResminiSil

    def computerInPlay = board.player() == computerPlays
    def computerToMove: Unit = {
        def board2board(size: Int) = {
            var b = new Board(size, Vector.fill(size * size)(0))
            for (s <- List(White, Black)) b = b.place(s, board.stones(s))
            b
        }
        val sBoard = board2board(board.size)
        val state = new State(sBoard, board.player())
        if (state.isGameOver) endTheGame
        else {
            val move = ABS.move(state) match {
                case Some(room) => room
                case _          => throw new Exception("Not here?!")
            }
            makeTheMove(move)
            // if we don't have any moves, computer to replay
            if (!board.isGameOver && computerInPlay) computerToMove
        }
    }

    def defineKeys = {
        onKeyPress { k =>
            k match {
                case Kc.VK_LEFT => mem.undo
                case Kc.VK_RIGHT => mem.redo
                case Kc.VK_UP => computerToMove
            }
        }
    }

    object mem {
        def undo = {
            history.undo
            endedTheGame = false
            repaint
        }
        def redo = {
            history.redo
            repaint
            if (board.isGameOver) endTheGame
        }
        def repaint {
            for (x <- board.range; y <- board.range)
            paint(Room(y, x), board.stone(y, x))
            updateScore
            drawLastMove // draw or show or paint - todo
            showMoves
        }
    }

    cleari()
    clearOutput()
    //toggleFullScreenCanvas()
    setBackground(darkGrayClassic)
    drawTheBoard
    showMoves
    defineKeys

    val hintPics = new HintPics(length)

    if (computerInPlay) computerToMove
}

class HintPics(length: Int) {
    val hintPayoff = Picture.textu("", 20, red)
    val hintCoord = Picture.textu("", 20, pink)
    def update(payoff: Int, room: Room) {
        hintPayoff.update(if (payoff > 0) payoff.toString else "")
        hintCoord.update(room.toString)
    }
    val pics = List(hintPayoff, hintCoord)
    def draw = pics.map(_.draw)
    def toggleV = pics.map(_.toggleV)
    def setPosition(p: Point) = {
        hintPayoff.setPosition(p)
        hintCoord.setPosition(p - Point(0, length / 2))
    }
    def moveToFront() = pics.map(_.moveToFront())
    def forwardInputTo(p: Picture) = pics.map(_.forwardInputTo(p))
    def refresh(picture: Picture, newPosition: Point) {
        this.setPosition(newPosition)
        this.toggleV
        this.moveToFront()
        this.forwardInputTo(picture)
    }
    draw
    toggleV
}

def test_basic_board_drawing(dur: Double): Unit = {
    val board = new EBoard(8, Black, 0)
    val history = new History(board)
    val computerPlays = Empty
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
    ui.makeTheMove(Room(2, 3))
    ui.newGame
}

def test_game_play = {
    val board = new EBoard(4, Black, 0)
    val history = new History(board)
    val computerPlays = Empty
    val ui = new UI(board, history, computerPlays)
    for (
        move <- List(
            Room(1, 0), Room(2, 0),
            Room(3, 0), Room(0, 2),
            Room(0, 3), Room(0, 0)
        )
    ) ui.makeTheMove(move)
    // now B to make two moves back to back:
    assert(board.player() == Black, "black back-to-back moves 1")
    ui.makeTheMove(Room(0, 1))
    assert(board.player() == Black, "black back-to-back moves 2")
    ui.makeTheMove(Room(2, 3))
    assert(board.player() == White, "finally white to move")
    for (move <- List(Room(3, 3), Room(3, 2))) ui.makeTheMove(move)
    // move W to make two back-to-back moves:
    assert(board.player() == White, "w1")
    ui.makeTheMove(Room(1, 3))
    assert(board.player() == White, "w2")
    ui.makeTheMove(Room(3, 1))
    assert(board.isGameOver == true, "the happy end")
    assert(board.score(White) == 10, "w=10")
    assert(board.score(Black) == 6, "b=6")
    ui.newGame
}
def test_computer_play = {
    val board = new EBoard(4, Black, 0)
    val history = new History(board)
    val computerPlays = Black
    val ui = new UI(board, history, computerPlays)
}
if (runUnitTests) {
    test_basic_board_drawing(0.1)
    test_game_play
    test_computer_play
}

