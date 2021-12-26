package project.movies.audiotours.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.view.LayoutInflater
import android.widget.RemoteViews
import androidx.annotation.RawRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import project.movies.audiotours.R
import project.movies.audiotours.databinding.NotificationPlayerBinding

class MusicService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        initBinding()
        mediaPlayer = instanceMediaPlayer(R.raw.music)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playMusic()

        playSound()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun playMusic() {
        startForeground(SERVICE_MUSIC, createPlayerNotification())
        NotificationManagerCompat.from(this).notify(SERVICE_MUSIC, createPlayerNotification())
    }

    private fun createPlayerNotification(): Notification {
        return NotificationCompat.Builder(this, NotificationMusicChannel.PLAY_CHANNEL_ID)
            .setContentTitle("Play music")
            .setSmallIcon(R.drawable.ic_play)
            .setCustomBigContentView(remoteViews())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()
    }

    private fun remoteViews(): RemoteViews {
        return RemoteViews(packageName, R.layout.notification_player).apply {
            setTextViewText(R.id.tw_information1, "Text")
        }
    }

    companion object {
        const val SERVICE_MUSIC = 1234
    }

    private var mediaPlayer: MediaPlayer? = null
    private var binding: NotificationPlayerBinding? = null

    private fun initBinding() {
        val inflater = LayoutInflater.from(this)
        binding = NotificationPlayerBinding.inflate(inflater)
    }

    private fun setIconButtonPlay(isIconPlay: Boolean) {
        val icon = if (isIconPlay)
            R.drawable.ic_play
        else
            R.drawable.ic_pause

        binding!!.btStartPause1.setImageDrawable(
            ResourcesCompat.getDrawable(resources, icon, null)
        )
    }

    private fun instanceMediaPlayer(@RawRes raw: Int): MediaPlayer {
        val player = MediaPlayer.create(this, raw)
        player.isLooping = true
        return player
    }

    private fun playSound() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            setIconButtonPlay(true)
        } else {
            mediaPlayer!!.start()
            setIconButtonPlay(false)
        }
    }
}