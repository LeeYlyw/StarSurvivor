package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IRecyclable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.util.Gauge
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin

/**
 * 일반 적보다 거대하고 튼튼하며, 지그재그로 변칙 이동하는 특수 보스급 드론.
 */
class SpecialEnemy private constructor(
    gctx: GameContext,
) : Sprite(gctx, R.drawable.special_drone), IBoxCollidable, IRecyclable { // 새로 넣은 에셋 매핑

    var life = START_LIFE
        private set
    var maxLife = START_LIFE
        private set

    val dead: Boolean
        get() = life <= 0

    override val collisionRect = RectF()

    override var width = SPECIAL_ENEMY_WIDTH
    override var height = SPECIAL_ENEMY_HEIGHT
    override var x = 0f
    override var y = 0f

    private var speed = SPEED
    private var dirX = 0f
    private var dirY = 1f

    // 지그재그 움직임을 정밀하게 계산하기 위한 누적 시간 타이머
    private var movementTimer = 0f

    init {
        if (gauge == null) {
            // 특수 적임을 인지할 수 있도록 보라색 체력 바를 커스텀 적용합니다.
            gauge = Gauge(0.12f, Color.parseColor("#8A2BE2"), Color.DKGRAY)
        }
        syncDstRect()
        updateCollisionRect()
    }

    fun init(x: Float, y: Float, dirX: Float, dirY: Float, wave: Int): SpecialEnemy {
        this.x = x
        this.y = y
        this.dirX = dirX
        this.dirY = dirY
        this.speed = SPEED + wave * 5f // 웨이브가 진행될수록 이동 속도 약간 증가
        this.movementTimer = 0f

        // 일반 적보다 3배 이상 높은 맷집을 부여합니다.
        this.life = START_LIFE + (wave * 2)
        this.maxLife = this.life

        syncDstRect()
        updateCollisionRect()
        return this
    }

    override fun update(gctx: GameContext) {
        movementTimer += gctx.frameTime

        // [핵심 메카닉] 원래 전진하려던 방향 벡터에 수직인 횡방향 유동 벡터를 삼각함수로 얹어
        // 화면을 요리조리 흔들며 다가오는 지그재그 회피 기동을 구현합니다.
        val perpX = -dirY
        val perpY = dirX
        val sway = sin(movementTimer * SWAY_FREQUENCY) * SWAY_AMPLITUDE

        x += (dirX * speed + perpX * sway) * gctx.frameTime
        y += (dirY * speed + perpY * sway) * gctx.frameTime

        if (isOutOfScreen(gctx)) {
            val scene = gctx.scene as? MainScene ?: return
            scene.world.remove(this, MainScene.Layer.ENEMY)
        }

        syncDstRect()
        updateCollisionRect()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val gaugeWidth = width * 0.8f
        val gaugeX = x - gaugeWidth / 2f
        val gaugeY = dstRect.bottom + 5f

        gauge?.draw(canvas, gaugeX, gaugeY, gaugeWidth, life.toFloat() / maxLife)
    }

    private fun isOutOfScreen(gctx: GameContext): Boolean {
        val marginX = SPECIAL_ENEMY_WIDTH
        val marginY = SPECIAL_ENEMY_HEIGHT
        return x < -marginX || x > gctx.metrics.width + marginX || y < -marginY || y > gctx.metrics.height + marginY
    }

    private fun updateCollisionRect() {
        collisionRect.set(dstRect)
        collisionRect.inset(COLLISION_INSET, COLLISION_INSET)
    }

    fun decreaseLife(power: Int) {
        life -= power
    }

    fun getScore(): Int {
        return BONUS_SCORE
    }

    override fun onRecycle() {
        life = START_LIFE
        maxLife = START_LIFE
        speed = SPEED
        dirX = 0f
        dirY = 1f
        movementTimer = 0f
    }

    companion object {
        const val SPECIAL_ENEMY_WIDTH = 180f  // 일반 적(120f)의 1.5배 크기
        const val SPECIAL_ENEMY_HEIGHT = 180f
        const val SPEED = 140f                 // 덩치가 큰 만큼 전진 속도는 약간 느리게 설정
        const val COLLISION_INSET = 15f

        const val START_LIFE = 8               // 높은 체력 진입장벽
        const val BONUS_SCORE = 500            // 처치 시 대량의 점수 획득

        const val SWAY_FREQUENCY = 5.0f        // 도는 횟수 (지그재그 속도)
        const val SWAY_AMPLITUDE = 250f        // 흔들리는 폭 (좌우 왕복 너비)

        private var gauge: Gauge? = null

        fun get(gctx: GameContext, x: Float, y: Float, dirX: Float, dirY: Float, wave: Int): SpecialEnemy {
            val scene = gctx.scene as? MainScene
                ?: return SpecialEnemy(gctx).init(x, y, dirX, dirY, wave)

            val specialEnemy = scene.world.obtain(SpecialEnemy::class.java) ?: SpecialEnemy(gctx)
            return specialEnemy.init(x, y, dirX, dirY, wave)
        }
    }
}