package entities

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.radians
import entities.enemies.Missile
import entities.enemies.RangedEnemy
import entities.enemies.TrackingEnemy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import math.Tracking
import org.jbox2d.common.Vec2
import kotlin.math.abs
import kotlin.math.atan2


//USE THIS TO STORE THE ENEMY BITMAPS AND ASSOCIATED FILES SO THAT CALLING THEM WILL BE EASY
object SpawningManager {


     fun spawnTrackingEnemy(x :Double, y :Double, view: Views, player :Player, parent: Container?){
         GlobalScope.launch {
//             val trackingEnemyBitmap = resourcesVfs["enemies/3 small ships.png"].readBitmap()
//             val enemy = TrackingEnemy(SpriteAnimation(trackingEnemyBitmap, spriteWidth = 59, spriteHeight = 57), view, player)
             val testingSpriteAnimation = SpriteAnimation(
                     spriteMap = resourcesVfs["homing_enemy.png"].readBitmap(),
                     spriteWidth = 48,
                     spriteHeight = 48,
                     columns = 9,
                     rows = 1
             )
             val enemy = TrackingEnemy(testingSpriteAnimation,view,player,5)
             enemy.xy(x, y)
             parent?.addChild(enemy)
             enemy.scale=3.0
             enemy.addUpdater {
                 trackPlayer(Vec2(player.x.toFloat(), player.y.toFloat()))

                 val distanceToPlayer = Vec2((parent?.pos?.x!! - x).toFloat(), (parent?.pos?.y!! - x).toFloat())
                 if (abs(distanceToPlayer.length()) > 1600)
                     removeFromParent()
             }
         }
    }

     fun spawnRangedEnemy(x :Double, y :Double, view: Views, player: Player, parent: Container?){
         GlobalScope.launch {
             val enemyBitmap = resourcesVfs["enemies/ranged Ship.png"].readBitmap()
             val enemy = RangedEnemy(SpriteAnimation(enemyBitmap, spriteWidth = 53, spriteHeight = 55), view, player,5)
             enemy.xy(x, y)
             parent?.addChild(enemy)
             enemy.scale=3.0
             var shootTimer = 0.0
             enemy.addUpdater() {
                 enemy.trackPlayer(Vec2(player.x.toFloat(), player.y.toFloat()))
             }
//             GlobalScope.launch {
                 while (true) {
                     delay(2500)
                     enemy.shootMissiles()
                 }
//             }
         }
     }

    fun spawnMissile(x :Double, y :Double, view: Views, player :Player, parent: Container?) {
        GlobalScope.launch {
            val enemyBitmap = resourcesVfs["animations/projectiles/rocket.png"].readBitmap()
            val enemy = Missile(SpriteAnimation(enemyBitmap), view, player,2)
            enemy.xy(x, y)
            parent?.addChild(enemy)
            enemy.scale=3.0
            enemy.velocity = Tracking.trackingVector(Vec2(enemy.x.toFloat(), enemy.y.toFloat().toFloat()),Vec2(player.x.toFloat(), player.y.toFloat()))
            enemy.velocity.mulLocal(enemy.moveSpeed)
            enemy.rotation = atan2(player.y-enemy.y,player.x-enemy.x).radians
            enemy.addUpdater {
            }
//            GlobalScope.launch{
                while(true) {
                    enemy.trackPlayer(Vec2(player.x.toFloat(), player.y.toFloat()))
                    delay(40)
                }
//            }
        }
    }

     fun spawnExplosion(x:Double, y: Double, angle: Angle, parent: Container?, size: Double){
         GlobalScope.launch {
             val bitmap = resourcesVfs["animations/explosions/explosion-6.png"].readBitmap()

             val sprite = Sprite(SpriteAnimation(
                     spriteMap = bitmap,
                     spriteWidth = 48,
                     spriteHeight = 48,
                     columns = 9,
                     rows = 1
             ))
             parent?.addChild(sprite)
             sprite.xy(x, y)
             sprite.rotation = angle
             sprite.scale = size
             sprite.playAnimation(spriteDisplayTime = 70.milliseconds)
             sprite.center()
             sprite.onAnimationCompleted {
                 sprite.removeFromParent()
             }
         }
     }

     fun spawnXP(x: Double, y: Double, player:Player, parent: Container?)  {
         GlobalScope.launch {
             val randedEnemyBitmap = resourcesVfs["exp.png"].readBitmap()
             val exp = ExperiencePoint(randedEnemyBitmap, player)
             exp.xy(x, y)
             parent?.addChild(exp)
         }
    }


}