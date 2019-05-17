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
        val NoOfEmp2 = ArrayList<BarEntry>()
        val year = BarDataSet(NoOfEmp2, "erfi")

        NoOfEmp.add(BarEntry(945f, 0f))
        NoOfEmp.add(BarEntry(1040f, 1f))
        NoOfEmp.add(BarEntry(1133f, 2f))
        NoOfEmp.add(BarEntry(1240f, 3f))
        NoOfEmp.add(BarEntry(1369f, 4f))


        NoOfEmp2.add(BarEntry(1501f, 6f))
        NoOfEmp2.add(BarEntry(1645f, 7f))
        NoOfEmp2.add(BarEntry(1578f, 8f))
        NoOfEmp2.add(BarEntry(1695f, 9f))



        val bardataset = BarDataSet(NoOfEmp, "No Of Employee")
        barchart2.animateY(5000)
        val data = BarData(bardataset)
        bardataset.setColors(*ColorTemplate.COLORFUL_COLORS)
        barchart2.setData(data)


    }

}