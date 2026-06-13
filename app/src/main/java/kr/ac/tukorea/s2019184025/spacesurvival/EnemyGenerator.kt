package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * MainScene에서 적 드론을 일정 시간마다 생성하는 클래스.
 *
 * 생존형 슈팅 게임에 맞게 적은 화면 위쪽에서만 내려오는 것이 아니라,
 * 화면 바깥 가장자리에서 생성되어 플레이어 방향으로 접근한다.
 */
class EnemyGenerator(
    private val gctx: GameContext,
) : IGameObject {
    private var enemyTime = 0f
    private var wave = 0

    override fun update(gctx: GameContext) {
        // [보충] MainScene의 상태가 PLAY가 아니라면 적을 생성하는 타이머를 흐르지 않게 차단합니다.
        val scene = gctx.scene as? MainScene ?: return
        if (scene.gameState != MainScene.State.PLAY) return

        enemyTime -= gctx.frameTime
        if (enemyTime > 0f) return

        wave++
        generateEnemies()
        enemyTime = GEN_INTERVAL
    }

    private fun generateEnemies() {
        val scene = gctx.scene as? MainScene ?: return
        val speed = Enemy.DEFAULT_SPEED + (wave - 1) * SPEED_STEP

        repeat(COUNT_PER_WAVE) {
            val spawn = getSpawnPosition()
            val direction = getDirectionToPlayer(
                enemyX = spawn.first,
                enemyY = spawn.second,
                player = scene.player,
            )

            // ────────── 👇 여기서부터 수정 및 추가 코드 👇 ──────────
            // 매 스폰 객체마다 15%의 확률로 보스급 'SpecialEnemy'를 출격시킵니다.
            if (kotlin.random.Random.nextInt(100) < 15) {
                scene.world.add(
                    SpecialEnemy.get(
                        gctx = gctx,
                        x = spawn.first,
                        y = spawn.second,
                        dirX = direction.first,
                        dirY = direction.second,
                        wave = wave
                    ),
                    MainScene.Layer.ENEMY // 일반 적과 동일하게 ENEMY 레이어에서 관리되어 이동/출력됩니다.
                )
            } else {
                // 85%의 확률로 기존의 일반 적 드론을 스폰시킵니다.
                scene.world.add(
                    Enemy.get(
                        gctx = gctx,
                        x = spawn.first,
                        y = spawn.second,
                        dirX = direction.first,
                        dirY = direction.second,
                        level = getEnemyLevel(),
                        speed = speed,
                    ),
                    MainScene.Layer.ENEMY,
                )
            }
            // ────────── 👆 여기까지 수정 및 추가 코드 👆 ──────────
        }
    }

    private fun getSpawnPosition(): Pair<Float, Float> {
        val screenWidth = gctx.metrics.width
        val screenHeight = gctx.metrics.height
        val margin = Enemy.ENEMY_WIDTH

        return when (Random.nextInt(4)) {
            0 -> Pair(Random.nextFloat() * screenWidth, -margin)                 // 위쪽 바깥
            1 -> Pair(Random.nextFloat() * screenWidth, screenHeight + margin)   // 아래쪽 바깥
            2 -> Pair(-margin, Random.nextFloat() * screenHeight)                // 왼쪽 바깥
            else -> Pair(screenWidth + margin, Random.nextFloat() * screenHeight) // 오른쪽 바깥
        }
    }

    private fun getDirectionToPlayer(
        enemyX: Float,
        enemyY: Float,
        player: Player,
    ): Pair<Float, Float> {
        val dx = player.x - enemyX
        val dy = player.y - enemyY
        val length = sqrt(dx * dx + dy * dy)

        if (length <= 0.0001f) {
            return Pair(0f, 1f)
        }

        return Pair(dx / length, dy / length)
    }

    private fun getEnemyLevel(): Int {
        val baseLevel = (wave + 8) / 10
        val variedLevel = baseLevel - Random.nextInt(3)

        return variedLevel.coerceIn(1, Enemy.MAX_LEVEL_COUNT)
    }

    override fun draw(canvas: Canvas) {
        // EnemyGenerator는 화면에 직접 그려지는 오브젝트가 아니라
        // Enemy 생성 시점만 관리한다.
    }

    companion object {
        const val GEN_INTERVAL = 3f
        const val COUNT_PER_WAVE = 5
        const val SPEED_STEP = 20f
    }
}