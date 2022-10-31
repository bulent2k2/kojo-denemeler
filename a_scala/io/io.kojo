import java.io.{File, FileInputStream, PrintWriter}
// https://stackoverflow.com/questions/4604237/how-to-write-to-a-file-in-scala

object input {
  def fromFile(path: String): List[String] = {
    val file = new File(path)
    if (!file.exists) {
      throw new Exception("Could not open file at " + path)
    } else {
      val istream = new FileInputStream(file)
      try {
        val s = scala.io.Source.fromInputStream(istream)
        println(s"Reading from $path")
        s.getLines.toList // return value here
      }
      catch {
        case e: Exception =>
          println("Could not load word list" + e)
          throw e
      }
      finally {
        istream.close
      }
    }
  }
}

object output {
  def toFile[T](list: List[T])(path: String) = write(new File(path)) { p => list.foreach(p.println) }

  // overwrites the file, if it exists! Otherwise, simply creates it
  def write(f: java.io.File)(op: PrintWriter => Unit) {
    println(s"Writing to ${f.getAbsolutePath}")
    val p = new PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  def sampleUsage() = {
    val data = List("Six", "strings", "written", "to", "a", "file!")
    write(new File("/Users/ben/example.txt")) { p =>
      data.foreach(p.println)
    }
  }
  // sampleUsage()
}
