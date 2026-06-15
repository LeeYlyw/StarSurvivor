package kr.ac.tukorea.s2019184025.spacesurvival

import android.graphics.Canvas
import android.util.Log
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.collidesWith
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

/**
 * MainScene에서 발생하는 모든 오브젝트 간의 충돌을 검사하는 클래스.
 */
class CollisionChecker : IGameObject {

    override fun update(gctx: GameContext) {
        val scene = gctx.scene as? MainScene ?: return
        val player = scene.player

        checkPlayerEnemyCollision(scene, player)
        checkBulletEnemyCollision(scene, gctx)
        checkPlayerAsteroidCollision(scene, player)
        checkPlayerItemCollision(scene, player)
    }

    private fun checkPlayerEnemyCollision(scene: MainScene, player: Player) {
        scene.world.forEachReversedAt(MainScene.Layer.ENEMY) { enemyObject ->

            // 1. 일반 적 드론 충돌 처리
            val enemy = enemyObject as? Enemy
            if (enemy != null && player.collidesWith(enemy)) {
                Log.d(TAG, "Player collided with enemy")

                if (!player.isInvincible) {
                    player.decreaseHp(ENEMY_DAMAGE)
                } else {
                    Log.d(TAG, "Damage blocked by Shield!")
                }

                scene.world.remove(enemy, MainScene.Layer.ENEMY)
                return@forEachReversedAt
            }

            // 2. 특수 적 드론 충돌 처리
            val specialEnemy = enemyObject as? SpecialEnemy
            if (specialEnemy != null && player.collidesWith(specialEnemy)) {
                Log.d(TAG, "Player collided with SpecialEnemy")

                if (!player.isInvincible) {
                    player.decreaseHp(SPECIAL_ENEMY_DAMAGE)
                } else {
                    Log.d(TAG, "SpecialEnemy damage blocked by Shield!")
                }

                scene.world.remove(specialEnemy, MainScene.Layer.ENEMY)
                return@forEachReversedAt
            }
        }
    }

    private fun checkBulletEnemyCollision(scene: MainScene, gctx: GameContext) {
        scene.world.forEachReversedAt(MainScene.Layer.ENEMY) { enemyObject ->

            // 1. 일반 적 드론 충돌 가동
            val enemy = enemyObject as? Enemy
            if (enemy != null) {
                scene.world.forEachReversedAt(MainScene.Layer.BULLET) { bulletObject ->
                    val bullet = bulletObject as? Bullet ?: return@forEachReversedAt

                    if (bullet.collidesWith(enemy)) {
                        Log.d(TAG, "Bullet hit enemy")
                        scene.world.remove(bullet, MainScene.Layer.BULLET)

                        enemy.decreaseLife(bullet.power)
                        if (enemy.dead) {
                            scene.world.remove(enemy, MainScene.Layer.ENEMY)
                            scene.addScore(enemy.getScore())

                            val rand = kotlin.random.Random.nextInt(100)
                            if (rand < 15) {
                                val healItem = HealItem.get(gctx, enemy.x, enemy.y)
                                scene.world.add(healItem, MainScene.Layer.UI)
                            } else if (rand < 23) {
                                val shieldItem = ShieldItem.get(gctx, enemy.x, enemy.y)
                                scene.world.add(shieldItem, MainScene.Layer.UI)
                            }
                        }
                    }
                }
                return@forEachReversedAt
            }

            // 2. 보스급 특수 적 드론 충돌 가동
            val specialEnemy = enemyObject as? SpecialEnemy
            if (specialEnemy != null) {
                scene.world.forEachReversedAt(MainScene.Layer.BULLET) { bulletObject ->
                    val bullet = bulletObject as? Bullet ?: return@forEachReversedAt

                    if (bullet.collidesWith(specialEnemy)) {
                        Log.d(TAG, "Bullet hit SpecialEnemy")
                        scene.world.remove(bullet, MainScene.Layer.BULLET)

                        specialEnemy.decreaseLife(bullet.power)
                        if (specialEnemy.dead) {
                            scene.world.remove(specialEnemy, MainScene.Layer.ENEMY)
                            scene.addScore(specialEnemy.getScore())

                            val healItem = HealItem.get(gctx, specialEnemy.x, specialEnemy.y)
                            scene.world.add(healItem, MainScene.Layer.UI)
                        }
                    }
                }
                return@forEachReversedAt
            }
        }
    }

    private fun checkPlayerAsteroidCollision(scene: MainScene, player: Player) {
        scene.world.forEachReversedAt(MainScene.Layer.ASTEROID) { asteroidObject ->
            val asteroid = asteroidObject as? Asteroid ?: return@forEachReversedAt

            if (player.collidesWith(asteroid)) {
                Log.d(TAG, "Player collided with asteroid")

                if (!player.isInvincible) {
                    player.decreaseHp(Asteroid.DAMAGE)
                } else {
                    Log.d(TAG, "Asteroid damage blocked by Shield!")
                }

                scene.world.remove(asteroid, MainScene.Layer.ASTEROID)
            }
        }
    }

    private fun checkPlayerItemCollision(scene: MainScene, player: Player) {
        scene.world.forEachReversedAt(MainScene.Layer.UI) { itemObject ->
            val healItem = itemObject as? HealItem
            if (healItem != null && player.collidesWith(healItem)) {
                Log.d(TAG, "Player obtained HealItem")
                player.resetHp()
                scene.world.remove(healItem, MainScene.Layer.UI)
                return@forEachReversedAt
            }

            val shieldItem = itemObject as? ShieldItem
            if (shieldItem != null && player.collidesWith(shieldItem)) {
                Log.d(TAG, "Player obtained ShieldItem")

                player.activateShield(ShieldItem.SHIELD_DURATION)

                scene.world.remove(shieldItem, MainScene.Layer.UI)
                return@forEachReversedAt
            }
        }
    }

    override fun draw(canvas: Canvas) {
    }

    companion object {
        private const val TAG = "CollisionChecker"
        private const val ENEMY_DAMAGE = 10
        private const val SPECIAL_ENEMY_DAMAGE = 25
    }
}