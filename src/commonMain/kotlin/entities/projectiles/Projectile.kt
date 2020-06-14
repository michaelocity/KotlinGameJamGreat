package entities.projectiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import org.jbox2d.common.Vec2

abstract class Projectile(private var bm: SpriteAnimation, val bulletspeed: Float =5.0f) : Sprite(bm){
    abstract fun check():          Unit
    abstract fun collide():        Unit
    abstract suspend fun shootNew():       Unit
    abstract fun updateVelocity(dt: Double): Unit

    private var render = true

    init {
        scale = 0.5
        center()
        addUpdater{ //adds updater to move
            move(it.milliseconds)
        }
    }

    private fun move(dt: Double) {
        var time = dt
        while (time > 0 && render) {
            updateVelocity(dt)
            check()
            time-=5f
        }
    }


}