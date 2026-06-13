package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Score(
    private val player: Player,
) : IGameObject {
    // MainScene에서 접근할 수 있도록 private set을 유지합니다.
    var score = 0
        private set

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = 48f
        isAntiAlias = true
    }

    fun add(value: Int) {
        score += value
    }

    // [신설] 재시작 시 점수를 0으로 되돌리기 위한 초기화 함수입니다.
    fun reset() {
        score = 0
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText("Score: $score", 40f, 70f, paint)
        canvas.drawText("HP: ${player.hp}", 40f, 130f, paint)
    }
}