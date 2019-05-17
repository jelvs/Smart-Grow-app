package unl.fct.smart_grow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;




class BarChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)

        val barchart2 = findViewById<BarChart>(R.id.barchart)
        val NoOfEmp = ArrayList<BarEntry>()



        NoOfEmp.add(BarEntry(94f, 20f))
        NoOfEmp.add(BarEntry(104f, 30f))
        NoOfEmp.add(BarEntry(113f, 40f))






        val bardataset = BarDataSet(NoOfEmp, "No Of Employee")
        barchart2.animateY(5000)
        val yAxis = barchart2.axisLeft
        val data = BarData(bardataset)
        bardataset.setColors(*ColorTemplate.COLORFUL_COLORS)
        barchart2.setData(data)


    }

}