package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IRecyclable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 파괴 불가능한 운석 장애물.
 *
 * 화면 가장자리 바깥에서 생성되어 직선으로 이동한다.
 * Bullet과 충돌해도 파괴되지 않으며,
 * Player와 충돌하면 Player의 HP를 감소시킨다.
 */
class Asteroid private constructor(
    private val gctx: GameContext,
) : Sprite(gctx, R.drawable.asteroid), IBoxCollidable, IRecyclable {
    override var width = ASTEROID_WIDTH
    override var height = ASTEROID_HEIGHT
    override var x = 0f
    override var y = 0f

    private var dirX = 0f
    private var dirY = 1f
    private var speed = DEFAULT_SPEED

    override val collisionRect = RectF()

    init {
        syncDstRect()
        updateCollisionRect()
    }

    fun init(
        x: Float,
        y: Float,
        dirX: Float,
        dirY: Float,
        speed: Float,
    ): Asteroid {
        this.x = x
        this.y = y
        this.dirX = dirX
        this.dirY = dirY
        this.speed = speed

        syncDstRect()
        updateCollisionRect()

        return this
    }

    override fun update(gctx: GameContext) {
        x += dirX * speed * gctx.frameTime
        y += dirY * speed * gctx.frameTime

        if (isOutOfScreen(gctx)) {
            val scene = gctx.scene as? MainScene ?: return
            scene.world.remove(this, MainScene.Layer.ASTEROID)
        }

        syncDstRect()
        updateCollisionRect()
    }

    private fun isOutOfScreen(gctx: GameContext): Boolean {
        val marginX = ASTEROID_WIDTH
        val marginY = ASTEROID_HEIGHT

        return x < -marginX ||
                x > gctx.metrics.width + marginX ||
                y < -marginY ||
                y > gctx.metrics.height + marginY
    }

    private fun updateCollisionRect() {
        collisionRect.set(dstRect)
        collisionRect.inset(COLLISION_INSET, COLLISION_INSET)
    }

    override fun onRecycle() {
        dirX = 0f
        dirY = 1f
        speed = DEFAULT_SPEED
    }

    companion object {
        const val ASTEROID_WIDTH = 130f
        const val ASTEROID_HEIGHT = 130f
        const val DEFAULT_SPEED = 180f
        const val COLLISION_INSET = 15f
        const val DAMAGE = 25

        fun get(
            gctx: GameContext,
            x: Float,
            y: Float,
            dirX: Float,
            dirY: Float,
            speed: Float,
        ): Asteroid {
            val scene = gctx.scene as? MainScene
                ?: return Asteroid(gctx).init(x, y, dirX, dirY, speed)

            val asteroid = scene.world.obtain(Asteroid::class.java) ?: Asteroid(gctx)
            return asteroid.init(x, y, dirX, dirY, speed)
        }
    }
}