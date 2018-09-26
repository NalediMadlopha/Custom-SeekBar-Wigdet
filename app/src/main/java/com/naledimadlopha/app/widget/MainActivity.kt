package com.naledimadlopha.app.widget

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekBarWidget = findViewById<SeekBarWidget>(R.id.seek_bar)
        seekBarWidget.setRightLabelText("10.25% (prime rate)")

        seekBarWidget.setSeekBarChangedListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val progressValue = (progress * seekBarWidget.getProgressInterval()) + seekBarWidget.getProgressMinimum()

                when {
                    progressValue < 10.25 -> {
                        val labelValue = String.format(getString(R.string.right_label_prime_minus), decimalFormat.format(progressValue), (10.25 - progressValue))
                        seekBarWidget.setRightLabelText(labelValue)
                    }
                    progressValue > 10.25 -> {
                        val labelValue = String.format(getString(R.string.right_label_prime_plus), decimalFormat.format(progressValue), (progressValue - 10.25))
                        seekBarWidget.setRightLabelText(labelValue)
                    }
                    else -> seekBarWidget.setRightLabelText("10.25% (prime rate)")
                }

                seekBarWidget.setProgress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                /* no-ops*/
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                /* no-ops*/
            }

        })
    }

    companion object {
        private val decimalFormat = DecimalFormat("##.##")
    }
}
