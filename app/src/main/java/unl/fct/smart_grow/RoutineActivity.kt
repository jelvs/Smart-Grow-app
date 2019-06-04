package unl.fct.smart_grow

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_routine.view.*
import org.json.JSONObject
import unl.fct.smart_grow.http.HttpTask
import unl.fct.smart_grow.security.MockJwtStorage
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.min

class RoutineActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    private var year: Int = -1;
    private var month: Int = -1;
    private var dayOfMonth: Int = -1;

    private var hour: Int = -1
    private var min: Int = -1

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        setListeners()
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners(){
        findViewById<Button>(R.id.select_date).setOnClickListener{editDate()}
        findViewById<Button>(R.id.select_time).setOnClickListener{editTime()}

        val createRoutine = findViewById<Button>(R.id.create_routine)
        createRoutine.setOnClickListener {
            val spinner = findViewById<Spinner>(R.id.switch_light_water)

            val json = JSONObject()
            json.put("output", spinner.selectedItem.toString())
            json.put("datetime", (LocalDateTime.of(year, month, dayOfMonth, hour, min).toEpochSecond(ZoneOffset.UTC)).toString())

            HttpTask(this) {
                if (it == null) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    return@HttpTask
                }else{
                    Toast.makeText(this, "Routine Created Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, RoutineList::class.java))
                }

            }.execute("POST", "https://api.smartgrow.space/routine", json.toString())
        }

        val goBack = findViewById<ImageButton>(R.id.gobackroutine)
        goBack.setOnClickListener {
            startActivity(Intent(this, RoutineList::class.java))
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun editDate() {
        val datePickerDialog = DatePickerDialog(this, R.style.datepicker)

        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->

            findViewById<TextView>(R.id.datePicker).text = "$year/${month + 1}/$dayOfMonth"
            this.year = year
            this.month = month + 1
            this.dayOfMonth = dayOfMonth
        }
        datePickerDialog.show()
    }


    private fun editTime() {
        val listener = OnTimeSetListener { view, hourOfDay, minute ->
            findViewById<TextView>(R.id.timePicker).text = "$hourOfDay:$minute"
            hour = hourOfDay
            min = minute
        }
        val timePickerDialog = TimePickerDialog(this, R.style.timepicker, listener, 0, 0, true)

        timePickerDialog.show()
    }

    override fun onBackPressed() {
        // do nothing
    }
}
