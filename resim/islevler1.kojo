silVeSakla
val i1 = çizimSonrasıİşlev { res =>
    res.kalemRenginiKur(mor)
    res.kondur(-50, 50)    
}

val res = i1 -> Resim.dikdörtgen(20, 2)
çiz(res)
