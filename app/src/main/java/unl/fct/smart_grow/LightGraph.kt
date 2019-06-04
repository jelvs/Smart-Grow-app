package unl.fct.smart_grow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import org.json.JSONArray
import unl.fct.smart_grow.http.HttpTask

class LightGraph : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_graph)

        val goBack = findViewById<ImageButton>(R.id.gobacklight)
        goBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        val spinner = findViewById<Spinner>(R.id.numberOfReadings)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                buildChart(spinner.selectedItem.toString().toInt())
            }
        }

        buildChart(spinner.selectedItem.toString().toInt())
    }

    private fun buildChart(numberOfReadings: Int) {
        HttpTask(this) {
            if (it == null) {
                Toast.makeText(this, "Error checking current light", Toast.LENGTH_LONG).show()
            } else {
                val response = JSONArray(it)
                setLightData(response, numberOfReadings)
            }
        }.execute("GET", "https://api.smartgrow.space/light?readings=$numberOfReadings")
    }

    @SuppressLint("NewApi")
    private fun setLightData(data: JSONArray, numberOfReadings: Int) {

        val lineChart = findViewById<LineChart>(R.id.linechart)

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Toast.makeText(applicationContext, "${e?.y.toString()}°C", Toast.LENGTH_SHORT).show()
            }
        })

        lineChart.axisRight.isEnabled = false
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)

        val values = mutableListOf<Entry>()

        for (index in 0 until data.length()) {
            val reading = data.getJSONObject(index)
            values.add(
                Entry(
                    values.size.toFloat(),
                    reading.getString("Reading").toFloat()
                )
            )
        }

        val set1 = LineDataSet(values, "Light")
        set1.lineWidth = 5f
        set1.setCircleColors(intArrayOf(R.color.green), this)
        set1.setDrawCircleHole(true)
        set1.setColors(intArrayOf(R.color.green), this)

        if (numberOfReadings >= 20) {
            set1.valueTextSize = 0f
        } else {
            set1.valueTextSize = 10f
        }

        val limitLineUpper = LimitLine(60f, "Too Bright")
        limitLineUpper.lineWidth = 4f
        limitLineUpper.enableDashedLine(10f, 10f, 0f)
        limitLineUpper.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        limitLineUpper.textSize = 15f

        val yAxis = lineChart.axisLeft
        yAxis.removeAllLimitLines()
        yAxis.addLimitLine(limitLineUpper)

        val xAxis = lineChart.xAxis
        xAxis.isEnabled = false
        xAxis.textSize = 8f

        val dataSet = listOf(set1)
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.animateX(2000)

        lineChart.invalidate()
    }

    override fun onBackPressed() {
        // do nothing
    }
}