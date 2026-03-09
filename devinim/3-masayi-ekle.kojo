silVeSakla
gridiGöster
eksenleriGöster
dez yç = 10
dez top = Resim.daire(yç)
top.çiz
dez (solKenar, sağKenar, taban, tavan) = (-200, 200, -100, 100)
çiz {
    kalemRengi(siyah) * götür(solKenar, taban) ->
        Resim.dikdörtgen(sağKenar - solKenar, tavan - taban)
}
top.kondur(solKenar + 20, taban + 30)
dez (tSo, tSa) = (solKenar + yç, sağKenar - yç)
dez (tTb, tTv) = (taban + yç, tavan - yç)
den (dx, dy) = (4, 2)
canlandır {
    top.kondur(top.konum.x + dx, top.konum.y + dy)
    eğer (top.konum.x >= tSa || top.konum.x <= tSo) dx = -dx
    eğer (top.konum.y >= tTv || top.konum.y <= tTb) dy = -dy
}