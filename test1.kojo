sil
canlandırmaHızınıKur(50)

def p1 = Picture {
    ev
    boyamaRenginiKur(sarı)
    ilerle(50, 50)
    //zıpla(-50); ileri(-50)
    yay(100, -180)
    ev
}
draw(p1)
val p2 = p1
draw(flipY -> p2)

satıryaz("""
Hoşgeldiniz! 
Görsel paletle kodlama yapmak için Araçlar menüsündeki
Komut Paleti komutunu kullanın.
""")
