package entities.enemies

import com.soywiz.korge.view.*
import com.soywiz.korma.geom.*
import entities.Player
import math.Tracking.trackingVector
import org.jbox2d.common.Vec2
import kotlin.math.atan2

class TrackingEnemy(bm: SpriteAnimation, views: Views, player: Player, health: Int) : Enemy(bm, views, player, moveSpeed = 1f, health = health) {

    override fun updateVelocity() {}

    override fun updatePosition(dt: Double) {
        xy(x + velocity.x ,y + velocity.y )

        if (velocity != Vec2())
            angle = atan2(velocity.y.toDouble(), velocity.x.toDouble()).radians

        rotation += rotation.shortDistanceTo(angle+90.degrees) * 4 * (dt / 1000)
    }

    override fun check() {
        if(collidesWith(player)){
            player.health-=5
        }
    }

    fun trackPlayer(playerPosition: Vec2): Unit {
        velocity = trackingVector(Vec2(x.toFloat(), y.toFloat()), playerPosition).mul(moveSpeed)
    }
}
