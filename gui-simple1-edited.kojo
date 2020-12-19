// http://www.kogics.net/public/kojolite/samples/quickref/widgets-picture-sample.kojo

// An example showing the use of user-interface widgets
// (as pictures) in the canvas

val reptf = TextField(5) //> val reptf: net.kogics.kojo.widget.TextField[Int] = net.kogics.kojo.widget.TextField[,0,0,0x0,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@3c8022ef,flags=288,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=165,g=205,b=255],columns=6,columnWidth=0,command=,horizontalAlignment=LEADING]
val linef = TextField(60) //> val linef: net.kogics.kojo.widget.TextField[Int] = net.kogics.kojo.widget.TextField[,0,0,0x0,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@3c8022ef,flags=288,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=165,g=205,b=255],columns=6,columnWidth=0,command=,horizontalAlignment=LEADING]
val angletf = TextField(360 / 5) //> val angletf: net.kogics.kojo.widget.TextField[Int] = net.kogics.kojo.widget.TextField[,0,0,0x0,invalid,layout=javax.swing.plaf.basic.BasicTextUI$UpdateHandler,alignmentX=0.0,alignmentY=0.0,border=com.apple.laf.AquaTextFieldBorder@3c8022ef,flags=288,maximumSize=,minimumSize=,preferredSize=,caretColor=javax.swing.plaf.ColorUIResource[r=0,g=0,b=0],disabledTextColor=javax.swing.plaf.ColorUIResource[r=128,g=128,b=128],editable=true,margin=javax.swing.plaf.InsetsUIResource[top=0,left=0,bottom=0,right=0],selectedTextColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=0,g=0,b=0],selectionColor=com.apple.laf.AquaImageFactory$SystemColorProxy[r=165,g=205,b=255],columns=6,columnWidth=0,command=,horizontalAlignment=LEADING]
val colordd = DropDown("blue", "green", "yellow") //> val colordd: net.kogics.kojo.widget.DropDown[String] = net.kogics.kojo.widget.DropDown[,0,0,0x0,invalid,layout=com.apple.laf.AquaComboBoxUI$AquaComboBoxLayoutManager,alignmentX=0.0,alignmentY=0.0,border=,flags=16777536,maximumSize=,minimumSize=,preferredSize=,isEditable=false,lightWeightPopupEnabled=true,maximumRowCount=8,selectedItemReminder=blue]
val colors = Map(
  "blue" -> Color(0, 0, 255, 12),
  "green" -> Color(0, 255, 0, 127),
  "yellow" -> yellow
) //> val colors: scala.collection.immutable.Map[String,java.awt.Color] = Map(blue -> java.awt.Color[r=0,g=0,b=255], green -> java.awt.Color[r=0,g=255,b=0], yellow -> java.awt.Color[r=255,g=255,b=0])

val instructions =
  <html>
To run the example, specify the required <br/>
values in the fields below, and click on the <br/>
<strong><em>Make + animate</em></strong> button at the bottom.
  </html>.toString //> val instructions: String = | <html> | To run the example, specify the required <br/> | values in the fields below, and click on the <br/> | <strong><em>Make + animate</em></strong> button at the bottom. |   </html>

val panel = ColPanel(
  RowPanel(Label(instructions)),
  RowPanel(Label("Repeat count: "), reptf),
  RowPanel(Label("Angle between lines: "), angletf),
  RowPanel(Label("Fill color: "), colordd),
  RowPanel(Label("Line size: "), linef),
  RowPanel(Button("Fire a shape") {
    val velocity = 50 // pixels per second
    val cbx = canvasBounds.x
    val figure =
      trans(cbx, 0) * fillColor(colors(colordd.value)) -> Picture {
        repeat(reptf.value) {
          forward(linef.value)
          right(angletf.value)
        }
      }
    draw(figure)
    val starttime = epochTime
    figure.react { self =>
      val ptime = epochTime
      val t1 = ptime - starttime
      val d = cbx + velocity * t1
      self.setPosition(d, 0)
    }
  })
) //> val panel: net.kogics.kojo.widget.ColPanel = net.kogics.kojo.widget.ColPanel[,0,0,0x0,invalid,layout=javax.swing.BoxLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=9,maximumSize=,minimumSize=,preferredSize=]

val pic = PicShape.widget(panel) //> val pic: net.kogics.kojo.picture.SwingPic = SwingPic (Id: 1268837805)
cleari()
draw(trans(canvasBounds.x, canvasBounds.y) -> pic)
