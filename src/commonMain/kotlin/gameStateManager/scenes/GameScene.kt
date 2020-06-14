package gameStateManager.scenes

import com.soywiz.klock.seconds
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import entities.ExperiencePoint
import entities.Player
import entities.SpawningManager
import entities.projectiles.PlayerBullet
import gameStateManager.GameDependency
import org.jbox2d.common.Vec2
import kotlin.random.Random

class GameScene(private val myDependency: GameDependency) : Scene() {
    var playerEarlierPosition = Vec2()

    override suspend fun Container.sceneInit() {
        var bullets = ArrayList<PlayerBullet>()
        val windowSize = Size(1280, 720)

        text("Test Game: ${myDependency.value}")
        solidRect(100.0, 100.0, Colors.BLUE).position(containerRoot.width / 2, containerRoot.height / 2).center().onClick {
            launchImmediately {
                sceneDestroy()
                removeAllComponents()
                sceneContainer.changeTo<MainMenuScene>(GameDependency("MainMenu"))
            }

        }

//        val music = resourcesVfs["sound\\Cairo_Braga_-_07_-_Goodbye_Outer_Space.mp3"].readMusic()
//        music.playForever()

        val backgroundHandler = BackgroundHandler(resourcesVfs["animations/background/space2_4-frames.png"].readBitmap())
//        val backgroundHandler = BackgroundHandler(resourcesVfs["stars.png"].readBitmap())
        val background = resourcesVfs["animations/background/space1_4-frames.png"].readBitmap()
//        val background = resourcesVfs["stars.png"].readBitmap()
        for (xPos in 0..windowSize.width.toInt() step background.width) {
            for (yPos in 0..windowSize.height.toInt()+background.height step background.height) {
                val background = backgroundHandler.createAnimation(Vec2(xPos.toFloat(), yPos.toFloat()))
                addChild(background)
                background.addUpdater {
                    if (-parent?.x!! - width > x) {
                        x += width * 6
                    }

                    if (-parent?.x!! + windowSize.width.toInt() < x) {
                        x -= width * 6
                    }


                    if (-parent?.y!! > y) {
                        y += height * 4
                    }

                    if (-parent?.y!! + windowSize.height.toInt() < y) {
                        y -= height * 4
                    }
                }
            }
        }

/*        val animation1 = Sprite(SpriteAnimation(
                spriteMap = resourcesVfs["animations/background/space1_4-frames.png"].readBitmap(),
                spriteWidth = backgroundHandler.spriteSize,
                spriteHeight = backgroundHandler.spriteSize,
                columns = 4,
                rows = 1))
        animation1.scale(backgroundHandler.scale)
        animation1.playAnimationLooped(spriteDisplayTime = 400.milliseconds)
        animation1.center()
        addChildAt(animation1,0)*/

        val player = Player(resourcesVfs["character/pitrizzo-SpaceShip-gpl3-opengameart-96x96.png"].readBitmap(), views)

        val xp = ExperiencePoint(resourcesVfs["exp.png"].readBitmap(), player).position(100, 100)
        addChild(xp)

        //we need to center the camera on the player
        addChild(player)
        //testing enemy

        /*
        fun calculateVectorDistance(firstVector: Vec2, secondVec2: Vec2): Float = sqrt(
                (firstVector.x - secondVec2.x) * (firstVector.x - secondVec2.x)
                        + (firstVector.y - secondVec2.y) * (firstVector.y - secondVec2.y))
        //used to prevent over refreshing
        fun checkForOutsideRenderRadius() {
            if (calculateVectorDistance(playerEarlierPosition, Vec2(player.x.toFloat(), player.y.toFloat()))
                    > backgroundHandler.spriteSize * backgroundHandler.scale * 0.1)  //change the float to change how much the player has top move for the game to update
            {
                backgroundHandler.updateSpritePos(Vec2(player.x.toFloat(), player.y.toFloat()), this)
                playerEarlierPosition = Vec2(player.x.toFloat(), player.y.toFloat())
            }
        }
        var job2 = launchImmediately {
            while (true) {
                checkForOutsideRenderRadius()
                delay(150.milliseconds)
            }
       
        */
        var timeMS = 0
        val cameraLag = 25
        addHrUpdater {
            position(-player.x + windowSize.width/2 + player.velocity.x / cameraLag, -player.y + windowSize.height/2 + player.velocity.y / cameraLag)
            timeMS = it.millisecondsInt
        }

        val minSpawningRange = 800
        val maxSpawningRange = 1200
        val minSpawningTime = 2
        val maxSpawningTime = 3

        launchImmediately {
            while (true) {
                val spawnPos = Point(player.x + cos(timeMS.radians) * Random.nextInt(minSpawningRange, maxSpawningRange), player.y + sin(timeMS.radians) * Random.nextInt(minSpawningRange, maxSpawningRange))
                SpawningManager.spawnRangedEnemy(spawnPos.x, spawnPos.y, views, player, this)
                delay(Random.nextInt(minSpawningTime, maxSpawningTime).seconds)
            }
        }
        launchImmediately {
            while (true) {
                val spawnPos = Point(player.x + cos(timeMS.radians) * Random.nextInt(minSpawningRange, maxSpawningRange), player.y + sin(timeMS.radians) * Random.nextInt(minSpawningRange, maxSpawningRange))
                SpawningManager.spawnTrackingEnemy(spawnPos.x, spawnPos.y, views, player, this)
                delay(Random.nextInt(minSpawningTime, maxSpawningTime).seconds)
            }
        }
    }
}