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
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.roundToInt

class DashboardActivity : AppCompatActivity() {

    private var temperatureTimer: Timer? = null
    private var humidityTimer: Timer? = null
    private var soilTimer: Timer? = null
    private var lightTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        displayRoutines()
        turnLight()
        turnWater()

        findViewById<Switch>(R.id.lightSwitch).isChecked = StateSwitches.light
        findViewById<Switch>(R.id.waterSwitch).isChecked = StateSwitches.water

        temperatureGauge.setOnClickListener { finish(); startActivity(Intent(this, TemperatureGraph::class.java)) }
        humidityGauge.setOnClickListener { finish(); startActivity(Intent(this, HumidityGraph::class.java)) }
        soilGauge.setOnClickListener { finish(); startActivity(Intent(this, SoilGraph::class.java)) }
        lightGauge.setOnClickListener { finish(); startActivity(Intent(this, LightGraph::class.java)) }
        routines.setOnClickListener { finish(); startActivity(Intent(this, RoutineList::class.java)) }

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
        temperatureTimer = timer("getLastTemperature", true, 0, 10000) {
            HttpTask(this@DashboardActivity) {
                if (it == null) {
                    //Toast.makeText(context, "Error checking current temperature", Toast.LENGTH_LONG).show()
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
        humidityTimer = timer("getLastHumidity", true, 0, 10000) {
            HttpTask(this@DashboardActivity) {
                if (it == null) {
                    //Toast.makeText(context, "Error checking current humidity", Toast.LENGTH_LONG).show()
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
        lightTimer = timer("getLastLight", true, 0, 10000) {
            HttpTask(this@DashboardActivity) {
                if (it == null) {
                    //Toast.makeText(context, "Error checking current light", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    try {
                        val response = JSONArray(it).getJSONObject(0)
                        val lastReading = response.getString("Reading")
                        gauge.value = lastReading.toDouble().roundToInt()
                        textView.text = "${lastReading.toDouble().roundToInt()}%"
                    } catch (e: Exception) {
                        gauge.value = 0
                        textView.text = "N/A"
                    }
                }
            }.execute("GET", "https://api.smartgrow.space/light")
        }
    }

    private fun setSoil(gauge: CustomGauge, textView: TextView, context: Context) {
        soilTimer = timer("getLastSoil", true, 0, 10000) {
            HttpTask(this@DashboardActivity) {
                if (it == null) {
                    //Toast.makeText(context, "Error checking current Moisture Soil", Toast.LENGTH_LONG).show()
                    gauge.value = 0
                    textView.text = "N/A"
                } else {
                    try {
                        val response = JSONArray(it).getJSONObject(0)
                        val lastReading = response.getString("Reading")
                        gauge.value = lastReading.toDouble().roundToInt()
                        textView.text = "${lastReading.toDouble().roundToInt()}%"
                    } catch (e: Exception) {
                        gauge.value = 0
                        textView.text = "N/A"
                    }
                }
            }.execute("GET", "https://api.smartgrow.space/soil")
        }
    }

    private fun displayRoutines() {
        if (!MockJwtStorage.isAdmin) {
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

    override fun onBackPressed() {
        // do nothing
    }

    override fun finish() {
        temperatureTimer?.cancel()
        humidityTimer?.cancel()
        soilTimer?.cancel()
        lightTimer?.cancel()

        super.finish()
    }

    fun turnLight() {
        val turn = findViewById<Switch>(R.id.lightSwitch)
        turn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                //Toast.makeText(this, "ON", Toast.LENGTH_LONG).show()
                //val auth = HttpTask.loginToApi(username,password)
                HttpTask(this@DashboardActivity) {
                    if (it == null) {
                        Toast.makeText(this, "Error turning On the Light, Try Again", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        Toast.makeText(this, "Light is On ", Toast.LENGTH_LONG).show()
                        StateSwitches.light = true
                        println(it)
                    }
                }.execute("POST", "http://192.168.43.140/light", "ON")
            } else{
                //Toast.makeText(this, "OFF", Toast.LENGTH_LONG).show()
                //val auth = HttpTask.loginToApi(username,password)
                HttpTask(this@DashboardActivity) {
                    if (it == null) {
                        Toast.makeText(this, "Error turning Off the Light, Try Again", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        Toast.makeText(this, "Light is Off ", Toast.LENGTH_LONG).show()
                        println(it)
                        StateSwitches.light = false
                    }
                }.execute("POST", "http://192.168.43.140/light", "OFF")
            }
        }
    }

    fun turnWater() {
        val turn = findViewById<Switch>(R.id.waterSwitch)
        turn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                //Toast.makeText(this, "ON", Toast.LENGTH_LONG).show()
                //val auth = HttpTask.loginToApi(username,password)
                HttpTask(this@DashboardActivity) {
                    if (it == null) {
                        Toast.makeText(this, "Error turning On the Water, Try Again", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        Toast.makeText(this, "Water is On ", Toast.LENGTH_LONG).show()
                        StateSwitches.water = true
                        println(it)
                    }
                }.execute("POST", "http://192.168.43.140/water", "ON")
            } else{
                //Toast.makeText(this, "OFF", Toast.LENGTH_LONG).show()
                //val auth = HttpTask.loginToApi(username,password)
                HttpTask(this@DashboardActivity) {
                    if (it == null) {
                        Toast.makeText(this, "Error turning Off the Water, Try Again", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        Toast.makeText(this, "Water is Off ", Toast.LENGTH_LONG).show()
                        StateSwitches.water = false
                        println(it)
                    }
                }.execute("POST", "http://192.168.43.140/water", "OFF")
            }
        }
    }


}