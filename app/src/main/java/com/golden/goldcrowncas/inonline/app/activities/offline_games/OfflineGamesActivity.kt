package com.golden.goldcrowncas.inonline.app.activities.offline_games

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.golden.goldcrowncas.inonline.app.R
import com.golden.goldcrowncas.inonline.app.activities.GameAction
import com.golden.goldcrowncas.inonline.app.constants.Intents
import com.golden.goldcrowncas.inonline.app.constants.makeIntent

class OfflineGamesActivity: AppCompatActivity() {
    private val gameAction = GameAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_games)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        gameAction.setBankValue(intent.getIntExtra("bank", 10000))
        gameAction.setLastWinValue(intent.getIntExtra("last_win", 0))
        gameAction.currentBet.value = intent.getIntExtra("current_bet", 10)
        findViewById<AppCompatButton>(R.id.butt_play_pokies).setOnClickListener {
            startActivity((this makeIntent Intents.PLAY_POKIES).apply {
                putExtra("bank", gameAction.bank.value.toString().toInt())
                putExtra("last_win", gameAction.lastWin.value.toString().toInt())
                putExtra("current_bet", gameAction.currentBet.value.toString().toInt())
            })
            finish()
        }
        findViewById<AppCompatButton>(R.id.butt_play_slots).setOnClickListener {
            startActivity((this makeIntent Intents.PLAY_SLOTS).apply {
                putExtra("bank", gameAction.bank.value.toString().toInt())
                putExtra("last_win", gameAction.lastWin.value.toString().toInt())
                putExtra("current_bet", gameAction.currentBet.value.toString().toInt())
            })
            finish()
        }
    }
}