package com.naledimadlopha.app.widget

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Spy
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SeekBarWidgetTest {

    private lateinit var seekBarWidget: SeekBarWidget

    @Spy
    private val spyContext: Context = Robolectric.buildActivity(Activity::class.java).get()
    @Mock
    private lateinit var mockInflater: LayoutInflater
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockLeftLabelTextView: TextView
    @Mock
    private lateinit var mockRightLabelTextView: TextView
    @Mock
    private lateinit var mockTypedArray: TypedArray
    @Mock
    private lateinit var mockSeekBar: SeekBar
    @Mock
    private lateinit var mockResources: Resources
    @Mock
    private lateinit var mockOnSeekBarChangeListener: SeekBar.OnSeekBarChangeListener

    @Before
    fun setUp() {
        initMocks(this)

        doReturn(mockInflater).`when`(spyContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        `when`(mockInflater.inflate(ArgumentMatchers.eq(R.layout.seek_bar_widget_layout), ArgumentMatchers.isA(SeekBarWidget::class.java), ArgumentMatchers.eq(true))).thenReturn(mockView)

        doReturn(mockTypedArray).`when`(spyContext).obtainStyledAttributes(null, R.styleable.SeekBarWidget, 0, 0)

        `when`<TextView>(mockView.findViewById(R.id.seek_bar_widget_left_label)).thenReturn(mockLeftLabelTextView)
        `when`<TextView>(mockView.findViewById(R.id.seek_bar_widget_right_label)).thenReturn(mockRightLabelTextView)
        `when`<SeekBar>(mockView.findViewById(R.id.seek_bar_widget_app_compat_seek_bar)).thenReturn(mockSeekBar)

        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_max, 100f)).thenReturn(100f)
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_min, 0f)).thenReturn(0f)
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_interval, 0.1f)).thenReturn(0.1f)

        `when`(mockSeekBar.resources).thenReturn(mockResources)

        seekBarWidget = SeekBarWidget(spyContext)
    }

    @Test
    fun should_be_an_instance_of_LinearLayout() {
        assertTrue(LinearLayout::class.java.isInstance(seekBarWidget))
    }

    @Test
    fun on_construction_inflate_the_seek_bar_widget_layout() {
        verify(mockInflater).inflate(R.layout.seek_bar_widget_layout, seekBarWidget, true)
    }

    @Test
    fun on_construction_set_maximum_progress() {
        verify(mockTypedArray).getFloat(R.styleable.SeekBarWidget_max, 100f).toInt()
    }

    @Test
    fun on_construction_set_minimum_progress() {
        verify(mockTypedArray).getFloat(R.styleable.SeekBarWidget_min, 0f).toInt()
    }

    @Test
    fun on_construction_set_start_progress() {
        verify(mockTypedArray).getFloat(R.styleable.SeekBarWidget_start, 0f).toInt()
    }

    @Test
    fun on_construction_set_start_progress_to_minimum_progress_if_less_than_minimum_progress() {
        val progressMinimum = 6f
        val progressStart = 0f

        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_min, 0f)).thenReturn(progressMinimum)
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)

        seekBarWidget = SeekBarWidget(spyContext)

        assertEquals(progressMinimum, seekBarWidget.getProgressStart())
    }

    @Test
    fun on_construction_set_start_progress_to_maximum_progress_if_greater_than_maximum_progress() {
        val progressMaximum = 100f
        val progressMinimum = 15f
        val progressStart = 50f

        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_max, 0f)).thenReturn(progressMaximum)
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_min, 0f)).thenReturn(progressMinimum)
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)

        seekBarWidget = SeekBarWidget(spyContext)

        assertEquals(progressStart, seekBarWidget.getProgressStart())
    }

    @Test
    fun on_construction_set_start_progress_to_specified_start_progress_if_its_within_minimum_and_maximum_range() {
        val progressMaximum = 100f
        val progressStart = 120f

        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_max, 0f)).thenReturn(progressMaximum)
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)

        seekBarWidget = SeekBarWidget(spyContext)

        assertEquals(progressMaximum, seekBarWidget.getProgressStart())
    }

    @Test
    fun on_construction_use_empty_string_if_left_label_text_is_not_specified() {
        verify(mockLeftLabelTextView).text = ""
    }

    @Test
    fun on_construction_set_left_label_text_if_specified() {
        val leftLabelText = "10.25% (prime rate)"
        `when`(mockTypedArray.getString(R.styleable.SeekBarWidget_left_label)).thenReturn(leftLabelText)

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockLeftLabelTextView).text = leftLabelText
    }

    @Test
    fun on_construction_use_empty_string_if_right_label_text_is_not_specified() {
        verify(mockRightLabelTextView).text = ""
    }

    @Test
    fun on_construction_set_right_label_text_if_specified() {
        val rightLabelText = "10.25% (prime rate)"
        `when`(mockTypedArray.getString(R.styleable.SeekBarWidget_right_label)).thenReturn(rightLabelText)

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockRightLabelTextView).text = rightLabelText
    }

    @Test
    fun on_construction_set_seek_bar_padding() {
        val dimensionPixelSize = 6
        `when`(mockResources.getDimensionPixelSize(R.dimen.seek_bar_widget_slider_padding)).thenReturn(dimensionPixelSize)

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockSeekBar).setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize)
    }

    @Test
    fun on_construction_set_seek_bar_progress() {
        verify(mockSeekBar).progress = ArgumentMatchers.anyInt()
    }

    @Test
    fun on_construction_set_seek_bar_max() {
        verify(mockSeekBar).max = ArgumentMatchers.anyInt()
    }

    @Test
    fun set_left_label_text() {
        val labelText = "Left label Text"
        `when`(mockLeftLabelTextView.text).thenReturn(labelText)

        seekBarWidget.setLeftLabelText(labelText)

        assertEquals(labelText, seekBarWidget.getLeftLabelText())
    }

    @Test
    fun set_right_label_text() {
        val labelText = "Right label Text"
        `when`(mockRightLabelTextView.text).thenReturn(labelText)

        seekBarWidget.setRightLabelText(labelText)

        assertEquals(labelText, seekBarWidget.getRightLabelText())
    }

    @Test
    fun setProgressStart_should_set_the_start_progress_to_minimum_progress_if_less_than_minimum_progress() {
        val progressMinimum = 6f
        val progressStart = 0f

        seekBarWidget.setProgressMinimum(progressMinimum)
        seekBarWidget.setProgressStart(progressStart)

        assertEquals(progressMinimum, seekBarWidget.getProgressStart())
    }

    @Test
    fun setProgressStart_should_set_the_start_progress_to_maximum_progress_if_greater_than_maximum_progress() {
        val progressMaximum = 100f
        val progressStart = 120f

        seekBarWidget.setProgressMaximum(progressMaximum)
        seekBarWidget.setProgressStart(progressStart)

        assertEquals(progressMaximum, seekBarWidget.getProgressStart())
    }

    @Test
    fun setProgressStart_should_set_the_start_progress_to_specified_start_progress_if_its_within_minimum_and_maximum_range() {
        val progressMaximum = 100f
        val progressMinimum = 0f
        val progressStart = 50f

        seekBarWidget.setProgressMaximum(progressMaximum)
        seekBarWidget.setProgressMinimum(progressMinimum)
        seekBarWidget.setProgressStart(progressStart)

        assertEquals(progressStart, seekBarWidget.getProgressStart())
    }

    @Test
    fun setProgressInterval_should_set_the_progress_interval_to_default_if_less_than_default() {
        val defaultProgressInterval = 0.1f

        seekBarWidget.setProgressInterval(0f)

        assertEquals(defaultProgressInterval, seekBarWidget.getProgressInterval())
    }

    @Test
    fun setProgressInterval_should_set_the_progress_interval_to_specified_interval() {
        val progressInterval = 1f

        seekBarWidget.setProgressInterval(progressInterval)

        assertEquals(progressInterval, seekBarWidget.getProgressInterval())
        verify(mockSeekBar).max = Math.round((100f - 0f) / progressInterval)
    }

    @Test
    fun set_progress_maximum() {
        val progressMax = 1f

        seekBarWidget.setProgressMaximum(progressMax)

        assertEquals(progressMax, seekBarWidget.getProgressMaximum())
        verify(mockSeekBar).max = Math.round((progressMax - 0f) / 0.1f)
    }

    @Test
    fun set_progress_minimum() {
        val progressMin = 1f

        seekBarWidget.setProgressMinimum(progressMin)

        assertEquals(progressMin, seekBarWidget.getProgressMinimum())
        verify(mockSeekBar).max = Math.round((100f - progressMin) / 0.1f)
    }

    @Test
    fun set_seek_bar_progress() {
        val progress = 10

        seekBarWidget.setProgress(progress)

        verify(mockSeekBar).progress = progress
    }

    @Test
    fun set_seek_bar_on_progress_changed_listener() {
        seekBarWidget.setSeekBarChangedListener(mockOnSeekBarChangeListener)

        assertEquals(mockOnSeekBarChangeListener, seekBarWidget.getSeekBarChangedListener())
        verify(mockSeekBar).setOnSeekBarChangeListener(mockOnSeekBarChangeListener)
    }

}