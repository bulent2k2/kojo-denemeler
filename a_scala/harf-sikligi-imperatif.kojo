def harfSıklığı(girdi: Dizin[Harf]): Dizin[(Harf, Sayı)] = {
    var sunum = Dizin[(Harf, Sayı)]()
    if (girdi.boşMu) sunum
    else {
        var önceki = girdi.başı
        var kuyruk = girdi.kuyruğu
        var sayaç = 1
        while (!kuyruk.boşMu) {
            if (kuyruk.başı == önceki) sayaç += 1
            else {
                sunum = (önceki, sayaç) :: sunum
                sayaç = 1
                önceki = kuyruk.başı
            }
            kuyruk = kuyruk.kuyruğu
        }
        (önceki, sayaç) :: sunum
    }
}

val yazı = "Kojo ile oyun oynayarak Scala dilini öğrenmek ve hatta işlevsel ve nesneye yönelik programlama becerisi edinmek harika değil mi "
val sözcükDizini = yazı.böl(" ")
val harfler = sözcükDizini.düzİşle(_.dizine).işle(_.büyükHarfe).sırayaSok(_ < _)
satıryaz(harfSıklığı(harfler))