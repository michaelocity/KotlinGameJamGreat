package entities.projectiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import org.jbox2d.common.Vec2

abstract class Projectile(private var bm: Bitmap, val bulletspeed: Double = 20.0) : Sprite(bm){
    abstract fun shootNew():   Unit
    abstract fun collide(): Unit
    abstract fun check():   Unit

    init {
        scale = 0.5
        center()
        addUpdater{ //adds updater to move
            //move(it.milliseconds)
        }
    }
/*
    private fun move(dt: Double) {
        var time = dt
        while (time > 0 && render) {
            check()
            updateVelocity()
            updatePosition(dt)
            time-=5f
        }
    }
*/

}