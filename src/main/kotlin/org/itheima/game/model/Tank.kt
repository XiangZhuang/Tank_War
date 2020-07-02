package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.game.business.Attackable
import org.itheima.game.business.Blockable
import org.itheima.game.business.Movable
import org.itheima.game.business.Sufferable
import org.itheima.game.enums.Direction
import org.itheima.kotlin.game.core.Painter

class Tank(override var x: Int, override var y: Int) : Movable, Blockable, Sufferable {
    override var width: Int = Config.block
    override var height: Int = Config.block

    override var currentDirection: Direction = Direction.UP
    override val speed: Int = 16

    override var blood: Int = 20

    // The direction that the tank cannot go
    private var badDirection: Direction? = null

    override fun draw() {
        val imagePath = when (currentDirection) {
            Direction.UP -> "img/tank_u.gif"
            Direction.DOWN -> "img/tank_d.gif"
            Direction.LEFT -> "img/tank_l.gif"
            Direction.RIGHT -> "img/tank_r.gif"
        }
        Painter.drawImage(imagePath, x, y)
    }

    fun move (d: Direction) {
        if (d != currentDirection) {
            this.currentDirection = d
            return
        }
        if (d == this.badDirection) {
            return
        }
        when (d) {
            Direction.UP -> if (y > 0) this.y -= speed
            Direction.DOWN -> if (y < Config.gameHeight - Config.block) this.y += speed
            Direction.LEFT -> if (x > 0) this.x -= speed
            Direction.RIGHT -> if (x < Config.gameWidth - Config.block) this.x += speed
        }
    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        this.badDirection = direction
    }

    fun shot (): Bullet {

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
        blood -= attackable.power
        return arrayOf(Blast(x, y))
    }
}