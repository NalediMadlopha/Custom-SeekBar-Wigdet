package com.naledimadlopha.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

class SeekBarWidget : LinearLayout {

    private var leftLabelTextView: TextView
    private var rightLabelTextView: TextView
    private var seekBar: SeekBar
    private lateinit var seekBarChangeListener: SeekBar.OnSeekBarChangeListener

    private var progressStart = 0f
    private var progressInterval = 0.1f
    private var progressMaximum = 100f
    private var progressMinimum = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        val view = LayoutInflater.from(context).inflate(R.layout.seek_bar_widget_layout, this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBarWidget, defStyleAttr, 0)

        leftLabelTextView = view.findViewById(R.id.seek_bar_widget_left_label)
        rightLabelTextView = view.findViewById(R.id.seek_bar_widget_right_label)
        seekBar = view.findViewById(R.id.seek_bar_widget_app_compat_seek_bar)

        try {
            progressMaximum = typedArray.getFloat(R.styleable.SeekBarWidget_max, 100f)
            progressMinimum = typedArray.getFloat(R.styleable.SeekBarWidget_min, 0f)
            progressStart = typedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)
                    .coerceAtMost(progressMaximum)
                    .coerceAtLeast(progressMinimum)
            progressInterval = typedArray.getFloat(R.styleable.SeekBarWidget_interval, 0.1f)
                    .coerceAtLeast(0.1f)

            leftLabelTextView.text = typedArray.getString(R.styleable.SeekBarWidget_left_label) ?: ""
            rightLabelTextView.text = typedArray.getString(R.styleable.SeekBarWidget_right_label) ?: ""

            val dimensionPixelSize = seekBar.resources.getDimensionPixelSize(R.dimen.seek_bar_widget_slider_padding)
            seekBar.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize)
            seekBar.progress = Math.round((progressStart - progressMinimum) / progressInterval)
            setSeekBarMaximum()
        } finally {
            typedArray.recycle()
        }
    }

    fun setLeftLabelText(labelText: String) {
        this.leftLabelTextView.text = labelText
    }

    fun getLeftLabelText(): String = leftLabelTextView.text.toString()

    fun setRightLabelText(labelText: String) {
        this.rightLabelTextView.text = labelText
    }

    fun getRightLabelText(): String = rightLabelTextView.text.toString()

    fun setProgressStart(progressStart: Float) {
        this.progressStart = progressStart.coerceAtLeast(progressMinimum).coerceAtMost(progressMaximum)
    }

    fun getProgressStart(): Float = progressStart

    fun setProgressInterval(progressInterval: Float) {
        this.progressInterval = progressInterval.coerceAtLeast(0.1f)
        setSeekBarMaximum()
    }

    fun getProgressInterval(): Float = progressInterval

    fun setProgressMaximum(progressMaximum: Float) {
        this.progressMaximum = progressMaximum
        setSeekBarMaximum()
    }

    fun getProgressMaximum(): Float = progressMaximum

    fun setProgressMinimum(progressMinimum: Float) {
        this.progressMinimum = progressMinimum
        setSeekBarMaximum()
    }

    fun getProgressMinimum(): Float = progressMinimum

    fun setProgress(progress: Int) {
        seekBar.progress = progress
    }

    fun setSeekBarChangedListener(seekBarChangeListener: SeekBar.OnSeekBarChangeListener) {
        this.seekBarChangeListener = seekBarChangeListener
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    fun getSeekBarChangedListener(): SeekBar.OnSeekBarChangeListener = seekBarChangeListener

    private fun setSeekBarMaximum() {
        seekBar.max = Math.round((progressMaximum - progressMinimum) / progressInterval)
    }

}