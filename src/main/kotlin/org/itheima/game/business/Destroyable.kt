package org.itheima.game.business

import org.itheima.game.model.View

interface Destroyable : View {
    fun isDestroyed (): Boolean
}