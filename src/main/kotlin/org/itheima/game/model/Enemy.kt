package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.game.business.*
import org.itheima.game.enums.Direction
import org.itheima.kotlin.game.core.Painter
import java.util.*

class Enemy(override var x: Int, override var y: Int) : Movable, AutoMovable, Blockable, AutoShot, Sufferable, Destroyable {
    override val width: Int = Config.block
    override val height: Int = Config.block
    override var currentDirection: Direction = Direction.DOWN
    override val speed: Int = 8

    override var blood: Int = 3

    private var badDirection: Direction? = null
    private var lastShotTime = 0L
    private val shotFrequency = 1000
    private var lastMoveTime = 0L
    private val moveFrequency = 200

    override fun draw() {
        val imagePath = when (currentDirection) {
            Direction.UP -> "img/enemy_1_u.gif"
            Direction.DOWN -> "img/enemy_1_d.gif"
            Direction.LEFT -> "img/enemy_1_l.gif"
            Direction.RIGHT -> "img/enemy_1_r.gif"
        }
        Painter.drawImage(imagePath, x, y)
    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        this.badDirection = direction
    }

    override fun autoMove() {
        val current: Long = System.currentTimeMillis()
        if (current - lastMoveTime < moveFrequency) return
        lastMoveTime = current
        if (currentDirection == this.badDirection) {
            currentDirection = randDir(badDirection)
            return
        }
        when (currentDirection) {
            Direction.UP -> if (y > 0) this.y -= speed
            Direction.DOWN -> if (y < Config.gameHeight - Config.block) this.y += speed
            Direction.LEFT -> if (x > 0) this.x -= speed
            Direction.RIGHT -> if (x < Config.gameWidth - Config.block) this.x += speed
        }
    }

    private fun randDir (badDirection: Direction?): Direction {
        val i: Int = Random().nextInt(4)
        val direction = when (i) {
            0 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            3 -> Direction.RIGHT
            else -> Direction.UP
        }
        if (direction == badDirection) {
            return randDir(badDirection)
        }
        return direction
    }

    override fun autoShot(): View? {
        val current: Long = System.currentTimeMillis()
        if (current - lastShotTime < shotFrequency) return null
        lastShotTime = current
        return Bullet(this, currentDirection) { bulletWidth, bulletHeight ->
            var bulletX: Int
            var bulletY: Int
            when (currentDirection) {
                Direction.UP -> {
                    bulletX = this.x + (this.width - bulletWidth) / 2
                    bulletY = this.y - bulletHeight / 2
                }
                Direction.DOWN -> {
                    bulletX = this.x + (this.width - bulletWidth) / 2
                    bulletY = this.y + this.height - bulletHeight / 2
                }
                Direction.LEFT -> {
                    bulletX = this.x - bulletWidth / 2
                    bulletY = this.y + (this.height - bulletHeight) / 2
                }
                Direction.RIGHT -> {
                    bulletX = this.x + this.width - bulletWidth / 2
                    bulletY = this.y + (this.height - bulletHeight) / 2
                }
            }

            Pair(bulletX, bulletY)
        }
    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        if (attackable.owner is Enemy) return null
        blood -= attackable.power
        return arrayOf(Blast(x, y))
    }

    override fun isDestroyed(): Boolean = blood <= 0
}