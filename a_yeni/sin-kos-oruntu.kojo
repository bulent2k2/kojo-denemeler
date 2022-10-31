// https://codex.kogics.net/codesketch?sketchId=1711
silVeSakla()
originBottomLeft()
val ta = tuvalAlanı
val renk = cm.radialGradient(  // merkezden dışa doğru değişen altın rengi için
    ta.eni / 2, ta.boyu / 2,
    renkler.gold.fadeOut(0.2),  // soluklaşsın biraz
    ta.eni / 2, beyaz, yanlış)
artalanıKur(renk)
ekranTazelemeHızınıKur(100)

class Karalama {
    var z = 0.0
    def setup(surface: CanvasDraw) { // todo
        import surface.{fill, stroke, strokeWeight, translate}
        stroke(siyah.fadeOut(0.8))
        strokeWeight(1)
        // fill(renkler.gold.fadeOut(0.2))
        translate(cwidth / 2, cheight / 2)
    }

    def drawLoop(surface: CanvasDraw) {
        surface.ellipse(kosinüs(z / 10) * 200, sinüs(z / 50) * 200, 100, 100)
        z += 1
    }
}

val karalama = new Karalama
val pic = Picture.fromSketch(karalama, 1) // todo
draw(pic)