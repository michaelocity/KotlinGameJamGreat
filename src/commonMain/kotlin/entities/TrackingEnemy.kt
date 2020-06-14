package entities.enemies

import com.soywiz.korge.view.*
import com.soywiz.korma.geom.*
import entities.SpawningManager.spawnExplosion
import entities.enemies.Enemy
import math.Tracking.trackingVector
import org.jbox2d.common.Vec2
import kotlin.math.atan2

class TrackingEnemy(bm: SpriteAnimation, views: Views, player: Player, health: Int = 3) : Enemy(bm, views, player, moveSpeed = 1f) {

    override fun updateVelocity() {}

    override fun updatePosition(dt: Double) {
        xy(x + velocity.x ,y + velocity.y )

        if (velocity != Vec2())
            angle = atan2(velocity.y.toDouble(), velocity.x.toDouble()).radians

        rotation += rotation.shortDistanceTo(angle+90.degrees) * 4 * (dt / 1000)
    }

    override fun check() {
        if (this.collidesWith(player)) {//set the image to be explosion if collided
            spawnExplosion(x, y, rotation, parent, 8.0)
            parent?.removeChild(this)

            for (i in 0..12) {
//                spawnXP(Random(1).nextInt((x - 10).toInt()), (x + 10).toInt()), Random(1).nextInt((y - 10).toInt(), (y + 10).toInt()), parent, 0.25)
            }

        }

    }

    fun trackPlayer(playerPosition: Vec2): Unit {

        velocity = trackingVector(Vec2(x.toFloat(), y.toFloat()), playerPosition).mul(moveSpeed)

    }
}