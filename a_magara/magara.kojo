kojoVarsayılanİkinciBakışaçısınıKur()
çıktıyıSil()
silVeSakla()
yaklaşmayaİzinVerme()

satıryaz("Oyunu kazanmak için kırmızı yuvarlağı bul ve al. Sonra da başa dönüp yeşil yuvarlağı al.")
satıryaz(s"\nKendi mağaranı kurmak için, '$kurulumDizini/examples/tiledgame/level1.tmx' dosyasını www.mapeditor.org sitesindeki Tiled düzenleyicisiyle değiştirebilirsin.")

scroll(-tuvalAlanı.x, tuvalAlanı.y)  // todo: tuvaliKaydır
artalanıKur(renkler.hsl(189, 0.03, 0.45))

// Tiled map layer of tiles that you collide with
val collisionLayer = 1

class Oyuncu(tx: Int, ty: Int, world: TileWorld) {
    val playerPos = world.tileToKojo(TileXY(tx, ty))
    val sheet = SpriteSheet("player.png", 30, 42)

    // player images are 30x40
    // scale the player down to fit into a 24 pixel wide tile
    def playerPicture(img: Image) = büyüt(0.8) -> Resim.imge(img)

    val stillRight = Resim.küme(playerPicture(sheet.imageAt(0, 0)))
    val stillLeft = Resim.küme(playerPicture(sheet.imageAt(0, 1)))

    val runningRight = Resim.küme(List(
        sheet.imageAt(0, 2),
        sheet.imageAt(1, 2),
        sheet.imageAt(2, 2),
        sheet.imageAt(3, 2),
        sheet.imageAt(4, 2)
    ).map(playerPicture))

    val runningLeft = Resim.küme(List(
        sheet.imageAt(0, 3),
        sheet.imageAt(1, 3),
        sheet.imageAt(2, 3),
        sheet.imageAt(3, 3),
        sheet.imageAt(4, 3)
    ).map(playerPicture))

    val jumpingRight = Resim.küme(List(
        sheet.imageAt(0, 0),
        sheet.imageAt(1, 0),
        sheet.imageAt(2, 0),
        sheet.imageAt(3, 0)
    ).map(playerPicture))

    val jumpingLeft = Resim.küme(List(
        sheet.imageAt(0, 1),
        sheet.imageAt(1, 1),
        sheet.imageAt(2, 1),
        sheet.imageAt(3, 1)
    ).map(playerPicture))

    var ancılResim = stillRight
    def currentPic = ancılResim.p // todo
    ancılResim.konumuKur(playerPos)

    var facingRight = true
    val gravity = -0.1
    val speedX = 3.0
    var speedY = -1.0
    var inJump = false

    def step() {
        stepCollisions()
        stepFood()
    }

    var goalEnabled = false
    def stepFood() {
        if (ancılResim.çarptıMı(halfwayGoal)) {
            halfwayGoal.sil()
            goal.saydamlığıKur(1)
            goalEnabled = true
        }
        if (goalEnabled) {
            if (ancılResim.çarptıMı(goal)) {
                goal.sil()
                stopAnimation()
                çizMerkezdeYazı("Tebrikler!", yeşil, 30)
            }
        }
    }

    def stepCollisions() {
        if (isKeyPressed(Kc.VK_RIGHT)) {
            facingRight = true
            updateImage(runningRight)
            ancılResim.götür(speedX, 0)
            if (world.hasTileAtRight(currentPic, collisionLayer)) { // todo
                world.moveToTileLeft(currentPic)
            }
        }
        else if (isKeyPressed(Kc.VK_LEFT)) {
            facingRight = false
            updateImage(runningLeft)
            ancılResim.götür(-speedX, 0)
            if (world.hasTileAtLeft(currentPic, collisionLayer)) {
                world.moveToTileRight(currentPic)
            }
        }
        else {
            if (facingRight) {
                updateImage(stillRight)
            }
            else {
                updateImage(stillLeft)
            }
        }

        if (isKeyPressed(Kc.VK_UP)) {
            if (!inJump) {
                speedY = 5
            }
        }

        speedY += gravity
        speedY = math.max(speedY, -10)
        ancılResim.götür(0, speedY)

        if (world.hasTileBelow(currentPic, collisionLayer)) {
            inJump = false
            world.moveToTileAbove(currentPic)
            speedY = 0
        }
        else {
            inJump = true
            if (world.hasTileAbove(currentPic, collisionLayer)) {
                world.moveToTileBelow(currentPic)
                speedY = -1
            }
        }

        if (inJump) {
            if (facingRight) {
                updateImage(jumpingRight)
            }
            else {
                updateImage(jumpingLeft)
            }
            ancılResim.sonrakiniGöster(200)
        }
        else {
            ancılResim.sonrakiniGöster()
        }
        scrollIfNeeded()
    }

    var cb = canvasBounds
    def scrollIfNeeded() {
        val threshold = 200
        val pos = ancılResim.konum
        if (cb.x + cb.width - pos.x < threshold) {
            scroll(speedX, 0)
            cb = canvasBounds
        }
        else if (pos.x - cb.x < threshold) {
            scroll(-speedX, 0)
            cb = canvasBounds
        }
    }

    def updateImage(newPic: Resim) {
        if (newPic != ancılResim) {
            ancılResim.gizle()
            newPic.göster()
            newPic.konumuKur(ancılResim.konum)
            ancılResim = newPic
        }
    }

    def çiz() {
        çizVeSakla(stillLeft, runningRight, runningLeft, jumpingRight, jumpingLeft)
        ancılResim.çiz()
    }
}

class İnenÇıkanTaşlar(tx: Int, ty: Int, world: TileWorld) {
    val playerPos = world.tileToKojo(TileXY(tx, ty))
    val sheet = SpriteSheet("tiles.png", 24, 24)
    // make attacker slighty smaller than a tile - to prevent picture based collision
    // with the player in an adjacent tile
    def attackerPicture(img: Image) = büyüt(0.98) * götür(0.2, 0.2) -> Resim.imge(img)

    var ancılResim = Resim.küme(List(
        sheet.imageAt(0, 6),
        sheet.imageAt(1, 6)
    ).map(attackerPicture))
    def currentPic = ancılResim.p // todo

    ancılResim.konumuKur(playerPos)

    val gravity = -0.03
    //    var speedX = 0.0
    var speedY = -2.0

    def step() {
        speedY += gravity
        speedY = math.max(speedY, -10)
        ancılResim.götür(0, speedY)
        ancılResim.sonrakiniGöster()
        if (world.hasTileBelow(currentPic, collisionLayer)) {
            world.moveToTileAbove(currentPic)
            speedY = 5
        }
        else if (world.hasTileAbove(currentPic, collisionLayer)) {
            world.moveToTileBelow(currentPic)
            speedY = -2
        }
    }

    def updateImage(newPic: Resim) {
        if (newPic != ancılResim) {
            ancılResim.gizle()
            newPic.göster()
            newPic.konumuKur(ancılResim.konum)
            ancılResim = newPic
        }
    }

    def çiz() {
        ancılResim.çiz()
    }
}

val tileWorld =
    new TileWorld("level1.tmx")

// Create a player object and set the level it is in
val player = new Oyuncu(9, 5, tileWorld)
val attackers = Dizin(
    new İnenÇıkanTaşlar(14, 2, tileWorld),
    new İnenÇıkanTaşlar(17, 3, tileWorld),
    new İnenÇıkanTaşlar(22, 9, tileWorld),
    new İnenÇıkanTaşlar(32, 2, tileWorld),
    new İnenÇıkanTaşlar(35, 3, tileWorld)
)

val goal = götür(12, 12) * boyaRengi(cm.greenYellow) * kalemRengi(black) -> Resim.daire(10)
goal.konumuKur(tileWorld.tileToKojo(TileXY(9, 2)))
goal.saydamlığıKur(0.2)
çiz(goal)

val halfwayGoal = götür(12, 12) * boyaRengi(cm.red) * kalemRengi(black) -> Resim.daire(10)
halfwayGoal.konumuKur(tileWorld.tileToKojo(TileXY(41, 15)))
çiz(halfwayGoal)

tileWorld.draw()
player.çiz()
attackers.foreach { attacker =>
    attacker.çiz()
}

canlandır {
    tileWorld.step()
    player.step()
    attackers.foreach { attacker =>
        attacker.step()
        if (player.ancılResim.çarptıMı(attacker.ancılResim)) {
            player.ancılResim.döndür(30)
            durdur()
            çizMerkezdeYazı("Çarptın. Tekrar dene!", kırmızı, 30)
        }
    }
}

tuvaliEtkinleştir()

// oyun kaynaklarını şuradan aldık: https://github.com/pricheal/pygame-tiled-demo
