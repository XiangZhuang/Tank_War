package org.itheima.game

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.game.business.*
import org.itheima.game.enums.Direction
import org.itheima.game.model.*
import org.itheima.kotlin.game.core.Window
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow: Window(title="Tank War",
    icon="img/logo.jpg",
    width=Config.gameWidth,
    height=Config.gameHeight) {

    private val views: CopyOnWriteArrayList<View> = CopyOnWriteArrayList<View>()

    private lateinit var tank: Tank

    private var gameOver = false

    private var enemyTotalSize = 20
    private var enemyActiveSize = 6

    private val enemyBornLocs: ArrayList<Pair<Int, Int>> = arrayListOf<Pair<Int, Int>>()
    private var indexBorn: Int = 0

    override fun onCreate() {
        // Create Map
//        val file = File(javaClass.getResource("/map/1.map").toURI())
        val resourceStream = javaClass.getResourceAsStream("/map/1.map")
        val reader = BufferedReader(InputStreamReader(resourceStream, "utf-8"))
        val lines: List<String> = reader.readLines()
        var lineNum = 0
        lines.forEach {
            line ->
            var columnNum = 0
            line.toCharArray().forEach {
                column ->
                when (column) {
                    '砖' -> views.add(Wall(columnNum * Config.block, lineNum * Config.block))
                    '草' -> views.add(Grass(columnNum * Config.block, lineNum * Config.block))
                    '水' -> views.add(Water(columnNum * Config.block, lineNum * Config.block))
                    '铁' -> views.add(Steel(columnNum * Config.block, lineNum * Config.block))
                    '敌' -> enemyBornLocs.add(Pair(columnNum * Config.block, lineNum * Config.block))
                }
                columnNum++
            }
            lineNum++
        }

        tank = Tank(Config.block * 10, Config.block * 12)
        views.add(tank)
        views.add(Camp(Config.gameWidth / 2 - Config.block, Config.gameHeight - 96))
    }

    override fun onDisplay() {
        views.forEach {
            view ->
            view.draw()
        }
    }

    override fun onKeyPressed(event: KeyEvent) {
        if (!gameOver) {
            when (event.code) {
                KeyCode.W -> tank.move(Direction.UP)
                KeyCode.S -> tank.move(Direction.DOWN)
                KeyCode.A -> tank.move(Direction.LEFT)
                KeyCode.D -> tank.move(Direction.RIGHT)
                KeyCode.ENTER -> {
                    val bullet = tank.shot()
                    views.add(bullet)
                }
            }
        }
    }

    override fun onRefresh() {
        // Destroy Unused Element
        views.filter { it is Destroyable }.forEach {
            if ((it as Destroyable).isDestroyed()) {
                views.remove(it)

                if (it is Enemy) {
                    enemyTotalSize--
                }

                val destroy: Array<View>? = it.showDestroy()
                destroy?.let {
                    views.addAll(destroy)
                }
            }
        }
        // Game Over
        if (gameOver) return
        // Business
        // Collision
        views.filter { it is Movable }.forEach {
            moveObj ->
            /** Because views is an arraylist of View,
             * need to specify its elements as Movable or Blockable
             * to enable the willCollide function
             */
            moveObj as Movable
            var badDirection: Direction? = null
            var badBlock: Blockable? = null
            views.filter { (it is Blockable) and (moveObj != it) }.forEach blockTag@ {
                blockObj ->
                blockObj as Blockable
                val direction: Direction? = moveObj.willCollide(blockObj)

                // Will be executed when direction is not null
                direction?.let {
                    badDirection = direction
                    badBlock = blockObj
                    return@blockTag
                }
            }
            moveObj.notifyCollision(badDirection, badBlock)
        }

       // Auto Move
        views.filter { it is AutoMovable }.forEach {
            (it as AutoMovable).autoMove()
        }

        views.filter { it is Attackable }.forEach {
            attack ->
            attack as Attackable
            views.filter { (it is Sufferable) and (attack.owner != it) and (attack != it)}.forEach sufferTag@ {
                suffer ->
                suffer as Sufferable
                if (attack.isCollision(suffer)) {
                    attack.notifyAttack(suffer)
                    val sufferRe: Array<View>? = suffer.notifySuffer(attack)
                    sufferRe?.let {
                        views.addAll(sufferRe)
                    }
                    return@sufferTag
                }
            }
        }

        views.filter{ it is AutoShot }.forEach {
            it as AutoShot
            val shot: View? = it.autoShot()
            shot?.let {
                views.add(shot)
            }
        }

        if ((views.filter{ it is Camp }.isEmpty()) or (enemyTotalSize <= 0)) {
            gameOver = true
        }

        if (views.filter{ it is Enemy }.size < enemyActiveSize) {
            println(indexBorn)
            val pair = enemyBornLocs[indexBorn % enemyBornLocs.size]
            println(pair)
            views.add(Enemy(pair.first, pair.second))
            indexBorn++
        }
    }

}