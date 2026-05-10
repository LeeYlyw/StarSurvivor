package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.random.Random

/**
 * 파괴 불가능한 운석을 낮은 빈도로 생성하는 클래스.
 *
 * 운석은 화면을 과도하게 복잡하게 만들지 않기 위해
 * 한 번에 1개씩만 생성되며, 플레이어를 추적하지 않고 직선으로 이동한다.
 */
class AsteroidGenerator(
    private val gctx: GameContext,
) : IGameObject {
    private var asteroidTime = GEN_INTERVAL
    private var wave = 0

    override fun update(gctx: GameContext) {
        asteroidTime -= gctx.frameTime
        if (asteroidTime > 0f) return

        wave++
        generateAsteroid()
        asteroidTime = getGenerateInterval()
    }

    private fun generateAsteroid() {
        val scene = gctx.scene as? MainScene ?: return

        val spawnData = getSpawnData()
        val speed = Asteroid.DEFAULT_SPEED + wave * SPEED_STEP

        scene.world.add(
            Asteroid.get(
                gctx = gctx,
                x = spawnData.x,
                y = spawnData.y,
                dirX = spawnData.dirX,
                dirY = spawnData.dirY,
                speed = speed,
            ),
            MainScene.Layer.ASTEROID,
        )
    }

    private fun getSpawnData(): SpawnData {
        val screenWidth = gctx.metrics.width
        val screenHeight = gctx.metrics.height
        val margin = Asteroid.ASTEROID_WIDTH

        return when (Random.nextInt(4)) {
            0 -> {
                // 위에서 아래로
                SpawnData(
                    x = Random.nextFloat() * screenWidth,
                    y = -margin,
                    dirX = 0f,
                    dirY = 1f,
                )
            }

            1 -> {
                // 아래에서 위로
                SpawnData(
                    x = Random.nextFloat() * screenWidth,
                    y = screenHeight + margin,
                    dirX = 0f,
                    dirY = -1f,
                )
            }

            2 -> {
                // 왼쪽에서 오른쪽으로
                SpawnData(
                    x = -margin,
                    y = Random.nextFloat() * screenHeight,
                    dirX = 1f,
                    dirY = 0f,
                )
            }

            else -> {
                // 오른쪽에서 왼쪽으로
                SpawnData(
                    x = screenWidth + margin,
                    y = Random.nextFloat() * screenHeight,
                    dirX = -1f,
                    dirY = 0f,
                )
            }
        }
    }

    private fun getGenerateInterval(): Float {
        return (GEN_INTERVAL - wave * INTERVAL_STEP).coerceAtLeast(MIN_INTERVAL)
    }

    override fun draw(canvas: Canvas) {
        // AsteroidGenerator는 화면에 직접 그려지는 오브젝트가 아니라
        // Asteroid 생성 시점만 관리한다.
    }

    data class SpawnData(
        val x: Float,
        val y: Float,
        val dirX: Float,
        val dirY: Float,
    )

    companion object {
        const val GEN_INTERVAL = 7f
        const val MIN_INTERVAL = 4.5f
        const val INTERVAL_STEP = 0.1f
        const val SPEED_STEP = 8f
    }
}