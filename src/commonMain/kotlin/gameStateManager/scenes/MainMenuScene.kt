package gameStateManager.scenes

import com.soywiz.klock.milliseconds
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korge.view.filter.BlurFilter
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korio.async.delay
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import gameStateManager.GameDependency

class MainMenuScene(private val myDependency: GameDependency) : Scene() {

    override suspend fun Container.sceneInit() {

        var blur = 20.0
        filter = BlurFilter(blur)

        val font1 = resourcesVfs["font//font1.fnt"].readBitmapFont()

        text("Test Main Menu: ${myDependency.value}", font = font1)
        //NOTE: the solidRect that is not deprecated requires Double parameters for width and height
        solidRect(100.0, 100.0, Colors.RED).position(containerRoot.width/3*2,containerRoot.height/2).center().onClick {
            launchImmediately {
                println("Switching to game")
                sceneDestroy()
                sceneContainer.changeTo<GameScene>(GameDependency("Game"))

            }

        }
        launchImmediately {
            while (blur>0) {
                filter = BlurFilter(blur)
                blur -= 0.3
                delay(20.milliseconds)
            }
        }
    }

}