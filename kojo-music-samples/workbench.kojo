import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import net.kogics.kojo.music._

// toggleFullScreenCanvas()
cleari()
clearOutput()
disablePanAndZoom()

val cb = canvasBounds

val offWhite = Color(0xF2F5F1)
val bluem = Color(0x1356A2)
val noteFill = bluem
val boundaryClr = black

setBackground(offWhite)

val tempo = 200

case class Bar(ons: Array[Boolean])

case class PhraseLine(note: Note, bars: Seq[Bar]) {
    def phrase: Phrase = {
        val elems = for {
            bar <- bars
            on <- bar.ons
            elem = if (on) note else Rest()
        } yield elem

        Phrase(elems: _*)
    }
}

case class NoteState(line: PhraseLine, barIdx: Int, pos: Int) {
    def on: Boolean = line.bars(barIdx).ons(pos)
    def toggleOn() {
        val ons = line.bars(barIdx).ons
        val on = ons(pos)
        ons(pos) = !on
    }
}

def notePic(line: PhraseLine, barIdx: Int, pos: Int) = {
    val picBack = Picture.rectangle(30, 30).withFillColor(noteFill).withPenColor(boundaryClr)
    if (pos == 0) picBack.setPenThickness(4)
    val picFront = Picture.rectangle(20, 20)
    picFront.setPenColor(noteFill)
    val noteState = NoteState(line, barIdx, pos)
    picFront.setFillColor(if (noteState.on) cm.lightBlue else noteFill)
    val pic = picStackCentered(picBack, picFront)
    pic.onMouseClick { (x, y) =>
        noteState.toggleOn()
        picFront.setFillColor(if (noteState.on) cm.lightBlue else noteFill)
        stopMusic()
    }
    pic
}

def barPic(line: PhraseLine, barIdx: Int) = picRow(
    notePic(line, barIdx, 0),
    notePic(line, barIdx, 1),
    notePic(line, barIdx, 2),
    notePic(line, barIdx, 3)
)

def noteLabelPic(note: Note) = {
    picStackCentered(
        Picture.rectangle(100, 10).withPenColor(noColor),
        picRow(
            Picture.text(NoteNames.pitchToNoteName(note.pitch)).withPenColor(black),
            Picture.hgap(3),
            Picture.text("/"),
            Picture.hgap(3),
            Picture.text(NoteNames.pitchToSwaraName(note.pitch)).withPenColor(black)
        )
    )
}

def percussionLabelPic(note: Note) = {
    picStackCentered(
        Picture.rectangle(100, 10).withPenColor(noColor),
        Picture.text("Drums").withPenColor(black)
    )
}

def linePic(line: PhraseLine) = picRowCentered(
    if (line == linePerc) percussionLabelPic(line.note) else noteLabelPic(line.note),
    Picture.hgap(6),
    barPic(line, 0),
    barPic(line, 1),
    barPic(line, 2),
    barPic(line, 3)
)

def bar = Bar(Array(false, false, false, false))

def line(note: Note) = PhraseLine(note, Seq(bar, bar, bar, bar))

var linePerc = line(Note(35))

val notes = Seq(48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72)

var lines = notes.map(p => line(Note(p)))

def linesPicMaker = picCol(
    lines.map(linePic(_))
)

def scorePicMaker = picCol(
    linesPicMaker,
    Picture.vgap(30),
    linePic(linePerc)
)

def currentScore: Score = {
    Score(
        tempo,
        Part.percussion(
            linePerc.phrase
        ),
        Part(
            InstrumentNames.nameToPC(currInstrument1Dd.value),
            nonBlankLines(lines.take(7)).map(_.phrase): _*
        ),
        Part(
            InstrumentNames.nameToPC(currInstrument2Dd.value),
            nonBlankLines(lines.drop(7)).map(_.phrase): _*
        )
    )
}

def startMusicServerButton: Button = {
    val btn = Button("Srv Up") {
        if (!MusicPlayer.started) {
            println("\nStarting music server. This might take a few seconds...")
            schedule(0.1) {
                MusicPlayer.startAsNeeded(true)
                currMsStartBtn.setEnabled(false)
                currMsStopBtn.setEnabled(true)
                currRunButton.setEnabled(true)
            }
        }
    }
    btn.setEnabled(!MusicPlayer.started)
    btn
}

def stopMusicServerButton = {
    val btn = Button("Srv Dn") {
        if (MusicPlayer.started) {
            stopMusic()
            MusicPlayer.stop()
            currMsStartBtn.setEnabled(true)
            currMsStopBtn.setEnabled(false)
            currRunButton.setEnabled(false)
        }
    }
    btn.setEnabled(MusicPlayer.started)
    btn
}

def runMusic() {
    if (!MusicPlayer.started) {
        println("Start the music server before Running.")
        return
    }

    MusicPlayer.playLoop(currentScore)

    currRunButton.setEnabled(false)
    currStopButton.setEnabled(true)
}

def stopMusic() {
    MusicPlayer.stopPlaying()
    currStopButton.setEnabled(false)
    currRunButton.setEnabled(true)
}

def runButton = Button("Play") {
    runMusic()
}

def stopButton = Button("Stop") {
    stopMusic()
}

val prefs = builtins.kojoCtx.asInstanceOf[net.kogics.kojo.lite.KojoCtx].prefs
val prefsKey = "kojo.music.dir"

def lastLoadSaveDir: Option[String] = {
    val dir = prefs.get(prefsKey, null)
    if (dir == null) None else Some(dir)
}

def updateLastLoadSaveDir(dir: String) {
    prefs.put(prefsKey, dir)
}

def saveButton = Button("Save") {
    import java.io._
    def chosenFile(): Option[File] = {
        import javax.swing.JFileChooser
        import javax.swing.filechooser.FileNameExtensionFilter
        val chooser = new JFileChooser()
        val filter = new FileNameExtensionFilter("Music Score", "score")
        chooser.setFileFilter(filter)
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
        chooser.setDialogTitle("Save Music Score")
        lastLoadSaveDir.foreach { dirStr =>
            val dir = new File(dirStr)
            if (dir.exists() && dir.isDirectory()) {
                chooser.setCurrentDirectory(dir)
            }
        }
        val returnVal = chooser.showSaveDialog(kojoCtx.frame)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            var selectedFile = chooser.getSelectedFile()
            updateLastLoadSaveDir(selectedFile.getParent)
            if (!selectedFile.getName().contains(".")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".score")
            }
            Some(selectedFile)
        }
        else {
            None
        }
    }
    val fileToSave = chosenFile()
    if (fileToSave.isDefined) {
        val oos = new ObjectOutputStream(new FileOutputStream(fileToSave.get))
        def writePhraseLine(line: PhraseLine) {
            oos.writeInt(line.note.pitch)
            line.bars.foreach { bar =>
                val bons: Array[Byte] =
                    bar.ons.map(b => if (b) 1.toByte else 0.toByte).toArray
                oos.write(bons)
            }
        }
        writePhraseLine(linePerc)
        lines.foreach { line =>
            writePhraseLine(line)
        }
        oos.close()
    }
}

def loadButton: Button = Button("Load") {
    import java.io._
    def chosenFile(): Option[File] = {
        import javax.swing.JFileChooser
        import javax.swing.filechooser.FileNameExtensionFilter
        val chooser = new JFileChooser()
        val filter = new FileNameExtensionFilter("Music Score", "score")
        chooser.setFileFilter(filter)
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
        chooser.setDialogTitle("Open Music Score")
        lastLoadSaveDir.foreach { dirStr =>
            val dir = new File(dirStr)
            if (dir.exists() && dir.isDirectory()) {
                chooser.setCurrentDirectory(dir)
            }
        }
        val returnVal = chooser.showOpenDialog(kojoCtx.frame)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val selectedFile = chooser.getSelectedFile()
            updateLastLoadSaveDir(selectedFile.getParent)
            Some(selectedFile)
        }
        else {
            None
        }
    }
    val fileToOpen = chosenFile()
    if (fileToOpen.isDefined) {
        val oos = new ObjectInputStream(new FileInputStream(fileToOpen.get))
        def readPhraseLine(): PhraseLine = {
            val pitch = oos.readInt()
            val bars = (0 until 4).map { _ =>
                val ba = new Array[Byte](4)
                oos.read(ba)
                val boolOns = ba.map(byte => if (byte == 1) true else false)
                Bar(boolOns)
            }
            PhraseLine(Note(pitch), bars)
        }
        linePerc = readPhraseLine()
        val num = lines.length
        lines = (0 until num).map { _ =>
            readPhraseLine()
        }
        oos.close()
        currentUi.erase()
        currentUi = ui
        drawCentered(currentUi)
    }
}

def nonBlankLines(lines: Seq[PhraseLine]): Seq[PhraseLine] =
    lines.filter { phraseLine =>
        phraseLine.bars.find { bar =>
            bar.ons.find { on =>
                on
            }.isDefined
        }.isDefined
    }

def exportButton: Button = Button("Export") {
    println(currentScore)
}

def updateSoundfontButton = {
    import java.io.File
    import java.nio.file.Files
    import java.nio.file.StandardCopyOption.REPLACE_EXISTING
    import net.kogics.kojo.util.Utils.isWin

    val audioDirName =
        if (isWin) s"$installDir/jre/lib/audio"
        else s"$homeDir/.gervill"

    val sfFile =
        new File(s"$homeDir/.kojo/extension/kojo-music/soundfont/FluidR3_GM.sf2")

    val javaSfFile =
        new File(s"$audioDirName/soundbank-emg.sf2")

    val btn = Button("Update Soundfont") {
        val audioDir = new File(audioDirName)

        if (!audioDir.exists) {
            println(s"Creating dir for soundfont file - ${audioDir.getAbsolutePath}")
            audioDir.mkdir()
        }

        println("Updating Java default soundfont file...")
        try {
            Files.copy(sfFile.toPath, javaSfFile.toPath, REPLACE_EXISTING)
            println("Update completed.")
            println("Restart Kojo for change to take effect.")
        }
        catch {
            case t: Throwable =>
                println(s"Problem - ${t.getMessage}")
        }
    }

    if (sfFile.exists) {
        if (sfFile.length == javaSfFile.length) {
            btn.setEnabled(false)
        }
        else {
            btn.setEnabled(true)
        }
    }
    else {
        btn.setEnabled(false)
    }
    btn
}

val vertGap = 3

var currMsStartBtn: Button = _
var currMsStopBtn: Button = _
var currRunButton: Button = _
var currStopButton: Button = _
var currInstrument1Dd: DropDown[String] = _
var currInstrument2Dd: DropDown[String] = _

def controlPanel = {
    currMsStartBtn = startMusicServerButton
    currMsStopBtn = stopMusicServerButton
    currRunButton = runButton
    currStopButton = stopButton
    currRunButton.setEnabled(MusicPlayer.started)
    currStopButton.setEnabled(false)
    currInstrument1Dd = DropDown(InstrumentNames.names: _*)
    currInstrument1Dd.setSelectedItem("Guitar (Nylon)")
    currInstrument2Dd = DropDown(InstrumentNames.names: _*)
    currInstrument2Dd.setSelectedItem("Piano")
    ColPanel(
        RowPanel(currRunButton, RowPanel.horizontalGap(10), currStopButton),
        ColPanel.verticalGap(vertGap),
        RowPanel(saveButton, RowPanel.horizontalGap(10), loadButton),
        ColPanel.verticalGap(vertGap),
        RowPanel(exportButton),
        ColPanel.verticalGap(vertGap * 3),
        ColPanel(
            RowPanel(Label(" Instrument 1:")),
            RowPanel(currInstrument1Dd),
            RowPanel(Label(" Instrument 2:")),
            RowPanel(currInstrument2Dd)
        ),
        ColPanel.verticalGap(vertGap * 15),
        RowPanel(updateSoundfontButton),
        ColPanel.verticalGap(vertGap),
        RowPanel(currMsStartBtn, RowPanel.horizontalGap(10), currMsStopBtn),
    )
}

def controls = Picture.widget(controlPanel)
def ui = picRowCentered(controls, Picture.hgap(20), scorePicMaker)

var currentUi = ui
drawCentered(currentUi)
println("If you run this program again while some music is playing, you will have to restart Kojo to stop the music that is playing from the previous run. So stop any playing music before re-running this program!")