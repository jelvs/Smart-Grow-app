package unl.fct.smart_grow

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_routine.view.*

class RoutineActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        setListeners()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners(){
        val selectDate = findViewById<Button>(R.id.select_date).setOnClickListener{editDate()}
        val selectTime = findViewById<Button>(R.id.select_time).setOnClickListener{editTime()}
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun editDate() {
        val datePickerDialog = DatePickerDialog(this, R.style.datepicker)

        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->

            findViewById<TextView>(R.id.datePicker).text = "$year/$month/$dayOfMonth"

        }
        datePickerDialog.show()
    }


    private fun editTime() {


        val listenner = object : OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                findViewById<TextView>(R.id.timePicker).text = "$hourOfDay:$minute"
            }
        }
        val timePickerDialog = TimePickerDialog(this, listenner, 0, 0, true)

        timePickerDialog.show()
    }
}
