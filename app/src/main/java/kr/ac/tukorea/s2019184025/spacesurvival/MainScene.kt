package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.VertScrollBackground
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * 실제 게임 플레이 및 시작/게임오버 화면 전환이 진행되는 메인 Scene.
 */
class MainScene(gctx: GameContext) : Scene(gctx) {

    enum class State {
        START,      // 시작 대기 화면
        PLAY,       // 인게임 플레이
        GAMEOVER    // 게임오버 화면
    }

    enum class Layer {
        BACKGROUND,
        PLAYER,
        BULLET,
        ENEMY,
        ASTEROID,
        CONTROLLER,
        UI,
    }

    override val clipsRect = true

    // 현재 게임의 상태 (기본값은 시작 화면)
    var gameState = State.START
        private set

    // UI 문구를 직접 그리기 위한 Paint 객체
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

    // 무한 루프 렉을 방지하기 위해 개별 update 호출을 제거하고 부모 기능을 활용합니다.
    override fun update(gctx: GameContext) {
        when (gameState) {
            State.START -> {
                // 시작 화면일 때는 월드 내부의 배경 레이어만 부분적으로 업데이트하고 싶지만,
                // 안전을 위해 우선 전체 world의 흐름을 태우되 플레이어나 생성기가 돌지 않게 제어하는 것이 좋습니다.
                // 일단은 무한 루프 방지를 위해 부모의 update를 안전하게 호출합니다.
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
        // 교수님 프레임워크가 world에 등록된 모든 요소를 기본적으로 그리도록 먼저 수행합니다.
        super.draw(canvas)

        when (gameState) {
            State.START -> {
                // 인게임 요소를 투명한 검은색 레이어로 살짝 덮어 배경만 보이고 인게임은 가려지게 처리할 수 있습니다.
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
                // 플레이 상태일 때는 추가적인 덮어쓰기 문구 없이 인게임 화면만 정상 출력합니다.
            }
            State.GAMEOVER -> {
                // 게임오버 시 화면을 붉은 톤으로 살짝 덮어줍니다.
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

    // 기존에 작성했던 onTouchEvent를 아래와 같이 restartGame() 호출 구조로 수정합니다.
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
                    // [수정] 게임오버 상태에서 터치 시 리셋 함수를 호출하도록 변경합니다.
                    restartGame()
                    return true
                }
            }
        }

        return if (gameState == State.PLAY) player.onTouchEvent(event) else true
    }

    // [신설] 완벽한 게임 리셋을 위한 재시작 함수입니다. companion object 바로 위에 배치하세요.
    private fun restartGame() {
        // 1. 플레이어 HP 복구 및 위치를 화면 중앙으로 초기화
        player.resetStatus()
        player.x = gctx.metrics.width / 2f
        player.y = gctx.metrics.height / 2f

        // 2. [교정] world.clear() 대신 교수님 프레임워크의 레이어 순회 구조를 활용해
        // 화면에 남아있는 총알, 적, 운석 오브젝트들을 리스트 역순으로 안전하게 제거합니다.

        // 적 드론 싹 비우기
        world.forEachReversedAt(Layer.ENEMY) { enemyObject ->
            world.remove(enemyObject, Layer.ENEMY)
        }

        // 총알 싹 비우기
        world.forEachReversedAt(Layer.BULLET) { bulletObject ->
            world.remove(bulletObject, Layer.BULLET)
        }

        // 운석 장애물 싹 비우기
        world.forEachReversedAt(Layer.ASTEROID) { asteroidObject ->
            world.remove(asteroidObject, Layer.ASTEROID)
        }

        // 3. 점수 객체 초기화는 잠시 후 Score.kt에 reset()을 만들어서 연결할 예정입니다.
        // 현재는 빨간 줄이 뜨지 않도록 주석 처리해 둡니다.
         score.reset()

        // 4. 게임 상태를 플레이 상태로 돌려놓기
        gameState = State.PLAY
    }

    companion object {
        const val BACKGROUND_SPEED = 40f
    }
}