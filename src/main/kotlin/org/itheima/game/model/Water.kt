package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.game.business.Blockable
import org.itheima.kotlin.game.core.Painter

class Water(override val x: Int, override val y: Int) : Blockable {
    override val width: Int = Config.block
    override val height: Int = Config.block

    override fun draw() {
        Painter.drawImage("img/water.gif", x, y)
    }
}