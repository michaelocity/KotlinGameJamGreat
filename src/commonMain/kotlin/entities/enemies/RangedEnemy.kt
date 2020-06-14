package entities.enemies

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.Views
import com.soywiz.korge.view.collidesWith
import com.soywiz.korge.view.xy
import com.soywiz.korma.geom.*
import entities.Player
import entities.SpawningManager
import math.Tracking
import org.jbox2d.common.Vec2
import kotlin.math.atan2

class RangedEnemy (bm: SpriteAnimation, views: Views, player: Player, health: Int) : Enemy(bm, views, player, moveSpeed = 1f,health = health) {

    override fun updateVelocity() {

    }

    override fun updatePosition(dt: Double) {
        xy(x + velocity.x ,y + velocity.y )
        if (velocity != Vec2())
            angle = atan2(velocity.y.toDouble(), velocity.x.toDouble()).radians

        rotation += rotation.shortDistanceTo(angle) * 10 * (dt / 1000)
    }

    override fun check() {
        if(this.collidesWith(player)){//set the image to be explosion if collided
            player.health-=5 //fixme
            setFrame(1)
            scale = 4.0
            playAnimation(spriteDisplayTime = 125.milliseconds)
            render = false
            onAnimationStopped{
                removeFromParent()
            }
        }
    }


    var shootTimer = 0.0
    fun trackPlayer(playerPosition: Vec2) : Unit {

        velocity = Tracking.arrival(Vec2(x.toFloat(), y.toFloat()), playerPosition,moveSpeed*2).mul(moveSpeed)



    }
    
    fun shootMissiles()
    {
        if (velocity.length()<1.5) {
            SpawningManager.spawnMissile(x, y, views, player, parent)
        }
    }
}