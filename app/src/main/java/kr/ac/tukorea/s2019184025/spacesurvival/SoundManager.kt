package kr.ac.tukorea.s2019184025.spacesurvival

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

object SoundManager {
    private const val TAG = "SoundManager"
    private var soundPool: SoundPool? = null

    private var shootSoundId = 0
    private var explodeSoundId = 0

    // 특정 프레임워크 객체 없이 범용 Context를 받도록 고정
    fun init(context: Context) {
        if (soundPool != null) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        shootSoundId = soundPool?.load(context, R.raw.shoot_sound, 1) ?: 0
        explodeSoundId = soundPool?.load(context, R.raw.explode_sound, 1) ?: 0

        Log.d(TAG, "SoundPool Initialized")
    }

    fun playShoot() {
        soundPool?.play(shootSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playExplode() {
        soundPool?.play(explodeSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}