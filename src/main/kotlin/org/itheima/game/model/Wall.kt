package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.game.business.Attackable
import org.itheima.game.business.Blockable
import org.itheima.game.business.Destroyable
import org.itheima.game.business.Sufferable
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

class Wall(override val x: Int, override val y: Int) : Blockable, Sufferable, Destroyable {
    override val width: Int = Config.block
    override val height: Int = Config.block

    override var blood: Int = 3

    override fun draw() {
        Painter.drawImage("img/wall.gif", x, y)
    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
        this.blood -= attackable.power
//        Composer.play("snd/hit.wav")
        return arrayOf(Blast(x, y))
    }

    override fun isDestroyed(): Boolean = this.blood <= 0
}