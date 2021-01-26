val kaplumbağa0 = TurkishAPI.kaplumbağa
def yeniKaplumbağa(x: Kesir, y: Kesir) = new TurkishAPI.Kaplumbağa(x, y)

def tümEkran = toggleFullScreenCanvas
def rastgeleSeçim = randomBoolean
object tuvalAlanı {
    def ta = tCanvas.cbounds
    def en = ta.width
    def boy = ta.height
    def x = ta.x
    def y = ta.y
    // todo: more..
}

case class Resim(r: Picture) {
    val konumuKur = kondur _
    def kondur(x: Kesir, y: Kesir) = r.setPosition(x, y)
    def kalemKalınlığınıKur(kalınlık: Sayı) = r.setPenThickness(kalınlık)
    def kalemRenginiKur = r.setPenColor _
    def alan = r.area _
    // todo: more..
}
object Resim {
    def çiz(r: Resim) = draw(r.r)
    def köşegen(en: Kesir, boy: Kesir) = Resim(Picture.line(en, boy))
    def yay(yarıçap: Kesir, açı: Kesir) = Resim(Picture.arc(yarıçap, açı))
    def daire(yarıçap: Kesir) = Resim(Picture.circle(yarıçap))
    def elips(xYarıçapı: Kesir, yYarıçapı: Kesir) = Resim(Picture.ellipse(xYarıçapı, yYarıçapı))
    def elipsDikdörtgenİçinde(en: Kesir, boy: Kesir) = Resim(Picture.ellipseInRect(en, boy))
    def yatay(boy: Kesir) = Resim(Picture.hline(boy))
    def dikey(boy: Kesir) = Resim(Picture.vline(boy))
    def dikdörtgen(en: Kesir, boy: Kesir) = Resim(Picture.rect(en, boy))
    def yazı(içerik: Her, yazıBoyu: Sayı) = Resim(Picture.text(içerik, yazıBoyu))
    def düğme(ad: Yazı)(işlev: => Birim) = Resim(Picture.button(ad)(işlev))
    def arayüz(parça: javax.swing.JComponent) = Resim(Picture.widget(parça))
    // todo: more..
}
def çiz(r: Resim) = Resim.çiz(r)

// Lalit, This is already in trInit.scala, so pls don't worry about it
def rastgeleRenk = randomColor
