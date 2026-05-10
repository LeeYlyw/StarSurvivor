package kr.ac.tukorea.s2019184025.spacesurvival

import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.VertScrollBackground
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 실제 게임 플레이가 진행되는 메인 Scene.
 *
 * Player, Bullet, Enemy, Asteroid, CollisionChecker, Score를 등록하여
 * 생존형 슈팅 게임의 핵심 흐름을 구성한다.
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

    private val background = VertScrollBackground(
        gctx = gctx,
        resId = R.drawable.space_background,
        speed = BACKGROUND_SPEED,
    )

    val player = Player(gctx)

    private val enemyGenerator = EnemyGenerator(gctx)
    private val asteroidGenerator = AsteroidGenerator(gctx)
    private val collisionChecker = CollisionChecker()
    private val score = Score(player)

    override val world = World(Layer.entries.toTypedArray()).apply {
        add(background, Layer.BACKGROUND)

        add(player, Layer.PLAYER)

        add(enemyGenerator, Layer.CONTROLLER)
        add(asteroidGenerator, Layer.CONTROLLER)
        add(collisionChecker, Layer.CONTROLLER)

        add(score, Layer.UI)
    }

    fun addScore(amount: Int) {
        score.add(amount)
    }

    fun getScore(): Int {
        return score.score
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return player.onTouchEvent(event)
    }

    companion object {
        const val BACKGROUND_SPEED = 40f
    }
}