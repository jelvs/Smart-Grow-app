package unl.fct.smart_grow.utils

import android.os.Build
import android.support.annotation.RequiresApi
import java.time.LocalDateTime
import kotlin.concurrent.thread

object RoutineHelper {

    private val routines = mutableListOf<Routine>()
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

    private fun executeRoutine (routine: Routine) {
        //TODO: call to arduino
        println ("executing routine")
    }
}