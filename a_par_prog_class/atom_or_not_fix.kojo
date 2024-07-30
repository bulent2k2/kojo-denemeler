private var x = new AnyRef {}
private var uidCount = 0L
def getUniqueID(): Long = x.synchronized {
    uidCount = uidCount + 1
    uidCount
}

def startThread() = {
    val t = new Thread {
        override def run() {
            val uids = for (i <- 0 until 10) yield getUniqueID()
            println(uids)
        }
    }
    t.start()
    t
}

startThread()
startThread()
/*
Vector(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
*/ 