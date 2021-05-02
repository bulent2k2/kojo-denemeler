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
    val variant:        Int) {
    require(3 < size, "Board needs to be 4x4 or bigger")
    require(20 > size, "Board needs to be 19x19 or smaller")
    require(startingPlayer != Empty, "White or Black needs to go first")
    private val board = Array.fill[Stone](size, size)(Empty)

}

