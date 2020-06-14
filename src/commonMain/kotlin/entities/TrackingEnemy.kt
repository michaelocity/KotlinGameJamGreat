package entities

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.lang.Thread_sleep
import com.soywiz.korma.geom.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import math.Tracking.arrival
import math.Tracking.curvingTracking
import math.Tracking.trackingVector
import org.jbox2d.common.Vec2
import kotlin.math.atan2

class TrackingEnemy(bm: SpriteAnimation, views: Views, player: Player, val health: Int = 2) : Enemy(bm, views, player, moveSpeed = 1f) {

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
            setFrame(1)
            scale = 4.0
            playAnimation(spriteDisplayTime = 125.milliseconds)
            render = false
            onAnimationStopped{
                removeFromParent()
            }
        }
    }

    fun trackPlayer(playerPosition: Vec2) : Unit {

        velocity = trackingVector(Vec2(x.toFloat(), y.toFloat()),playerPosition).mul(moveSpeed)

    }

}