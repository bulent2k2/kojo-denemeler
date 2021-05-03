val sayfaBiçimi = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
val ortalamaBiçimi = "text-align:center;"

val sayfa1 = Page(
    name = "Giriş",
    body =
        <body style={ sayfaBiçimi + ortalamaBiçimi }>
            { for (i <- 1 to 5) yield { <br/> } }
            <h3>Kojo'la bilgisayar programlamayı öğrenmek için</h3>
            şu sayfaya bakabilirsin: 
            <a href="http://docs.kogics.net">Kojo Kullanma Kılavuzu</a><br/>
            <p>http://docs.kogics.net</p>
            <br/>
            Bir sonraki sayfada da bazı Türkçe tür adlarının ve diğer terimlerın
            İngilizce karşılıklarını bulabilirsin.
        </body>
)

// Set Up Styles for tutorial pages
val pageStyle = "color:black;background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

implicit def toSHtm(s:String):SHtm={new SHtm(escTrx(s))}
def escTrx(s:String) = s.replace("&" , "&amp;").replace(">" , "&gt;").replace("<" , "&lt;")
def escRem(s:String) = s.replace("&amp;","&"  ).replace("&gt;",">").replace("&lt;","<")
def row(c:SHtm *)={
    val r=c.map(x=>{new SHtm("<td>" + x.s + "</td>")}).reduce(_ + _) 
    new SHtm("<tr>" + r.s + "</tr>")
}
def table(c:SHtm *)={
    val r=c.reduce(_ + _) 
    new SHtm("""<table border="1">""" + r.s + "</table>")
}
def tPage(title:String,h:SHtm *)={
    <body style={pageStyle}>
    <p style={headerStyle}>
        {new xml.Unparsed(title)}
        {nav}
        <hr/>
        {new xml.Unparsed(h.reduce(_ + _).s)}
    
    </p>
    </body>    
    
}

val sayfa2 = Page(
    name = "Sözlük",
    body = tPage("Türkçe İngilizce Kılavuz",
      "Temel Tür Adları ve İngilizce Karşılıkları".h2,
      table(
        row("Türkçe Terim", "İngilizce", "Açıklama"),
        row("Nesne","Object", "Her nesnenin temel sınıfı"),
        row("Birim","Unit", "Çıktı yok anlamında kullanılır")
      )
    )
)
val öykü = Story(sayfa1, sayfa2)
stClear() // öyküyü temizle
stPlayStory(öykü) // öyküyü anlatmaya başla
