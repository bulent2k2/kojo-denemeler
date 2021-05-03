silVeSakla
def üçgen = Resim { yinele(3) { ileri(100); sağ(120) } }
val r1 = döndür(30) * büyüt(0.5) * yansıtX -> üçgen
val r2 = döndür(90) * büyüt(0.5) * yansıtX -> üçgen
val rd1 = resimYatayDizi(r2, r1, r2, r1, r2, r1)
val rd2 = resimDikeyDizi(rd1, rd1, rd1, rd1)
çizMerkezde(rd2)
