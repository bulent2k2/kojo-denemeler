silVeSakla
gridiGöster
eksenleriGöster
dez yç = 10 // yarıçap
dez top = Resim.daire(yç) // topun resmi
top.çiz // tuvale çizelim
top.kondur(-200, -100) // sol alt köşeden harekete başlasın
// Devinim için canladır komutuyla bir döngü başlatalım:
canlandır { 
    // Bu küme içindeki komutlar saniyede yaklaşık 40 kere yinelenir
    top.kondur(top.konum.x + 2, top.konum.y + 1)
    eğer(top.konum.x >= 200) // yeterince gidince döngüyü durduralım
        durdur
}
