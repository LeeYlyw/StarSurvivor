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
            val enemy = enemyObject as? Enemy ?: return@forEachReversedAt

            if (player.collidesWith(enemy)) {
                Log.d(TAG, "Player collided with enemy")

                // [수정] 플레이어가 무적 상태가 아닐 때만 HP를 감소시킵니다.
                if (!player.isInvincible) {
                    player.decreaseHp(ENEMY_DAMAGE)
                } else {
                    Log.d(TAG, "Damage blocked by Shield!")
                }

                // 적 드론은 무적 상태 여부와 관계없이 부딪히면 파괴됩니다.
                scene.world.remove(enemy, MainScene.Layer.ENEMY)
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

                            // 적 파괴 시 아이템 드롭 분기 (회복 15%, 보호막 8% 확률)
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

            // 2. [신설] 보스급 특수 적 드론 충돌 가동
            val specialEnemy = enemyObject as? SpecialEnemy
            if (specialEnemy != null) {
                scene.world.forEachReversedAt(MainScene.Layer.BULLET) { bulletObject ->
                    val bullet = bulletObject as? Bullet ?: return@forEachReversedAt

                    if (bullet.collidesWith(specialEnemy)) {
                        Log.d(TAG, "Bullet hit SpecialEnemy")
                        scene.world.remove(bullet, MainScene.Layer.BULLET)

                        // 특수 적의 튼튼한 체력을 깎아내립니다.
                        specialEnemy.decreaseLife(bullet.power)
                        if (specialEnemy.dead) {
                            scene.world.remove(specialEnemy, MainScene.Layer.ENEMY)
                            scene.addScore(specialEnemy.getScore()) // 보너스 500점 획득!

                            // 특수 적 격추 시에는 생존 보상으로 회복 아이템을 100% 확률로 확정 드롭합니다.
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

                // [수정] 플레이어가 무적 상태가 아닐 때만 운석 피해를 입힙니다.
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
            // 1. 회복 아이템 충돌 처리
            val healItem = itemObject as? HealItem
            if (healItem != null && player.collidesWith(healItem)) {
                Log.d(TAG, "Player obtained HealItem")
                player.resetHp()
                scene.world.remove(healItem, MainScene.Layer.UI)
                return@forEachReversedAt
            }

            // 2. [신설] 보호막 아이템 충돌 처리
            val shieldItem = itemObject as? ShieldItem
            if (shieldItem != null && player.collidesWith(shieldItem)) {
                Log.d(TAG, "Player obtained ShieldItem")

                // 플레이어 무적 버프 가동 (4초간 무적)
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
    }
}