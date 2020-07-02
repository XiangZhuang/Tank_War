package org.itheima.game.business

import org.itheima.game.enums.Direction
import org.itheima.game.model.View

interface AutoMovable : View {
    val currentDirection: Direction
    val speed: Int
    fun autoMove ()
}