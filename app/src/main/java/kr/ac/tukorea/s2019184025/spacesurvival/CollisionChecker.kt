package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.util.Log
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.collidesWith
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * MainScene에서 발생하는 충돌을 검사하는 클래스.
 *
 * 현재는 기본 구조 단계이므로 다음 두 가지 충돌만 처리한다.
 * 1. Player - Enemy 충돌
 * 2. Bullet - Enemy 충돌
 *
 * 이후 4주차 구현에서 Asteroid 충돌과 Player 체력 감소 처리를 추가할 예정이다.
 */
class CollisionChecker : IGameObject {
    override fun update(gctx: GameContext) {
        val scene = gctx.scene as? MainScene ?: return
        val player = scene.player

        checkPlayerEnemyCollision(scene, player)
        checkBulletEnemyCollision(scene)
        checkPlayerAsteroidCollision(scene, player)
    }

    private fun checkPlayerEnemyCollision(scene: MainScene, player: Player) {
        scene.world.forEachReversedAt(MainScene.Layer.ENEMY) { enemyObject ->
            val enemy = enemyObject as? Enemy ?: return@forEachReversedAt

            if (player.collidesWith(enemy)) {
                Log.d(TAG, "Player collided with enemy")

                // 2주차 기본 구조에서는 충돌한 적만 제거한다.
                // 4주차에서 Player HP 감소 및 GameOver 처리로 확장할 예정이다.
                player.decreaseHp(ENEMY_DAMAGE)
                scene.world.remove(enemy, MainScene.Layer.ENEMY)            }
        }
    }

    private fun checkBulletEnemyCollision(scene: MainScene) {
        scene.world.forEachReversedAt(MainScene.Layer.ENEMY) { enemyObject ->
            val enemy = enemyObject as? Enemy ?: return@forEachReversedAt

            scene.world.forEachReversedAt(MainScene.Layer.BULLET) { bulletObject ->
                val bullet = bulletObject as? Bullet ?: return@forEachReversedAt

                if (bullet.collidesWith(enemy)) {
                    Log.d(TAG, "Bullet hit enemy")

                    scene.world.remove(bullet, MainScene.Layer.BULLET)

                    enemy.decreaseLife(bullet.power)
                    if (enemy.dead) {
                        scene.world.remove(enemy, MainScene.Layer.ENEMY)
                        scene.addScore(enemy.getScore())
                    }
                }
            }
        }
    }

    private fun checkPlayerAsteroidCollision(scene: MainScene, player: Player) {
        scene.world.forEachReversedAt(MainScene.Layer.ASTEROID) { asteroidObject ->
            val asteroid = asteroidObject as? Asteroid ?: return@forEachReversedAt

            if (player.collidesWith(asteroid)) {
                player.decreaseHp(Asteroid.DAMAGE)
                scene.world.remove(asteroid, MainScene.Layer.ASTEROID)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        // 충돌 검사는 화면에 직접 그릴 내용이 없으므로 draw에서는 처리하지 않는다.
    }

    companion object {
        private const val TAG = "CollisionChecker"
        private const val ENEMY_DAMAGE = 10

    }
}