package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IRecyclable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 플레이어의 HP를 회복시켜주는 아이템 객체.
 */
class HealItem private constructor(
    val context: GameContext, // CollisionChecker에서 접근할 수 있도록 val 변수로 설정합니다.
) : Sprite(context, R.drawable.heart_item), IBoxCollidable, IRecyclable {
    override var width = ITEM_WIDTH
    override var height = ITEM_HEIGHT
    override var x = 0f
    override var y = 0f

    private var speed = FALL_SPEED
    override val collisionRect = RectF()

    init {
        syncDstRect()
        updateCollisionRect()
    }

    fun init(startX: Float, startY: Float): HealItem {
        this.x = startX
        this.y = startY
        this.speed = FALL_SPEED

        syncDstRect()
        updateCollisionRect()
        return this
    }

    override fun update(gctx: GameContext) {
        y += speed * gctx.frameTime

        if (isOutOfScreen(gctx)) {
            val scene = gctx.scene as? MainScene ?: return
            scene.world.remove(this, MainScene.Layer.UI)
        }

        syncDstRect()
        updateCollisionRect()
    }

    private fun isOutOfScreen(gctx: GameContext): Boolean {
        return y > gctx.metrics.height + ITEM_HEIGHT
    }

    private fun updateCollisionRect() {
        collisionRect.set(dstRect)
    }

    override fun onRecycle() {
        speed = FALL_SPEED
    }

    companion object {
        const val ITEM_WIDTH = 80f
        const val ITEM_HEIGHT = 80f
        const val FALL_SPEED = 160f

        fun get(gctx: GameContext, x: Float, y: Float): HealItem {
            val scene = gctx.scene as? MainScene
                ?: return HealItem(gctx).init(x, y)

            val item = scene.world.obtain(HealItem::class.java) ?: HealItem(gctx)
            return item.init(x, y)
        }
    }
}