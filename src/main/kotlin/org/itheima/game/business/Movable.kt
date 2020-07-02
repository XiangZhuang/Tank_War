package org.itheima.game.business

import org.itheima.game.Config
import org.itheima.game.enums.Direction
import org.itheima.game.model.View

interface Movable : View {
    // If return nil Direction, means that there will be no collision
    val currentDirection: Direction
    val speed: Int

    fun willCollide (block: Blockable): Direction? {
        var x = this.x
        var y = this.y
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
        if (x < 0) return Direction.LEFT
        if (x > Config.gameWidth - this.width) return Direction.RIGHT
        if (y < 0) return Direction.UP
        if (y > Config.gameHeight - this.height) return Direction.DOWN
        val collision = checkCollision(x, y, this.width, this.height, block.x, block.y, block.width, block.height)
        return if (collision) currentDirection else null
    }
    fun notifyCollision (direction: Direction?, block: Blockable?)
}