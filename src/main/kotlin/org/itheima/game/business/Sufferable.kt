package org.itheima.game.business

import org.itheima.game.model.View

interface Sufferable : View {
    val blood: Int
    fun notifySuffer (attackable: Attackable): Array<View>?
}