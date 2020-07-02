package org.itheima.game.model

import org.itheima.game.Config
import org.itheima.kotlin.game.core.Painter

class Blast(override val x: Int, override val y: Int) : org.itheima.game.business.Destroyable {
    override val width: Int = Config.block
    override val height: Int = Config.block
    private val imgPaths: ArrayList<String> = arrayListOf<String>()
    private var index: Int = 0

    init {
        for (i in 1..32) {
            imgPaths.add("img/blast_$i.png")
        }
    }

    override fun draw() {
        val i: Int = index % imgPaths.size
        Painter.drawImage(imgPaths[i], x, y)
        index++
    }

    override fun isDestroyed(): Boolean {
        return index >= imgPaths.size
    }
}