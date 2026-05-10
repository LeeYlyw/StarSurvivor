package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 실제 게임 플레이가 진행되는 메인 Scene.
 *
 * 현재 기본 구조 단계에서는 Player, Bullet, Enemy, EnemyGenerator,
 * CollisionChecker, Score를 등록하여 게임의 핵심 흐름을 구성한다.
 */
class MainScene(gctx: GameContext) : Scene(gctx) {
    enum class Layer {
        BACKGROUND,
        PLAYER,
        BULLET,
        ENEMY,
        ASTEROID,
        CONTROLLER,
        UI,
    }

    override val clipsRect = true

    val player = Player(gctx)

    private val enemyGenerator = EnemyGenerator(gctx)
    private val collisionChecker = CollisionChecker()
    private val score = Score(player)
    private val asteroidGenerator = AsteroidGenerator(gctx)
    override val world = World(Layer.entries.toTypedArray()).apply {
        add(player, Layer.PLAYER)
        add(enemyGenerator, Layer.CONTROLLER)
        add(collisionChecker, Layer.CONTROLLER)
        add(asteroidGenerator, Layer.CONTROLLER)
    }

    fun addScore(amount: Int) {
        score.add(amount)
    }

    fun getScore(): Int {
        return score.score
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        super.draw(canvas)

        score.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return player.onTouchEvent(event)
    }
}