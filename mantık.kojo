case class Önerge(doğruMu: İkil) {
    val bu = this
    def tersi = Önerge(!doğruMu)
    def ve(öbürü: Önerge) = if (doğruMu) öbürü else bu
    def veya(öbürü: Önerge) = if (doğruMu) bu else öbürü
    def yada(öbürü: Önerge) = (bu veya öbürü) ve (bu ve öbürü).tersi
    def eşittir(öbürü: Önerge) = doğruMu == öbürü.doğruMu

    def unary_!(): Önerge = bu.tersi
    def *(öbürü: Önerge) = bu ve öbürü
    def +(öbürü: Önerge) = bu veya öbürü
    def x(öbürü: Önerge) = bu yada öbürü
    def ==(öbürü: Önerge) = doğruMu == öbürü.doğruMu

    override def toString = if (doğruMu) "doğru" else "yanlış"
    def to01 = if (doğruMu) "1" else "0"
}

def tersi(x: Önerge) = x.tersi
def ve(x: Önerge, y: Önerge) = x ve y
def veya(x: Önerge, y: Önerge) = x veya y
def yada(x: Önerge, y: Önerge) = x yada y
def eşittir(x: Önerge, y: Önerge) = x eşittir y

def deneme() = {
    def çizgi = satıryaz("-" * 34)
    def çift = {çizgi; çizgi}
    val ara = " " * 10
    çift
    val (d, y) = (Önerge(doğru), Önerge(yanlış))
    val (dt, yt) = (tersi(d), tersi(y))
    satıryaz(s"  $d'nun tersi == $dt")
    satıryaz(s"  $y'ın tersi == $yt")
    çizgi
    satıryaz(s"$ara !${d.to01} == ${(!d).to01}")
    satıryaz(s"$ara !${y.to01} == ${(!y).to01}")
    çift
    val seçenek = Dizin(d, y)
    for ((bağlam, adı) <- Dizin((ve _, "ve"), (veya _, "veya"), (yada _, "ya da"))) {
        for (a <- seçenek; b <- seçenek) {
            satıryaz(f"  $a%6s $adı%6s $b%6s == ${bağlam(a, b)}")
        }
        çizgi
    }
    çizgi
    for ((bağlam, adı) <- Dizin((ve _, "*"), (veya _, "+"), (yada _, "x"))) {
        for (a <- seçenek; b <- seçenek) {
            val c = bağlam(a, b)
            satıryaz(f"$ara ${a.to01}%s $adı%s ${b.to01}%s == ${c.to01}")
        }
        çizgi
    }
    çizgi
    for (a <- seçenek; b <- seçenek) {
        val (ab, a_b, axb) = (a ve b, a veya b, a yada b)
        satıryaz(f"  $a%6s ve    $b%6s == $ab")
        satıryaz(f"  $a%6s veya  $b%6s == $a_b")
        satıryaz(f"  $a%6s ya da $b%6s == $axb")
        çizgi
    }
    çizgi
    for (a <- seçenek; b <- seçenek) {
        val (ab, a_b, axb) = (a * b, a + b, a x b)
        satıryaz(s"$ara ${a.to01} * ${b.to01} == ${ab.to01}")
        satıryaz(s"$ara ${a.to01} + ${b.to01} == ${a_b.to01}")
        satıryaz(s"$ara ${a.to01} x ${b.to01} == ${axb.to01}")
        çizgi
    }
    çizgi
}
çıktıyıSil
deneme
