package project.movies.audiotours

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import project.movies.audiotours.service.MusicService
import project.movies.audiotours.ui.MainFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment())
            .commit()
    }

    override fun onStop() {
        super.onStop()
        val playerIntent = Intent(this, MusicService::class.java)
        this.startService(playerIntent)
    }
}