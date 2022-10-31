import java.io.{File, PrintWriter}
// https://stackoverflow.com/questions/4604237/how-to-write-to-a-file-in-scala

object puts {
  def writeToFile[T](list: List[T])(path: String) = write(new File(path)) { p => list.foreach(p.println) }

  // overwrites the file, if it exists! Otherwise, simply creates it
  def write(f: java.io.File)(op: PrintWriter => Unit) {
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
