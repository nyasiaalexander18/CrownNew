package games.for_fun.crownnew.constants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import games.for_fun.crownnew.activities.offline_games.OfflineGamesActivity
import games.for_fun.crownnew.activities.play_pokies.PlayPokiesActivity
import games.for_fun.crownnew.activities.play_slots.PlaySlotsActivity

infix fun AppCompatActivity.makeIntent(id: Int) = Intent(this, classById(id))

fun classById(id: Int): Class<*> {
    return when(id) {
        Intents.OFFLINE_GAMES -> OfflineGamesActivity::class.java
        Intents.PLAY_POKIES -> PlayPokiesActivity::class.java
        Intents.PLAY_SLOTS -> PlaySlotsActivity::class.java
        else -> throw IndexOutOfBoundsException()
    }
}

object Intents {
    const val OFFLINE_GAMES = 0
    const val PLAY_POKIES = 1
    const val PLAY_SLOTS = 2
}