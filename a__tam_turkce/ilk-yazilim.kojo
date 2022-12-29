dez x = 6 // İngilizcesi: val
den y = 6 // var
y += 1
tanım örnekYöntem(x: Sayı, y: Sayı): Yazı = { // def
    dez aty = 42
    eğer(x * y < aty) { // if
        "küçük"
    } yoksa eğer(x * y > aty) { // else if
        "büyük"
    } yoksa {
        s"Alan Turing öldüğünde $aty yaşındaydı." // todo: s for string
    }
}
satıryaz(örnekYöntem(x, y))

durum sınıf Uygulama() { // case class
    satıryaz("Uygulama 1")
    tanım bilgiVer = satıryaz("Uygulama 2")
}
özellik Yazıcı { // trait
    tanım yazı = { satıryaz("Yazıcıdan") }
}
nesne Merhaba yayar Uygulama birlikte Yazıcı { // object foo extends bar with dee
    satıryaz("bir işlev tanımlayalım:")
    tanım yaz2 () = {  // def
        dez msg = "Günaydın!"
        dez mesaj = " Merhaba canlar."
        den foo = " Bu da değişken değer."
        foo = msg + foo + mesaj
        satıryaz(foo)
    }
    eğer(doğru) {
        satıryaz("eğer doğruysa bunu yazalım")
        yaz2()
    } yoksa {
        satıryaz("Burada olmamalıyız!")
    }
    yazı
    bilgiVer
}
Merhaba
silVeSakla
hızıKur(çokHızlı)
kalemiKaldır(); sol; ileri(200); sağ; kalemiİndir()
yazı("ben saklambaç oynamayı seven bir kaplumbağayım ☺")