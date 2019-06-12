package unl.fct.smart_grow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import unl.fct.smart_grow.http.HttpTask
import unl.fct.smart_grow.utils.Routine
import unl.fct.smart_grow.utils.RoutineHelper


class RoutineList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_list)

        setListeners()
        fillTable()
    }

    private fun setListeners() {
        val goBack = findViewById<ImageButton>(R.id.gobackroutinelist)
        goBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        val createRoutine = findViewById<Button>(R.id.create_routine_list)
        createRoutine.setOnClickListener {
            startActivity(Intent(this, RoutineActivity::class.java))
        }
    }

    override fun onBackPressed() {
        // do nothing
    }

//    private fun setTable() {
//        fillTable()
////        HttpTask(this) {
////            if (it == null) {
////                Toast.makeText(this, "Error checking current soils", Toast.LENGTH_LONG).show()
////            } else {
////                val response = JSONArray(it)
////                fillTable(response)
////            }
////        }.execute("GET", "https://api.smartgrow.space/routine")
//    }

    private fun fillTable() {
        val container = findViewById<TableLayout>(R.id.listRoutines)
        container.removeAllViews()

        val inflater = LayoutInflater.from(this)

        if (RoutineHelper.routines.isEmpty()) {
            val child = TextView(this)

            child.text = "No routines set..."
            container.addView(child)
        } else {
            for (index in 0 until RoutineHelper.routines.size) {
                val routine = RoutineHelper.routines[index]

                val child = inflater.inflate(R.layout.routine_table_item, container, false) as LinearLayout

                child.findViewById<TextView>(R.id.motor).text = routine.output
                child.findViewById<TextView>(R.id.dateof).text = routine.dateTime.toString()
                child.findViewById<TextView>(R.id.onoff).text = if (routine.state) "ON" else "OFF"

                child.findViewById<ImageButton>(R.id.action).setOnClickListener {

                    RoutineHelper.deleteRoutine(routine)
                    finish()
                    startActivity(Intent(this, RoutineList::class.java))

//                    val json = JSONObject()
//                    json.put("id", routine.getInt("Id"))
//

//                    //TODO: delete locally

//
//                    HttpTask(this) {
//                        if (it == null) {
//                            //
//                        } else {
//                            finish()
//                            startActivity(Intent(this, RoutineList::class.java))
//                        }
//                    }.execute("DELETE", "https://api.smartgrow.space/routine", json.toString())
                }

                container.addView(child)
            }
        }
    }
}
