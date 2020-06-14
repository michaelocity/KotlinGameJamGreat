package entities.projectiles

import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.*
import com.soywiz.korma.geom.cos
import com.soywiz.korma.geom.sin
import entities.enemies.Enemy
import entities.Player
import entities.SpawningManager
import entities.enemies.Missile
import org.jbox2d.common.Vec2
import kotlin.math.abs

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
        // velocity of bullet relative to player?
//        velocity = Vec2(cos(rotation).toFloat() + player.velocity.x , sin(rotation).toFloat() + player.velocity.y)
        velocity.mulLocal(bulletspeed)
//        println("original velocity  $velocity ")
        playAnimationLooped()
    }

     override fun updateVelocity(dt: Double) {
         //println(velocity)
         xy(x +velocity.x ,y +velocity.y )

         val distanceToPlayer = Vec2((player.x - x).toFloat(), (player.y - y).toFloat())
         if (abs(distanceToPlayer.length()) > 1600)
             removeFromParent()
    }

    @KorgeInternal
    @ExperimentalStdlibApi
    override fun check() {
        try {
            parent!!.children.forEach {
                if(it is Enemy && it.collidesWith(this) && it.health > 0){
//                    println("before it.health: ${it.health}")
                    it.health--
//                    println("after it.health: ${it.health}")
                    if(it.health <= 0){
//                        println(it)
                        println(it.size)
                        if(it !is Missile) {
                            SpawningManager.spawnExplosion(it.pos.x, it.pos.y, it.angle, it.parent, 8.0)
                            for (i in 0 until 3) {
                                SpawningManager.spawnXP(it.pos.x,it.pos.y,player,it.parent)
                            }
                        }else{
                            SpawningManager.spawnExplosion(it.pos.x, it.pos.y, it.angle, it.parent, 1.0)
                        }
                        it.removeFromParent()
                    }
                    render = false
                    removeFromParent()
                }
            }
        }catch (ignored: Exception){}
    }

}
