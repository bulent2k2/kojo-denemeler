// üçgen desenle kaplama (delaunay triangulation)
// her tıklama yeni bir nokta ekler
// noktaları üçgenlerle bağlıyoruz
// son noktayı silmek için sola bakan ok (yani geri gitme tuşuna) 
// ya da z tuşuna bas. z'ye bastıkça silmeye devam eder
// silinen son noktayı geri koymak için sağa bakan oka (yani ileri gitme tuşuna)
// ya da c'ye bas. c'ye bastıkça geri koymaya devam eder
// silinen noktaları tamamen unutmak için x'e bas.

cleari()
clearOutput()
setBackground(white)
disablePanAndZoom()
val cb = canvasBounds
val points = ArrayBuffer(Point(-100, -50), Point(100, -50), Point(-100, 50))

onMouseClick { (x, y) =>
    points.append(Point(x, y))
    println(s"Şimdi ${points.size} nokta var. Yeni nokta: ($x, $y)")
    if (points.size > 2) {
        drawTriangles()
    }
}

import collection.mutable.Stack
val removedPoints = Stack.empty[Point]
def pushToStack(p: Point) = removedPoints.push(p)
def popFromStack() = removedPoints.pop
def emptyStack() = removedPoints.size == 0

onKeyPress { key =>
    {
        def back = {
            removeLastPoint()
            println(points.size + " nokta kaldı")
        }
        def fwd = if (emptyStack())
            println("Başka silinmiş nokta kalmadı")
        else {
            val oldPoint = popFromStack()
            points.append(oldPoint)
            println(s"${points.size} noktaya çıktı")
            if (points.size > 2) {
                drawTriangles()
            }
        }
        key match {
            case 37 => back // arrow left
            case 90 => back // z
            case 39 => fwd // arrow right
            case 67 => fwd // c
            case 88 => { // pressed x
                while (!emptyStack) removedPoints.pop
                println("Çöpü boşalttık")
            }
            case a => println(s"$a nolu tuşa bastın")
        }
    }
}
def removeLastPoint() {
    if (points.size > 0) {
        pushToStack(points(points.size - 1))
        points.remove(points.size - 1)
    }
    if (points.size > 2)
        drawTriangles()
    else
        erasePictures()
}
def drawTriangles() {
    erasePictures()
    val trs = triangulate(points)
    println(s"${trs.size} üçgen var")
    trs.foreach { t =>
        val pic = Picture {
            val lg = cm.linearGradient(t.a.x, t.a.y, black, t.b.x, t.b.y, blue)
            setFillColor(lg)
            setPenColor(gray)
            setPosition(t.a.x, t.a.y)
            lineTo(t.b.x, t.b.y)
            lineTo(t.c.x, t.c.y)
        }
        draw(pic)
    }
}

val message1 = penColor(black) -> Picture.text("Tıklayınca başlar", 40)
val message2 = penColor(black) -> Picture.text("Tıkladıkça devam eder", 30)

val msg = picColCentered(message2, Picture.vgap(20), message1)
drawCentered(msg)
