package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.game.business.Attackable
import org.itheima.game.business.AutoMovable
import org.itheima.game.business.Destroyable
import org.itheima.game.business.Sufferable
import org.itheima.game.enums.Direction
import org.itheima.game.ext.checkCollision
import org.itheima.kotlin.game.core.Painter

class Bullet(
    override val owner: View, override val currentDirection: Direction,
    create: (width: Int, height: Int) -> Pair<Int, Int>) : AutoMovable, Destroyable, Attackable, Sufferable {
    override val width: Int
    override val height: Int
    override var x: Int = 0
    override var y: Int = 0
    override val speed: Int = 8
    override var power: Int = 1
    override val blood: Int = 1

    private var isDestroyed: Boolean = false

    val imgPath = when (currentDirection) {
        Direction.UP -> "img/shot_top.gif"
        Direction.DOWN -> "img/shot_bottom.gif"
        Direction.LEFT -> "img/shot_left.gif"
        Direction.RIGHT -> "img/shot_right.gif"
    }

    init {
        val size: Array<Int> = Painter.size(imgPath)
        width = size[0]
        height = size[1]
        val locPair = create(width, height)
        x = locPair.first
        y = locPair.second
    }

    override fun draw() {
        Painter.drawImage(imgPath, x, y)
    }

    override fun autoMove() {
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
    }

    override fun isDestroyed(): Boolean {
        if (isDestroyed) return true
        if (x < -width || x > Config.gameWidth) return true
        if (y < -height || y > Config.gameHeight) return true
        return false
    }

    override fun isCollision(sufferable: Sufferable): Boolean {
        return checkCollision(sufferable)
    }

    override fun notifyAttack(sufferable: Sufferable) {
        isDestroyed = true
    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        if ((this.owner is Enemy) or (attackable.owner == this.owner) or (this == attackable)) return null
        return arrayOf(Blast(x - (this.owner.width - width) / 2, y - (this.owner.height - height) / 2))
    }
}