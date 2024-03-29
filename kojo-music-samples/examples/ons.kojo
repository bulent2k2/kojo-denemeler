// #include /music.kojo

cleari()

val re_wq = Note(Swara.Re, w + q, 1, 127, 0.5)

val notes = Phrase(
    re_w, sa, r, re, r_h, ga_komal, re_wq, re, r_w
)

val score =
    Score(
        120.0,
        InstrumentPart(Instrument.PAN_FLUTE, notes)
    )

showServerControls()
MusicPlayer.playLoop(score)
updateServerControls()
println(notes.durationDecimalForm)
