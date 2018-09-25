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
    fun should_be_an_instance_of_OnSeekBarChangeListener() {
        assertTrue(SeekBar.OnSeekBarChangeListener::class.java.isInstance(seekBarWidget))
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
    fun on_construction_use_default_string_format_if_not_specified_for_leftLabelTextView_text() {
        val progressStart = 10f
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockLeftLabelTextView).text = progressStart.toString()
    }

    @Test
    fun on_construction_use_string_format_if_specified_for_leftLabelTextView_text() {
        val progressStart = 10.25f
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)
        `when`(mockTypedArray.getString(R.styleable.SeekBarWidget_left_label)).thenReturn("%s (prime rate)")

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockLeftLabelTextView).text = "$progressStart (prime rate)"
    }

    @Test
    fun on_construction_use_default_string_format_if_not_specified_for_rightLabelTextView_text() {
        val progressStart = 10f
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockRightLabelTextView).text = progressStart.toString()
    }

    @Test
    fun on_construction_use_string_format_if_specified_for_rightLabelTextView_text() {
        val progressStart = 10.25f
        `when`(mockTypedArray.getFloat(R.styleable.SeekBarWidget_start, 0f)).thenReturn(progressStart)
        `when`(mockTypedArray.getString(R.styleable.SeekBarWidget_right_label)).thenReturn("%s (prime rate)")

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockRightLabelTextView).text = "$progressStart (prime rate)"
    }

    @Test
    fun on_construction_set_seek_bar_padding() {
        val dimensionPixelSize = 6
        `when`(mockResources.getDimensionPixelSize(R.dimen.seek_bar_widget_slider_padding)).thenReturn(dimensionPixelSize)

        seekBarWidget = SeekBarWidget(spyContext)

        verify(mockSeekBar).setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize)
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
    fun set_start_progress() {
        val progressStart = 10.0f

        seekBarWidget.setProgressStart(progressStart)

        assertEquals(progressStart, seekBarWidget.getProgressStart())
    }

    @Test
    fun set_progress_interval() {
        val progressInterval = 1f

        seekBarWidget.setProgressInterval(progressInterval)

        assertEquals(progressInterval, seekBarWidget.getProgressInterval())
    }

    @Test
    fun set_progress_maximum() {
        val progressMax = 1f

        seekBarWidget.setProgressMaximum(progressMax)

        assertEquals(progressMax, seekBarWidget.getProgressMaximum())
    }

    @Test
    fun set_progress_minimum() {
        val progressMin = 1f

        seekBarWidget.setProgressMinimum(progressMin)

        assertEquals(progressMin, seekBarWidget.getProgressMinimum())
    }

}