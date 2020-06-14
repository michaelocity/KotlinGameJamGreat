package entities.projectiles

import com.soywiz.klock.seconds
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.xy
import com.soywiz.korma.geom.cos
import com.soywiz.korma.geom.sin
import entities.Player
import org.jbox2d.common.Vec2

class PlayerBullet(private val player: Player, bm : SpriteAnimation) : Projectile(bm) {
    var velocity = Vec2(1.0f,0f)


    override suspend fun shootNew() {
        scale = 1.0
        rotation = player.angle
        player.parent?.let { addTo(it) }
        //xy(player.x,player.y)

        println(rotation)
        println(velocity)
        velocity = Vec2(cos(rotation).toFloat(), sin(rotation).toFloat())
        velocity.mulLocal(bulletspeed)
        println("original velocity  $velocity ")
        playAnimationLooped()
    }

     override fun updateVelocity(dt: Double) {
         //println(velocity)
        xy(x +velocity.x ,y +velocity.y )
    }

    override fun collide() {

    }

    override fun check() {

    }

}
