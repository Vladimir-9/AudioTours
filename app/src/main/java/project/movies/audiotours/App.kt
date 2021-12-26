package project.movies.audiotours

import android.app.Application
import project.movies.audiotours.service.NotificationMusicChannel

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationMusicChannel.create(this)
    }
}