package smsinfosolutions.ind.hibl.report

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityReportBinding
import smsinfosolutions.ind.hibl.utilities.*


class ReportActivity : AppCompatActivity() {
    lateinit var binding:ActivityReportBinding
    lateinit var tile_no:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)



       val title= intent.getStringExtra("title")
        binding.toolbar.title=title
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        tile_no=intent.getStringExtra("tile_no")
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        getReport()
    }


    fun getReport() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getReport(AppPreferences.mobile, tile_no)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {

                    val json_str = response.body()!!.string()
                    val gson = Gson()

           /*         val jelement: JsonElement = JsonParser().parse(json_str)
                    var jobject = jelement.asJsonObject

                    val titles = jobject.getAsJsonArray("title")*/


                    val data = gson.fromJson(json_str, ReportData::class.java)

                   val titles=data.title
                    binding.head1.text=titles[0]
                    binding.head2.text=titles[1]
                    if(titles.size>2)
                    {
                        binding.head3.text=titles[2]
                        binding.head3.visibility= View.VISIBLE
                    }
                    else
                    {
                        binding.head3.visibility= View.GONE
                    }
                    val adapter = ReportAdapter(data.data,titles.size)
                    binding.recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message)

            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}