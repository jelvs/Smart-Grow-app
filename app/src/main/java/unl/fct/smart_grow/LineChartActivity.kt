package unl.fct.smart_grow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.*

class LineChartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)

        val lineChart = findViewById<LineChart>(R.id.linechart)
        /*lineChart.onChartGestureListener = this
        lineChart.setOnChartValueSelectedListener(this)*/

        lineChart.axisRight.isEnabled = false
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)

        val values = listOf(
            Entry(0f, 60f),
            Entry(1f, 50f),
            Entry(2f, 70f),
            Entry(3f, 30f),
            Entry(4f, 20f),
            Entry(5f, 90f)
        )

        val values2 = listOf(
            Entry(0f, 30f),
            Entry(1f, 10f),
            Entry(2f, 70f),
            Entry(3f, 43f),
            Entry(4f, 65f),
            Entry(5f, 81f)
        )

        val set1 = LineDataSet(values, "Set 1")
        set1.lineWidth = 5f
        set1.setCircleColors(intArrayOf(R.color.black), this)
        set1.setColors (intArrayOf(R.color.black), this)
        set1.valueTextSize = 10f

        val set2 = LineDataSet(values2, "Set 2")
        set2.lineWidth = 5f
        set2.setCircleColors(intArrayOf(R.color.green), this)
        set2.setColors (intArrayOf(R.color.green), this)
        set2.valueTextSize = 10f

        val limitLineUpper = LimitLine(70f, "Too Hot")
        limitLineUpper.lineWidth = 4f
        limitLineUpper.enableDashedLine(10f, 10f, 0f)
        limitLineUpper.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        limitLineUpper.textSize = 15f

        val yAxis = lineChart.axisLeft
        yAxis.removeAllLimitLines()
        yAxis.addLimitLine(limitLineUpper)


        val dataSet = listOf(set1, set2)
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate()
    }
}