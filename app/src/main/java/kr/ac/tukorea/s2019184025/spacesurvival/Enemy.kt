package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IRecyclable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.util.Gauge
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 화면 가장자리 바깥에서 등장하여 플레이어 방향으로 접근하는 적 드론.
 *
 * Bullet과 충돌하면 체력이 감소하고,
 * 체력이 0 이하가 되면 제거되며 점수를 제공한다.
 */
class Enemy private constructor(
    private val gctx: GameContext,
) : Sprite(gctx, R.drawable.enemy_drone), IBoxCollidable, IRecyclable {
    var level = 1
        private set

    var life = level * LIFE_PER_LEVEL
        private set

    var maxLife = life
        private set

    val dead: Boolean
        get() = life <= 0

    override val collisionRect = RectF()

    override var width = ENEMY_WIDTH
    override var height = ENEMY_HEIGHT
    override var x = 0f
    override var y = 0f

    private var speed = DEFAULT_SPEED
    private var dirX = 0f
    private var dirY = 1f

    init {
        if (gauge == null) {
            gauge = Gauge(
                0.1f,
                Color.RED,
                Color.DKGRAY,
            )
        }

        syncDstRect()
        updateCollisionRect()
    }

    fun init(
        x: Float,
        y: Float,
        dirX: Float,
        dirY: Float,
        level: Int,
        speed: Float,
    ): Enemy {
        this.x = x
        this.y = y
        this.dirX = dirX
        this.dirY = dirY
        this.level = level
        this.speed = speed

        life = level * LIFE_PER_LEVEL
        maxLife = life

        syncDstRect()
        updateCollisionRect()

        return this
    }

    override fun update(gctx: GameContext) {
        x += dirX * speed * gctx.frameTime
        y += dirY * speed * gctx.frameTime

        if (isOutOfScreen(gctx)) {
            val scene = gctx.scene as? MainScene ?: return
            scene.world.remove(this, MainScene.Layer.ENEMY)
        }

        syncDstRect()
        updateCollisionRect()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val gaugeWidth = width * 0.7f
        val gaugeX = x - gaugeWidth / 2f
        val gaugeY = dstRect.bottom

        gauge?.draw(
            canvas,
            gaugeX,
            gaugeY,
            gaugeWidth,
            life.toFloat() / maxLife
        )
    }

    private fun isOutOfScreen(gctx: GameContext): Boolean {
        val marginX = ENEMY_WIDTH
        val marginY = ENEMY_HEIGHT

        return x < -marginX ||
                x > gctx.metrics.width + marginX ||
                y < -marginY ||
                y > gctx.metrics.height + marginY
    }

    private fun updateCollisionRect() {
        collisionRect.set(dstRect)
        collisionRect.inset(COLLISION_INSET, COLLISION_INSET)
    }

    fun decreaseLife(power: Int) {
        life -= power
    }

    fun getScore(): Int {
        return level * SCORE_PER_LEVEL
    }

    override fun onRecycle() {
        level = 1
        life = LIFE_PER_LEVEL
        maxLife = life
        speed = DEFAULT_SPEED
        dirX = 0f
        dirY = 1f
    }

    companion object {
        const val ENEMY_WIDTH = 120f
        const val ENEMY_HEIGHT = 120f
        const val DEFAULT_SPEED = 210f
        const val COLLISION_INSET = 10f

        // [난이도 튜닝] 레벨당 체력 가중치를 1에서 3으로 대폭 상향 (레벨 3만 돼도 체력 9)
        const val LIFE_PER_LEVEL = 2

        // [보상 상향] 적이 단단해진 만큼 격추 시 획득 점수도 2배로 인상 (레벨 * 200)
        const val SCORE_PER_LEVEL = 200
        const val MAX_LEVEL_COUNT = 20

        private var gauge: Gauge? = null

        fun get(
            gctx: GameContext,
            x: Float,
            y: Float,
            dirX: Float,
            dirY: Float,
            level: Int,
            speed: Float,
        ): Enemy {
            val scene = gctx.scene as? MainScene
                ?: return Enemy(gctx).init(x, y, dirX, dirY, level, speed)

            val enemy = scene.world.obtain(Enemy::class.java) ?: Enemy(gctx)
            return enemy.init(x, y, dirX, dirY, level, speed)
        }
    }
}