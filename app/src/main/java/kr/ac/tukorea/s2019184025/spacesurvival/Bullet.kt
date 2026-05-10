package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IRecyclable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.atan2

/**
 * 플레이어가 자동으로 발사하는 탄환.
 *
 * 생성 시점에 전달받은 방향(dirX, dirY)으로 이동한다.
 * 플레이어가 바라보는 방향과 같은 방향으로 발사되도록 구성한다.
 */
class Bullet private constructor(
    gctx: GameContext,
) : Sprite(gctx, R.drawable.bullet), IBoxCollidable, IRecyclable {
    override var width = BULLET_WIDTH
    override var height = BULLET_HEIGHT
    override var x = 0f
    override var y = 0f

    var power = 1
        private set

    private var dirX = 0f
    private var dirY = -1f
    private var angle = 0f

    override val collisionRect: RectF
        get() = dstRect

    init {
        syncDstRect()
    }

    fun init(
        startX: Float,
        startY: Float,
        dirX: Float,
        dirY: Float,
        power: Int,
    ): Bullet {
        x = startX
        y = startY
        this.dirX = dirX
        this.dirY = dirY
        this.power = power

        angle = Math.toDegrees(atan2(dirY.toDouble(), dirX.toDouble())).toFloat() + 90f

        syncDstRect()
        return this
    }

    override fun update(gctx: GameContext) {
        x += dirX * SPEED * gctx.frameTime
        y += dirY * SPEED * gctx.frameTime

        syncDstRect()

        if (isOutOfScreen(gctx)) {
            val scene = gctx.scene as? MainScene ?: return
            scene.world.remove(this, MainScene.Layer.BULLET)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(angle, x, y)
        super.draw(canvas)
        canvas.restore()
    }

    private fun isOutOfScreen(gctx: GameContext): Boolean {
        return x < -BULLET_WIDTH ||
                x > gctx.metrics.width + BULLET_WIDTH ||
                y < -BULLET_HEIGHT ||
                y > gctx.metrics.height + BULLET_HEIGHT
    }

    override fun onRecycle() {
        power = 1
        dirX = 0f
        dirY = -1f
        angle = 0f
    }

    companion object {
        const val BULLET_WIDTH = 28f
        const val BULLET_HEIGHT = 56f
        const val SPEED = 1500f

        fun get(
            gctx: GameContext,
            x: Float,
            y: Float,
            dirX: Float,
            dirY: Float,
            power: Int,
        ): Bullet {
            val scene = gctx.scene as? MainScene
                ?: return Bullet(gctx).init(x, y, dirX, dirY, power)

            val bullet = scene.world.obtain(Bullet::class.java) ?: Bullet(gctx)
            return bullet.init(x, y, dirX, dirY, power)
        }
    }
}