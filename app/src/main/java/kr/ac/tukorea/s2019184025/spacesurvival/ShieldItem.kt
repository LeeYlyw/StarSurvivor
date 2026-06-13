package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IBoxCollidable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IRecyclable
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 플레이어에게 일정 시간 무적 버프를 주는 보호막 아이템 객체.
 * 적이 파괴될 때 낮은 확률로 스폰되어 아래로 직선 이동한다.
 */
class ShieldItem private constructor(
    val gameContext: GameContext,
) : Sprite(gameContext, R.drawable.shield_item), IBoxCollidable, IRecyclable {
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

    fun init(startX: Float, startY: Float): ShieldItem {
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
        const val FALL_SPEED = 140f // 회복 아이템보다 약간 느리게 내려오도록 설정
        const val SHIELD_DURATION = 4.0f // 보호막 지속 시간 (4초)

        fun get(gctx: GameContext, x: Float, y: Float): ShieldItem {
            val scene = gctx.scene as? MainScene
                ?: return ShieldItem(gctx).init(x, y)

            val item = scene.world.obtain(ShieldItem::class.java) ?: ShieldItem(gctx)
            return item.init(x, y)
        }
    }
}