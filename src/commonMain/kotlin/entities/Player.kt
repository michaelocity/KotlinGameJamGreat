package entities

import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.kmem.toInt
import com.soywiz.korev.Key
import com.soywiz.korge.time.delay
import com.soywiz.korge.tween.rotateBy
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import entities.projectiles.PlayerBullet
import input.PlayerInput
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jbox2d.common.MathUtils
import org.jbox2d.common.Vec2
import kotlin.math.atan2
import kotlin.math.floor

class Player(bm: Bitmap, private var views: Views, var reloadTime: TimeSpan = 100.milliseconds) : Sprite(bm) {
//    private val moveUpKey = Key.W
//    private val moveLeftKey = Key.A
//    private val moveDownKey = Key.S
//    private val moveRightKey = Key.D

    private val moveSpeed = 10f
    private val deceleration = 0.99f

    val velocity = Vec2()
    private val input = Vec2()
    private val bounciness = -0.75f
    private val gunRecoil = 10
//    private var angle = 0.radians
    var angle = 0.radians



    private val playerInput = PlayerInput(views)

    init {
        scale = 0.5
        center()
        shoot()
        addUpdater {
            val deltaTime = it.milliseconds / 1000

            input.x = (playerInput.pressingRight().toInt() - playerInput.pressingLeft().toInt()).toFloat()
            input.y = (playerInput.pressingDown().toInt() - playerInput.pressingUp().toInt()).toFloat()
            input.normalize()



//            velocity.x += input.x * moveSpeed
//            velocity.y += input.y * moveSpeed
//            velocity.x *= deceleration
//            velocity.y *= deceleration

            velocity.x = (velocity.x + input.x * moveSpeed) * deceleration
            velocity.y = (velocity.y + input.y * moveSpeed) * deceleration

            if (playerInput.pressingBoost())
                velocity = Vec2(input.x, input.y)

            xy(x + velocity.x * deltaTime,y + velocity.y * deltaTime)

            if (velocity != Vec2())
                angle = atan2(velocity.y.toDouble(), velocity.x.toDouble()).radians

            rotation += rotation.shortDistanceTo(angle + 90.degrees) * 10 * deltaTime
        }
    }
    private fun shoot(){
        GlobalScope.launch {
            while(true) {
                delay(10.milliseconds)
                if (playerInput.pressingAttack()) {
                    val animation = SpriteAnimation(
                                spriteMap = resourcesVfs["animations/projectiles/Projectile3.png"].readBitmap(),
                                spriteWidth = 42,
                                spriteHeight = 7,
                                columns = 3,
                                rows = 1)

                    val bullet1 = PlayerBullet(this@Player, animation)
                    bullet1.xy(x + cos(rotation) * 16, y + sin(rotation) * 16)
                    bullet1.shootNew()
                    val bullet2 = PlayerBullet(this@Player, animation)
                    bullet2.xy(x - cos(rotation) * 16, y - sin(rotation) * 16)
                    bullet2.shootNew()
                    //velocity.x -= cos(rotation).toFloat() * 100f
                    //velocity.y -= sin(rotation).toFloat() * 100f
                    delay(reloadTime)
                }
            }
        }
    }

}