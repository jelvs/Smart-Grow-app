package unl.fct.smart_grow.utils

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Switch
import unl.fct.smart_grow.http.HttpTask
import java.time.LocalDateTime
import kotlin.concurrent.thread

@SuppressLint("StaticFieldLeak")
object RoutineHelper {

    private var waterSwitch: Switch? = null
    private var lightSwitch: Switch? = null

    val routines = mutableListOf<Routine>()
    private var routineChecker: Thread? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun start(lightSwitch: Switch, waterSwitch: Switch) {
        this.lightSwitch = lightSwitch
        this.waterSwitch = waterSwitch

        if (routineChecker == null) {
            routineChecker = thread(true, false, null, "routineChecker") {
                while (true) {
                    val toRemove = mutableListOf<Int>()
                    routines.forEachIndexed { index, routine ->
                        if (LocalDateTime.now().isAfter(routine.dateTime)) {
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
    }

    fun addRoutine(routine: Routine) {
        routines.add(routine)
    }

    fun deleteRoutine(routine: Routine) {
        this.routines.remove(routine)
    }

    private fun executeRoutine(routine: Routine) {
        println("executing routine: ${routine.output}, ${routine.state}")

        val onOff = if (routine.state) {
            "ON"
        } else {
            "OFF"
        }

        if (routine.output == "Water") {
            HttpTask(null) {
                if (it == null) {
                    return@HttpTask
                } else {
                    StateSwitches.turnOnOffWater(waterSwitch!!, routine.state)
                    println(it)
                }
            }.execute("POST", "${ApiConfig.arduinoIp}/water", onOff)

        } else {
            HttpTask(null) {
                if (it == null) {
                    return@HttpTask
                } else {
                    StateSwitches.turnOnOffLight(lightSwitch!!, routine.state)
                    println(it)
                }
            }.execute("POST", "${ApiConfig.arduinoIp}/light", onOff)
        }
    }
}