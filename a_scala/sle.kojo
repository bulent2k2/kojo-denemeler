val info = <html> 
   Create some blocks as input to topo design
   </html>.toString

val ay_xl = ay.Yazıgirdisi(0)
val ay_yl = ay.Yazıgirdisi(0)
val ay_width = ay.Yazıgirdisi(100)
val ay_height = ay.Yazıgirdisi(50)
val ui = Resim.arayüz(
    ay.Sütun(
        ay.Sıra(ay.Tanıt(info)),
        ay.Sıra(ay.Tanıt("xl: "), ay_xl),
        ay.Sıra(ay.Tanıt("yl: "), ay_yl),
        ay.Sıra(ay.Tanıt("width: "), ay_width),
        ay.Sıra(ay.Tanıt("height: "), ay_height),
        ay.Sıra(ay.Düğme("Draw") {
            val rect = götür(ay_xl.value, ay_yl.value) ->
                Resim.dikdörtgen(ay_width.value, ay_height.value)
            çiz(rect)
        })
    )
)
silVeSakla()
çiz( götür(tuvalAlanı.x, tuvalAlanı.y) -> ui )
