package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * 플레이어 우주선.
 *
 * 터치한 위치를 목표 좌표로 저장하고,
 * update()에서 해당 위치를 향해 상하좌우로 이동한다.
 * 이동 방향에 맞춰 우주선 이미지가 회전하며,
 * 일정 시간마다 현재 바라보는 방향으로 Bullet을 자동 발사한다.
 */
class Player(
    private val gctx: GameContext,
) : Sprite(gctx, R.drawable.player_ship), IBoxCollidable {
    override var width = PLAYER_WIDTH
    override var height = PLAYER_HEIGHT
    override var x = gctx.metrics.width / 2f
    override var y = gctx.metrics.height / 2f

    private val minPlayerX = PLAYER_WIDTH / 2f
    private val maxPlayerX = gctx.metrics.width - PLAYER_WIDTH / 2f
    private val minPlayerY = PLAYER_HEIGHT / 2f
    private val maxPlayerY = gctx.metrics.height - PLAYER_HEIGHT / 2f

    private var targetX = x
    private var targetY = y

    // 이미지가 기본적으로 위쪽을 바라본다고 가정한다.
    private var angle = 0f

    // 현재 플레이어가 바라보는 방향. 기본값은 위쪽.
    private var aimDirX = 0f
    private var aimDirY = -1f

    private var fireCoolTime = FIRE_INTERVAL

    var hp = MAX_HP
        private set

    // 보호막 무적 지속 시간을 관리하는 타이머 변수 (0보다 크면 무적)
    var shieldTimer = 0f
        private set

    // 외부(CollisionChecker)에서 플레이어가 현재 무적 상태인지 쉽게 판단할 수 있는 플래그
    val isInvincible: Boolean
        get() = shieldTimer > 0f

    val dead: Boolean
        get() = hp <= 0

    override val collisionRect = RectF()

    init {
        syncDstRect()
        updateCollisionRect()
    }

    override fun update(gctx: GameContext) {
        if (dead) return

        if (shieldTimer > 0f) {
            shieldTimer -= gctx.frameTime
            if (shieldTimer < 0f) {
                shieldTimer = 0f
            }
        }

        updatePosition(gctx)
        fireBullet(gctx)

        syncDstRect()
        updateCollisionRect()
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(angle, x, y)
        super.draw(canvas)
        canvas.restore()
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (dead) return true

        val pt = gctx.metrics.fromScreen(event.x, event.y)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                targetX = pt.x.coerceIn(minPlayerX, maxPlayerX)
                targetY = pt.y.coerceIn(minPlayerY, maxPlayerY)
            }
        }

        return true
    }

    fun decreaseHp(amount: Int) {
        hp = (hp - amount).coerceAtLeast(0)
    }

    fun resetHp() {
        hp = MAX_HP
    }

    // 보호막 아이템을 먹었을 때 호출하여 무적 시간을 충전하는 함수
    fun activateShield(duration: Float) {
        shieldTimer = duration
    }

    // 재시작(Restart)할 때 혹시 남아있을지 모를 보호막 시간도 깨끗이 지워줍니다.
    fun resetStatus() {
        hp = MAX_HP
        shieldTimer = 0f
    }

    private fun updatePosition(gctx: GameContext) {
        val step = SPEED * gctx.frameTime

        val dx = targetX - x
        val dy = targetY - y

        if (abs(dx) > 0.5f || abs(dy) > 0.5f) {
            val length = sqrt(dx * dx + dy * dy)

            if (length > 0.0001f) {
                aimDirX = dx / length
                aimDirY = dy / length
            }

            angle = Math.toDegrees(
                atan2(aimDirY.toDouble(), aimDirX.toDouble())
            ).toFloat() + 90f
        }

        x = when {
            dx > step -> x + step
            dx < -step -> x - step
            else -> targetX
        }

        y = when {
            dy > step -> y + step
            dy < -step -> y - step
            else -> targetY
        }

        x = x.coerceIn(minPlayerX, maxPlayerX)
        y = y.coerceIn(minPlayerY, maxPlayerY)
    }

    private fun fireBullet(gctx: GameContext) {
        fireCoolTime -= gctx.frameTime
        if (fireCoolTime > 0f) return

        fireCoolTime = FIRE_INTERVAL

        val scene = gctx.scene as? MainScene ?: return
        val currentScore = scene.getScore()

        // 안전하게 CONTROLLER 레이어를 순회하며 EnemyGenerator 인스턴스를 직접 찾아 wave 값을 획득합니다.
        var currentWave = 1
        scene.world.forEachReversedAt(MainScene.Layer.CONTROLLER) { obj ->
            val generator = obj as? EnemyGenerator
            if (generator != null) {
                try {
                    val field = EnemyGenerator::class.java.getDeclaredField("wave")
                    field.isAccessible = true
                    currentWave = field.getInt(generator)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // 기존 점수 비례 데미지 공식을 그대로 유지합니다.
        val power = 10 + currentScore / 1000

        val bulletStartX = x + aimDirX * BULLET_OFFSET
        val bulletStartY = y + aimDirY * BULLET_OFFSET

        // 라운드 조건에 맞추어 평행 총알 개수 계산
        // - 1~2 라운드: 1발
        // - 3~5 라운드: 2발 (2라운드 완료 후)
        // - 6~9 라운드: 3발 (5라운드 완료 후)
        // - 10 라운드 이상: 4발 (9라운드 완료 후)
        val bulletCount = when {
            currentWave < 3 -> 1
            currentWave < 6 -> 2
            currentWave < 10 -> 3
            else -> 4
        }

        // 조준 방향(aimDirX, aimDirY)에 완벽히 수직인 좌우 평행 벡터 계산
        val perpX = -aimDirY
        val perpY = aimDirX

        // 1자 대형 총알 간의 평행 간격 폭 설정 (픽셀 단위)
        val spaceOffset = 30f

        // 총알 개수만큼 가로 정렬을 계산하여 정면 일자로 사격 가동
        for (i in 0 until bulletCount) {
            val centerOffset = (i - (bulletCount - 1) / 2f) * spaceOffset

            val finalX = bulletStartX + perpX * centerOffset
            val finalY = bulletStartY + perpY * centerOffset

            // 모든 총알은 각도가 꺾이지 않고 플레이어가 조준한 방향 그대로 평행하게 전진합니다.
            val bullet = Bullet.get(gctx, finalX, finalY, aimDirX, aimDirY, power)
            scene.world.add(bullet, MainScene.Layer.BULLET)
        }
    }

    private fun updateCollisionRect() {
        collisionRect.set(dstRect)
        collisionRect.inset(COLLISION_INSET_X, COLLISION_INSET_Y)
    }

    companion object {
        const val SPEED = 360f

        const val PLAYER_WIDTH = 120f
        const val PLAYER_HEIGHT = 120f

        const val MAX_HP = 300

        const val FIRE_INTERVAL = 0.28f
        const val BULLET_OFFSET = 65f

        const val COLLISION_INSET_X = 30f
        const val COLLISION_INSET_Y = 20f
    }
}