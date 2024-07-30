private var uidCount = 0L

def getUniqueID(): Long = {
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
Vector(1, 2, 3, 4, 5, 7, 9, 11, 13, 15)
Vector(1, 2, 3, 4, 6, 8, 10, 12, 14, 16)
*/ 