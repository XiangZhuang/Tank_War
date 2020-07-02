package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.game.business.Attackable
import org.itheima.game.business.Blockable
import org.itheima.game.business.Sufferable
import org.itheima.kotlin.game.core.Painter

class Steel(override val x: Int, override val y: Int) : Blockable, Sufferable {
    override val width: Int = Config.block
    override val height: Int = Config.block
    override val blood: Int = 1

    override fun draw() {
        Painter.drawImage("img/steel.gif", x, y)
    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        return null
    }
}