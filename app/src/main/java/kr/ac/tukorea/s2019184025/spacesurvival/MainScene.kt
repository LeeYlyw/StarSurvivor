package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.VertScrollBackground
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class MainScene(gctx: GameContext) : Scene(gctx) {

    enum class State {
        START, PLAY, GAMEOVER
    }

    enum class Layer {
        BACKGROUND, PLAYER, BULLET, ENEMY, ASTEROID, CONTROLLER, UI
    }

    override val clipsRect = true

    var gameState = State.START
        private set

    private val uiTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

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

    override fun update(gctx: GameContext) {
        when (gameState) {
            State.START -> {
                super.update(gctx)
            }
            State.PLAY -> {
                super.update(gctx)
                if (player.dead) {
                    gameState = State.GAMEOVER
                }
            }
            State.GAMEOVER -> {
                super.update(gctx)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        when (gameState) {
            State.START -> {
                val dimPaint = Paint().apply { color = Color.parseColor("#BB000000") }
                canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), dimPaint)

                uiTextPaint.color = Color.WHITE
                uiTextPaint.textSize = 80f
                canvas.drawText("STAR SURVIVOR", canvas.width / 2f, canvas.height / 3f, uiTextPaint)

                uiTextPaint.textSize = 45f
                uiTextPaint.color = Color.YELLOW
                canvas.drawText("Touch Screen to Start", canvas.width / 2f, canvas.height / 2f, uiTextPaint)
            }
            State.PLAY -> {
            }
            State.GAMEOVER -> {
                val dimPaint = Paint().apply { color = Color.parseColor("#88FF0000") }
                canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), dimPaint)

                uiTextPaint.color = Color.WHITE
                uiTextPaint.textSize = 100f
                canvas.drawText("GAME OVER", canvas.width / 2f, canvas.height / 3f, uiTextPaint)

                uiTextPaint.color = Color.YELLOW
                uiTextPaint.textSize = 55f
                canvas.drawText("Final Score: ${getScore()}", canvas.width / 2f, canvas.height / 2f, uiTextPaint)

                uiTextPaint.textSize = 40f
                uiTextPaint.color = Color.GREEN
                canvas.drawText("Touch to Restart Game", canvas.width / 2f, canvas.height * 0.65f, uiTextPaint)
            }
        }
    }

    fun addScore(amount: Int) {
        score.add(amount)
    }

    fun getScore(): Int {
        return score.score
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            when (gameState) {
                State.START -> {
                    gameState = State.PLAY
                    return true
                }
                State.PLAY -> {
                    return player.onTouchEvent(event)
                }
                State.GAMEOVER -> {
                    restartGame()
                    return true
                }
            }
        }
        return if (gameState == State.PLAY) player.onTouchEvent(event) else true
    }

    private fun restartGame() {
        player.resetStatus()
        player.x = gctx.metrics.width / 2f
        player.y = gctx.metrics.height / 2f

        world.forEachReversedAt(Layer.ENEMY) { enemyObject ->
            world.remove(enemyObject, Layer.ENEMY)
        }

        world.forEachReversedAt(Layer.BULLET) { bulletObject ->
            world.remove(bulletObject, Layer.BULLET)
        }

        world.forEachReversedAt(Layer.ASTEROID) { asteroidObject ->
            world.remove(asteroidObject, Layer.ASTEROID)
        }

        score.reset()
        gameState = State.PLAY
    }

    companion object {
        const val BACKGROUND_SPEED = 40f
    }
}