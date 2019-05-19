package unl.fct.smart_grow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import pl.pawelkleczkowski.customgauge.CustomGauge
import unl.fct.smart_grow.http.HttpTask
import kotlin.concurrent.timer
import kotlin.math.roundToInt

class DashboardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val barChart = findViewById<BarChart>(R.id.linechart)

        val btnBarChart = findViewById<Button>(R.id.btnBarChart)

        val btnActivityChart = findViewById<Button>(R.id.btnActivityChart)

        temperatureGauge.setOnClickListener { startActivity(Intent(this, LineChartActivity::class.java)) }
        val temperatureValue = findViewById<TextView>(R.id.temperatureValue)
        val gauge = findViewById<CustomGauge>(R.id.temperatureGauge)

        setTemperature(gauge, temperatureValue, this)
    }

    private fun setTemperature(gauge: CustomGauge, textView: TextView, context: Context) {
        timer("getLastTemperature", true, 0, 10000){
            HttpTask {
                if (it == null) {
                    Toast.makeText(context, "Error checking current temperature", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    val response = JSONArray(it).getJSONObject(0)
                    val lastReading = response.getString("Reading")
                    gauge.value = lastReading.toDouble().roundToInt()
                    textView.text = "$lastReading°C"
                }
            }.execute("GET", "https://api.smartgrow.space/temperature")
        }
    }

    /*fun turnLight() {
        val turn = findViewById<Switch>(R.id.Light)
        turn.setOnClickListener {


                //val auth = HttpTask.loginToApi(username,password)
                val json = JSONObject()

                HttpTask {
                    if (it == null) {
                        Toast.makeText(this, "Error turning the Light, Try Again", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        Toast.makeText(this, "Light is On ", Toast.LENGTH_LONG).show()
                        println(it)
                    }

                }.execute("POST", "https://api.smartgrow.space/turnLight", json.toString())


            }
        }*/
}