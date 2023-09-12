package games.for_fun.crownnew.activities

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import games.for_fun.crownnew.constants.Intents
import games.for_fun.crownnew.constants.makeIntent

class GameAction {
    val currentBet = MutableLiveData(10)
    val bank = MutableLiveData(0)
    val lastWin = MutableLiveData(0)

    val textBank = MutableLiveData(0)
    val textLastWin = MutableLiveData(0)

    fun setBankValue(value: Int) {
        textBank.value = value
        bank.value = value
    }

    fun setLastWinValue(value: Int) {
        textLastWin.value = value
        lastWin.value = value
    }

    fun getBackAction(activity: AppCompatActivity): (View) -> Unit = {
        activity.startActivity((activity makeIntent Intents.OFFLINE_GAMES).apply {
            putExtra("bank", bank.value.toString().toInt())
            putExtra("last_win", lastWin.value.toString().toInt())
            putExtra("current_bet", currentBet.value.toString().toInt())
        })
        activity.finish()
    }
}