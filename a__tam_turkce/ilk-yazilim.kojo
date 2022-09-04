dez x = 6 // val
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
    satıryaz("Defining with turkish keyword: tanım:..")
    tanım yaz2 () = {
        dez msg = "Hello, my dears."
        dez mesaj = " Merhaba canlar."
        den foo = " Bu da değişken değer."
        foo = msg + foo + mesaj
        satıryaz(foo)
    }
    eğer(doğru) {
        satıryaz("In the body of the turkish translation of the if keyword")
        yaz2()
    } yoksa {
        satıryaz("NOT HERE!")
    }
    yazı
    bilgiVer
}
Merhaba
silVeSakla
yazı("  ben bir kaplumbağayım")
