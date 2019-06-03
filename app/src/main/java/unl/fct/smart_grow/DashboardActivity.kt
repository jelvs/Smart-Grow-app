package unl.fct.smart_grow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import pl.pawelkleczkowski.customgauge.CustomGauge
import unl.fct.smart_grow.http.HttpTask
import unl.fct.smart_grow.security.MockJwtStorage
import kotlin.concurrent.timer
import kotlin.math.roundToInt

class DashboardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        displayRoutines()

        temperatureGauge.setOnClickListener { startActivity(Intent(this, TemperatureGraph::class.java)) }
        humidityGauge.setOnClickListener { startActivity(Intent(this, HumidityGraph::class.java)) }
        soilGauge.setOnClickListener { startActivity(Intent(this, SoilGraph::class.java)) }
        lightGauge.setOnClickListener { startActivity(Intent(this, LightGraph::class.java)) }
        routines.setOnClickListener{startActivity(Intent(this, RoutineActivity::class.java)) }

        val temperatureValue = findViewById<TextView>(R.id.temperatureValue)
        val gaugeTemperature = findViewById<CustomGauge>(R.id.temperatureGauge)
        val gaugeHumidity = findViewById<CustomGauge>(R.id.humidityGauge)
        val gaugeLight = findViewById<CustomGauge>(R.id.lightGauge)
        val gaugeSoil = findViewById<CustomGauge>(R.id.soilGauge)

        setTemperature(gaugeTemperature, temperatureValue, this)
        setHumidity(gaugeHumidity, humidityValue, this)
        setLight(gaugeLight, lightValue, this)
        setSoil(gaugeSoil, soilValue, this)
    }

    private fun setTemperature(gauge: CustomGauge, textView: TextView, context: Context) {
        timer("getLastTemperature", true, 0, 2000) {
            HttpTask {
                if (it == null) {
                    Toast.makeText(context, "Error checking current temperature", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    try {
                        val response = JSONArray(it).getJSONObject(0)
                        val lastReading = response.getString("Reading")
                        gauge.value = lastReading.toDouble().roundToInt()
                        textView.text = "$lastReading°C"
                    } catch (e: Exception) {
                        gauge.value = 0
                        textView.text = "N/A"
                    }
                }
            }.execute("GET", "https://api.smartgrow.space/temperature")
        }
    }

    private fun setHumidity(gauge: CustomGauge, textView: TextView, context: Context) {
        timer("getLastHumidity", true, 0, 10000) {
            HttpTask {
                if (it == null) {
                    Toast.makeText(context, "Error checking current humidity", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    try {
                        val response = JSONArray(it).getJSONObject(0)
                        val lastReading = response.getString("Reading")
                        gauge.value = lastReading.toDouble().roundToInt()
                        textView.text = "$lastReading%"
                    } catch (e: Exception) {
                        gauge.value = 0
                        textView.text = "N/A"
                    }
                }
            }.execute("GET", "https://api.smartgrow.space/humidity")
        }
    }

    private fun setLight(gauge: CustomGauge, textView: TextView, context: Context) {
        timer("getLastLight", true, 0, 10000) {
            HttpTask {
                if (it == null) {
                    Toast.makeText(context, "Error checking current light", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    try {
                        val response = JSONArray(it).getJSONObject(0)
                        val lastReading = response.getString("Reading")
                        gauge.value = lastReading.toDouble().roundToInt()
                        textView.text = "$lastReading%"
                    } catch (e: Exception) {
                        gauge.value = 0
                        textView.text = "N/A"
                    }
                }
            }.execute("GET", "https://api.smartgrow.space/light")
        }
    }

    private fun setSoil(gauge: CustomGauge, textView: TextView, context: Context) {
        timer("getLastSoil", true, 0, 10000) {
            HttpTask {
                if (it == null) {
                    Toast.makeText(context, "Error checking current Moisture Soil", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    try {
                        val response = JSONArray(it).getJSONObject(0)
                        val lastReading = response.getString("Reading")
                        gauge.value = lastReading.toDouble().roundToInt()
                        textView.text = "$lastReading%"
                    } catch (e: Exception) {
                        gauge.value = 0
                        textView.text = "N/A"
                    }
                }
            }.execute("GET", "https://api.smartgrow.space/soil")
        }
    }

    private fun displayRoutines () {
        if (!MockJwtStorage.isAdmin){
            val routineButton = findViewById<Button>(R.id.routines)
            routineButton.visibility = View.GONE

            val lightSwitch = findViewById<Switch>(R.id.lightSwitch)
            lightSwitch.isClickable = false
            lightSwitch.isEnabled = false

            val waterSwicth = findViewById<Switch>(R.id.waterSwitch)
            waterSwicth.isClickable = false
            waterSwicth.isEnabled = false
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