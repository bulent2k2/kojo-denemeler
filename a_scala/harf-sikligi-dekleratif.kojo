val harfSıklığı = "Kojo ile oyun oynayarak Scala dilini öğrenmek ve hatta işlevsel ve nesneye yönelik programlama becerisi edinmek harika değil mi".
    böl(" ").düzİşle(_.dizine).işle(_.büyükHarfe).sırayaSok(_ < _).
    soldanKatla(Dizin[(Harf, Sayı)]()) {
        case ((önceki, sayaç) :: kuyruk, harf) if (önceki == harf) => (önceki, sayaç + 1) :: kuyruk
        case (sunum, harf)                                         => (harf, 1) :: sunum
    }

satıryaz(harfSıklığı.
    sırala(p => p._2).tersi.
    işle { p => s"${p._1}:${p._2}" }.
    yazıYap(" "))