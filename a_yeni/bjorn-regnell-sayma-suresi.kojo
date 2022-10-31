çıktıyıSil()
sayıyaKadarSay(5000)
sayıyaKadarSay(1000000)
for (i <- 0 |- 9) {
    println(f"$i: $buAn $buAn2 $buSaniye%.3f $buSaniye2")
    pause(0.1)
}
zamanTut("") {
    sayıyaKadarSay(200_000_000) // ~ 3 saniye
}()
