package org.itheima.game.business

import org.itheima.game.model.View

interface Attackable : View {
    val owner: View
    val power: Int
    fun isCollision (sufferable: Sufferable): Boolean
    fun notifyAttack (sufferable: Sufferable)
}