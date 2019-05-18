package unl.fct.smart_grow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

import com.github.mikephil.charting.charts.BarChart;
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONObject
import pl.pawelkleczkowski.customgauge.CustomGauge

class DashboardActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val barChart =  findViewById<BarChart>(R.id.linechart)

        val btnBarChart = findViewById<Button>(R.id.btnBarChart)

        val btnActivityChart = findViewById<Button>(R.id.btnActivityChart)

        temperatureGauge.setOnClickListener { startActivity(Intent(this, LineChartActivity::class.java)) }
        val temperatureValue = findViewById<TextView>(R.id.temperatureValue).text.toString()
        val gauge = findViewById<CustomGauge>(R.id.temperatureGauge)
        gauge.value = temperatureValue.split("°")[0].toInt()
    }

    fun turnLight() {
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
        }


}