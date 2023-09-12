package games.for_fun.crownnew.activities.play_pokies

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import games.for_fun.crownnew.R
import games.for_fun.crownnew.activities.CanSpin
import games.for_fun.crownnew.activities.GameAction
import games.for_fun.crownnew.constants.Gradients
import games.for_fun.crownnew.constants.Intents
import games.for_fun.crownnew.constants.makeIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.random.Random

class PlayPokiesActivity: AppCompatActivity(), CanSpin {
    private val gameAction = GameAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_pokies)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        gameAction.setBankValue(intent.getIntExtra("bank", 10000))
        gameAction.setLastWinValue(intent.getIntExtra("last_win", 0))
        gameAction.currentBet.value = intent.getIntExtra("current_bet", 10)

        val bank = findViewById<AppCompatTextView>(R.id.tv_bank)
        bank.paint.shader = Gradients.getGold(bank.width, bank.textSize)
        val lastWin = findViewById<AppCompatTextView>(R.id.tv_last_win)
        lastWin.paint.shader = Gradients.getGold(bank.width, bank.textSize)
        val currentBet = findViewById<AppCompatTextView>(R.id.tv_current_bet)
        currentBet.paint.shader = Gradients.getGold(bank.width, bank.textSize)

        findViewById<AppCompatImageButton>(R.id.img_b_minus).setOnClickListener {
            gameAction.currentBet.value?.run {
                if(this > 10) {
                    gameAction.currentBet.value = this - 10
                }
            }
        }

        findViewById<AppCompatImageButton>(R.id.img_b_plus).setOnClickListener {
            gameAction.currentBet.value?.run {
                gameAction.currentBet.value = this + 10
            }
        }

        gameAction.currentBet.observe(this) {
            currentBet.text = getString(R.string.tv_current_bet, it)
        }
        gameAction.textBank.observe(this) {
            bank.text = getString(R.string.tv_bank, it)
        }
        gameAction.textLastWin.observe(this) {
            lastWin.text = getString(R.string.tv_last_win, it)
        }

        findViewById<AppCompatButton>(R.id.butt_spin).setOnClickListener {
            it.isEnabled = false
            spin()
        }

        findViewById<AppCompatImageButton>(R.id.img_b_back).setOnClickListener(gameAction.getBackAction(this))
    }

    override fun spin() {
        if((gameAction.bank.value ?: 0) > (gameAction.currentBet.value ?: 0)) {
            gameAction.textBank.value = (gameAction.textBank.value ?: 0) - (gameAction.currentBet.value ?: 0)
            gameAction.textLastWin.value = 0
            thread {
                val bet = gameAction.currentBet.value ?: 0
                runBlocking {
                    val slots = listOf<AppCompatImageView>(
                        findViewById(R.id.img_v_slot_elem_01),
                        findViewById(R.id.img_v_slot_elem_02),
                        findViewById(R.id.img_v_slot_elem_03),
                        findViewById(R.id.img_v_slot_elem_04),

                        findViewById(R.id.img_v_slot_elem_05),
                        findViewById(R.id.img_v_slot_elem_06),
                        findViewById(R.id.img_v_slot_elem_07),
                        findViewById(R.id.img_v_slot_elem_08),

                        findViewById(R.id.img_v_slot_elem_09),
                        findViewById(R.id.img_v_slot_elem_10),
                        findViewById(R.id.img_v_slot_elem_11),
                        findViewById(R.id.img_v_slot_elem_12),

                        findViewById(R.id.img_v_slot_elem_13),
                        findViewById(R.id.img_v_slot_elem_14),
                        findViewById(R.id.img_v_slot_elem_15),
                        findViewById(R.id.img_v_slot_elem_16),
                    )
                    val idFromTag: (Int) -> Int = {it + R.drawable.img_slot_elem_01 }
                    val convertTag: Any.() -> Int = {(this as String).toInt()}
                    repeat(60) {
                        slots[15].setImageResource(idFromTag(slots[11].tag.convertTag()))
                        slots[15].tag = slots[11].tag
                        slots[14].setImageResource(idFromTag(slots[10].tag.convertTag()))
                        slots[14].tag = slots[10].tag
                        slots[13].setImageResource(idFromTag(slots[9].tag.convertTag()))
                        slots[13].tag = slots[9].tag
                        slots[12].setImageResource(idFromTag(slots[8].tag.convertTag()))
                        slots[12].tag = slots[8].tag

                        slots[11].setImageResource(idFromTag(slots[7].tag.convertTag()))
                        slots[11].tag = slots[7].tag
                        slots[10].setImageResource(idFromTag(slots[6].tag.convertTag()))
                        slots[10].tag = slots[6].tag
                        slots[9].setImageResource(idFromTag(slots[5].tag.convertTag()))
                        slots[9].tag = slots[5].tag
                        slots[8].setImageResource(idFromTag(slots[4].tag.convertTag()))
                        slots[8].tag = slots[4].tag

                        slots[7].setImageResource(idFromTag(slots[3].tag.convertTag()))
                        slots[7].tag = slots[3].tag
                        slots[6].setImageResource(idFromTag(slots[2].tag.convertTag()))
                        slots[6].tag = slots[2].tag
                        slots[5].setImageResource(idFromTag(slots[1].tag.convertTag()))
                        slots[5].tag = slots[1].tag
                        slots[4].setImageResource(idFromTag(slots[0].tag.convertTag()))
                        slots[4].tag = slots[0].tag

                        slots[3].tag = Random.nextInt(0, 7).toString()
                        slots[3].setImageResource(idFromTag(slots[3].tag.convertTag()))
                        slots[2].tag = Random.nextInt(0, 7).toString()
                        slots[2].setImageResource(idFromTag(slots[2].tag.convertTag()))
                        slots[1].tag = Random.nextInt(0, 7).toString()
                        slots[1].setImageResource(idFromTag(slots[1].tag.convertTag()))
                        slots[0].tag = Random.nextInt(0, 7).toString()
                        slots[0].setImageResource(idFromTag(slots[0].tag.convertTag()))

                        delay(50)
                    }
                    runOnUiThread {
                        gameAction.setLastWinValue(0)
                        for(i in 0..7) {
                            try {
                                if (
                                    (slots[0].tag.convertTag() == i || slots[4].tag.convertTag() == i || slots[8].tag.convertTag() == i || slots[12].tag.convertTag() == i)
                                    &&
                                    (slots[1].tag.convertTag() == i || slots[5].tag.convertTag() == i || slots[9].tag.convertTag() == i || slots[13].tag.convertTag() == i)
                                    &&
                                    (slots[2].tag.convertTag() == i || slots[6].tag.convertTag() == i || slots[10].tag.convertTag() == i || slots[14].tag.convertTag() == i)
                                    &&
                                    (slots[3].tag.convertTag() == i || slots[7].tag.convertTag() == i || slots[11].tag.convertTag() == i || slots[15].tag.convertTag() == i)
                                ) {
                                    gameAction.setLastWinValue(
                                        (gameAction.lastWin.value ?: 0) + when (i) {
                                            0 -> 10
                                            1 -> 20
                                            3 -> 30
                                            4 -> 40
                                            5 -> 50
                                            6 -> 60
                                            7 -> 70
                                            else -> {
                                                throw IllegalArgumentException("$i")
                                            }
                                        } * (bet / 10)
                                    )
                                }
                            }
                            catch (e: Exception) {
                                Toast.makeText(this@PlayPokiesActivity,
                                    e.stackTrace.joinToString("\n"), Toast.LENGTH_LONG).show()
                            }
                        }
                        gameAction.setBankValue((gameAction.bank.value ?: 0) - bet + (gameAction.lastWin.value ?: 0))
                        findViewById<AppCompatButton>(R.id.butt_spin).isEnabled = true
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        findViewById<AppCompatImageButton>(R.id.img_b_back).performClick()
    }
}