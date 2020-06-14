package entities

import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.kmem.toInt
import com.soywiz.korev.Key
import com.soywiz.korge.animate.play
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
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.floor

class ExperiencePoint(bm: Bitmap, player: Player) : Sprite(bm) {
    init {
        scale = 0.25
        center()
        addUpdater {
            rotationDegrees += 5

            val deltaTime = it.milliseconds / 1000
            val distanceToPlayer = Vec2(((player.x - x).toFloat()), (player.y - y).toFloat())
            if (abs(distanceToPlayer.length()) < 200) {
                pos.x += ((player.pos.x - pos.x) * abs(1 - abs(distanceToPlayer.x / 200))) * 2 * deltaTime
                pos.y += ((player.pos.y - pos.y) * abs(1 - abs(distanceToPlayer.y / 200))) * 2 * deltaTime
            }

            if (collidesWith(player)) {
                player.xp += 1
                removeFromParent()
            }
        }
    }
}