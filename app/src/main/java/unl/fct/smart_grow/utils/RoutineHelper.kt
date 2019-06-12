package unl.fct.smart_grow.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import unl.fct.smart_grow.http.HttpTask
import java.time.LocalDateTime
import kotlin.concurrent.thread

object RoutineHelper: Activity() {

    val routines = mutableListOf<Routine>()
    private var routineChecker: Thread? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun start () {
        routineChecker = thread(true, false, null, "routineChecker") {
            while (true) {
                val toRemove = mutableListOf<Int>()
                routines.forEachIndexed { index, routine ->
                    if (LocalDateTime.now().isAfter(routine.dateTime)){
                        executeRoutine(routine)
                        toRemove.add(index)
                    }
                }

                toRemove.forEach {
                    routines.removeAt(it)
                }

                Thread.sleep(1000)
            }
        }
    }

    fun addRoutine (routine: Routine) {
        routines.add(routine)
    }

    fun deleteRoutine (routine: Routine){
        this.routines.remove(routine)
    }

    private fun executeRoutine (routine: Routine) {
        if (routine.output == "Water"){
                HttpTask(this) {
                    if (it == null) {
                        //Toast.makeText(this, "Error turning On the Water, Try Again", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        //Toast.makeText(this, "Water is On ", Toast.LENGTH_LONG).show()
                        StateSwitches.water = true
                        println(it)
                    }
                }.execute("POST", "http://192.168.43.140/water", "ON")

        }else{
            HttpTask(this) {
                if (it == null) {
                    //Toast.makeText(this, "Error turning On the Water, Try Again", Toast.LENGTH_LONG).show()
                    return@HttpTask
                }else{
                    //Toast.makeText(this, "Water is On ", Toast.LENGTH_LONG).show()
                    StateSwitches.water = true
                    println(it)
                }
            }.execute("POST", "http://192.168.43.140/light", "ON")
            println("LIGHT")
        }
        println ("executing routine")
    }
}