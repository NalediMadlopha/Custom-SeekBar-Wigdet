package com.naledimadlopha.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

class SeekBarWidget : LinearLayout, SeekBar.OnSeekBarChangeListener {

    private var leftLabelTextView: TextView
    private var rightLabelTextView: TextView
    private var seekBar: SeekBar
    private lateinit var seekBarChangeListener: SeekBar.OnSeekBarChangeListener

    private var leftLabelFormat: String
    private var rightLabelFormat: String

    private var progressStart = 0f
    private var progressInterval = 0.1f
    private var progressMaximum = 100f
    private var progressMinimum = 0f
    private var progressValue = progressStart

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
            progressValue = progressStart
            progressInterval = typedArray.getFloat(R.styleable.SeekBarWidget_interval, 0.1f)
                    .coerceAtLeast(0.1f)

            leftLabelFormat = typedArray.getString(R.styleable.SeekBarWidget_left_label) ?: DEFAULT_LABEL_FORMAT
            leftLabelTextView.text = String.format(leftLabelFormat, progressStart)

            rightLabelFormat = typedArray.getString(R.styleable.SeekBarWidget_right_label) ?: DEFAULT_LABEL_FORMAT
            rightLabelTextView.text = String.format(rightLabelFormat, progressStart)

            val dimensionPixelSize = seekBar.resources.getDimensionPixelSize(R.dimen.seek_bar_widget_slider_padding)
            seekBar.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        /* no-op */
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        /* no-op */
    }

    fun setLeftLabelText(labelText: String) {
        this.leftLabelFormat = labelText
        this.leftLabelTextView.text = String.format(labelText, progressValue)
    }

    fun getLeftLabelText(): String = leftLabelTextView.text.toString()

    fun setRightLabelText(labelText: String) {
        this.rightLabelFormat = labelText
        this.rightLabelTextView.text = String.format(labelText, progressValue)
    }

    fun getRightLabelText(): String = rightLabelTextView.text.toString()

    fun setProgressStart(progressStart: Float) {
        this.progressStart = progressStart
    }

    fun getProgressStart(): Float = progressStart

    fun setProgressInterval(progressInterval: Float) {
        this.progressInterval = progressInterval
    }

    fun getProgressInterval(): Float = progressInterval

    fun setProgressMaximum(progressMaximum: Float) {
        this.progressMaximum = progressMaximum
    }

    fun getProgressMaximum(): Float = progressMaximum

    fun setProgressMinimum(progressMinimum: Float) {
        this.progressMinimum = progressMinimum
    }

    fun getProgressMinimum(): Float = progressMinimum

    companion object {
        private const val DEFAULT_LABEL_FORMAT = "%s"
    }

}