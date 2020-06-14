package gameStateManager.scenes

import com.soywiz.klock.milliseconds
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.geom.div
import entities.Player
import entities.TrackingEnemy
import gameStateManager.GameDependency
import org.jbox2d.common.MathUtils.Companion.atan2
import org.jbox2d.common.Vec2
import kotlin.math.atan2
import kotlin.math.round

class GameScene(private val myDependency: GameDependency) : Scene() {

    var playerEarlierPosition = Vec2()


    override suspend fun Container.sceneInit() {
        text("Test Game: ${myDependency.value}")
        solidRect(100.0, 100.0, Colors.BLUE).position(containerRoot.width / 2, containerRoot.height / 2).center().onClick {
            launchImmediately {
                sceneDestroy()
                removeAllComponents()
                sceneContainer.changeTo<MainMenuScene>(GameDependency("MainMenu"))
            }

        }

        val backgroundHandler = BackgroundHandler(resourcesVfs["animations/background/space1_4-frames.png"].readBitmap())
        backgroundHandler.createBackgroundTiles(this)

        val sprite = resourcesVfs["animations/background/space1_4-frames.png"].readBitmap()
        for (xPos in 0..containerRoot.width.toInt() step sprite.width) {
            for (yPos in 0..containerRoot.height.toInt()+sprite.height step sprite.height) {
                val background = backgroundHandler.createAnimation(Vec2(xPos.toFloat(), yPos.toFloat()))
                addChild(background)
                background.addUpdater {
                    if (-parent?.x!! - width > x) {
                        x += width * 6
                    }

                    if (-parent?.x!! + containerRoot.width < x) {
                        x -= width * 6
                    }


                    if (-parent?.y!! > y) {
                        y += height * 4
                    }

                    if (-parent?.y!! + containerRoot.height < y) {
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

        val player = Player(resourcesVfs["character\\pitrizzo-SpaceShip-gpl3-opengameart-96x96.png"].readBitmap(), views)

        //we need to center the camera on the player
        addChild(player)
        //testing enemy
        val explosionAnimation = SpriteAnimation(
                spriteMap = resourcesVfs["homing_enemy.png"].readBitmap(),
                spriteWidth = 48,
                spriteHeight = 48,
                columns = 9,
                rows = 1
        )
        val testTrackingEnemy = TrackingEnemy(explosionAnimation, views, player)
        testTrackingEnemy.xy(100,100)
        addChild(testTrackingEnemy)

        var job = launchImmediately {
            while (true) {
                testTrackingEnemy.trackPlayer(Vec2(player.x.toFloat(), player.y.toFloat()))
                delay(100.milliseconds)

            }
        }

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
        }
        */

        val cameraLag = 25
        addHrUpdater {
            position(-player.x + containerRoot.width / 2 + player.velocity.x / cameraLag, -player.y + containerRoot.height / 2 + player.velocity.y / cameraLag)
        }


    }
}