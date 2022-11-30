// Kare grid çizgesi kuracağız. Düzlemsel bir çizgedir.
dez KNS = 2 // Karenin bir kenarında kaç tane nokta olsun? kns arttıkça oyun zorlaşır. Başlangıçtaki nokta sayısı = kns*kns.
den yarıçap = 10 // bu da noktanın yarıçapı

den çizgiler = Küme[Çizgi]() // boş küme olarak başlarız
den noktalar = Küme[Nokta]()
den noktalar2 = Yöney[Nokta]() // dışsal (ya da içsel) nokta eklemek için gerekli
