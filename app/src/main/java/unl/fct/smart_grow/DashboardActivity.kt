package unl.fct.smart_grow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText

import com.github.mikephil.charting.charts.BarChart;

class DashboardActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val barChart =  findViewById<BarChart>(R.id.barchart)

        val btnBarChart = findViewById<Button>(R.id.btnBarChart)

        btnBarChart.setOnClickListener {
            startActivity(Intent(this, BarChartActivity::class.java))
        }




    }


}