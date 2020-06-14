package entities.enemies

import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.Views
import com.soywiz.korge.view.collidesWith
import com.soywiz.korge.view.xy
import com.soywiz.korma.geom.*
import entities.enemies.Enemy
import math.Tracking
import org.jbox2d.common.Vec2
import kotlin.math.atan2

class Missile (bm: SpriteAnimation, views: Views, player: Player, health: Int = 50) : Enemy(bm, views, player, moveSpeed = 3f) {

    override fun updateVelocity() {

    }

    override fun updatePosition(dt: Double) {
        xy(x + velocity.x ,y + velocity.y )
        if (velocity != Vec2())
            angle = atan2(velocity.y.toDouble(), velocity.x.toDouble()).radians

        rotation += rotation.shortDistanceTo(angle) * 10 * (dt / 1000)
        health -= dt.toInt()/1000
    }

    override fun check() {
        if(this.collidesWith(player)){//set the image to be explosion if collided
            SpawningManager.spawnExplosion(x,y,angle,parent,1.0)
            player.health--
            render = false
            removeFromParent()
        }
        if (this.collidesWith(player.bullets))
        {
            health--
            if(health<=0){
                SpawningManager.spawnExplosion(x,y,angle,parent,1.0)
                removeFromParent()
                SpawningManager.spawnXP(x.toInt(),y.toInt(),player,parent)
            }
        }
    }

    fun trackPlayer(playerPosition: Vec2) : Unit {
        velocity = Tracking.curvingTracking(Vec2(x.toFloat(), y.toFloat()), playerPosition, velocity, angle, 2.degrees).mul(moveSpeed)
    }
}