val fileName = "./bellek.kojo"
val bufferedSource = scala.io.Source.fromFile(fileName)
var cnt = 0
for (lines <- bufferedSource.getLines()) {
    // translate
    println(s"${lines.size}")
    cnt += 1
}
bufferedSource.close()
println(s"line count: $cnt")
