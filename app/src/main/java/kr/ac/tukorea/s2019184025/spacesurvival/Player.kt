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

    val dead: Boolean
        get() = hp <= 0

    override val collisionRect = RectF()

    init {
        syncDstRect()
        updateCollisionRect()
    }

    override fun update(gctx: GameContext) {
        if (dead) return

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
        val power = 10 + scene.getScore() / 1000

        val bulletStartX = x + aimDirX * BULLET_OFFSET
        val bulletStartY = y + aimDirY * BULLET_OFFSET

        val bullet = Bullet.get(
            gctx = gctx,
            x = bulletStartX,
            y = bulletStartY,
            dirX = aimDirX,
            dirY = aimDirY,
            power = power,
        )

        scene.world.add(bullet, MainScene.Layer.BULLET)
    }

    private fun updateCollisionRect() {
        collisionRect.set(dstRect)
        collisionRect.inset(COLLISION_INSET_X, COLLISION_INSET_Y)
    }

    companion object {
        const val SPEED = 360f

        const val PLAYER_WIDTH = 120f
        const val PLAYER_HEIGHT = 120f

        const val MAX_HP = 100

        const val FIRE_INTERVAL = 0.28f
        const val BULLET_OFFSET = 65f

        const val COLLISION_INSET_X = 30f
        const val COLLISION_INSET_Y = 20f
    }
}