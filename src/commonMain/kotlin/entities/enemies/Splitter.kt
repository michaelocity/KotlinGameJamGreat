package entities.enemies

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.*
import com.soywiz.korma.geom.plus
import com.soywiz.korma.geom.radians
import com.soywiz.korma.geom.shortDistanceTo
import com.soywiz.korma.geom.times
import entities.Player
import math.Tracking
import org.jbox2d.common.Vec2
import kotlin.math.atan2

class Splitter(val bm: SpriteAnimation, override var views: Views, player: Player, health: Int) : Enemy(bm, views, player, moveSpeed = 1f,health = health) {

    override fun updateVelocity() {

    }

    override fun updatePosition(dt: Double) {
        xy(x + velocity.x, y + velocity.y)
        if (velocity != Vec2())
            angle = atan2(velocity.y.toDouble(), velocity.x.toDouble()).radians

        rotation += rotation.shortDistanceTo(angle) * 10 * (dt / 1000)
    }

    override  fun check() {
        if (this.collidesWith(player)) {//set the image to be explosion if collided
            player.health-=5
            setFrame(1)
            scale = 4.0
            playAnimation(spriteDisplayTime = 125.milliseconds)
            render = false
            onAnimationStopped {
                removeFromParent()
            }
        }
    }


    fun Split(currentScene : Container)
    {
        currentScene.removeChild(this)
        currentScene.addChild(createSplitter())
        currentScene.addChild(createSplitter())

    }

    fun createSplitter(): Splitter = Splitter(bm, views, player, health - 1)



    fun trackPlayer(playerPosition: Vec2): Unit {

        velocity = Tracking.trackingVector(Vec2(x.toFloat(), y.toFloat()), playerPosition).mul(moveSpeed)

    }
}