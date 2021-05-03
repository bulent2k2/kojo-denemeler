//#include types
//#include eBoard

// support for sequences of undo/redo
class History(board: EBoard) {
    val size = board.size
    private val oldBoards = ArrayBuffer.empty[Array[Array[Stone]]]
    private val players = ArrayBuffer.empty[Stone]
    private val moves = ArrayBuffer.empty[Option[Room]]
}
