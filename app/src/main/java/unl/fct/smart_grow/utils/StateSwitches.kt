package unl.fct.smart_grow.utils

import android.widget.Switch

object StateSwitches {

    var water: Boolean = false
    var light: Boolean = false

    fun turnOnOffLight(switch: Switch, on: Boolean) {
        light = on
        switch.isChecked = on
    }

    fun turnOnOffWater(switch: Switch, on: Boolean) {
        water = on
        switch.isChecked = on
    }
}