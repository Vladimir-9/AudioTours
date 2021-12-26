package project.movies.audiotours.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationMusicChannel {

    const val PLAY_CHANNEL_ID = "play"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createPlayChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPlayChannel(context: Context) {
        val name = "play music"
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(PLAY_CHANNEL_ID, name, priority)
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}