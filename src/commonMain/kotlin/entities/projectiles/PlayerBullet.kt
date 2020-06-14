package entities.projectiles

import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.bitmap.Bitmap
import entities.Player

class PlayerBullet(player: Player, bm : Bitmap) : Projectile(/*default player bullet png*/bm) {
    override fun shootNew() {
        //shoot new bullet from direction of players rotation
        val job = addUpdater {

        }
    }

    override fun collide() {
        TODO("Not yet implemented")
    }

    override fun check() {
        TODO("Not yet implemented")
    }

}