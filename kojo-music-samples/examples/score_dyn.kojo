// #include /music.kojo

cleari()

val notes = Seq(sa, re, ga, pa, sa3)
val instruments = Seq(Instrument.PIANO, Instrument.PAN_FLUTE)
val tempos = Seq(60.0, 120.0)

val scoreMaker = new ScoreGenerator {
    def nextScore = {
        println("Getting next score")
        Score(
            100, //randomFrom(tempos),
            InstrumentPart(
                randomFrom(instruments),
                Phrase(randomFrom(notes.permutations.toList))
            )
        )
    }
}

showServerControls()
MusicPlayer.playLoop(scoreMaker)
updateServerControls()
