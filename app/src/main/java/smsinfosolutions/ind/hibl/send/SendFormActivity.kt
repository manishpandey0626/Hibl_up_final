package smsinfosolutions.ind.hibl.send


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.healthymantra.piousvision.utilities.SendUserData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.DashboardActivity
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivitySendFormBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg


class SendFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendFormBinding
    private lateinit var adapter: SendListAdapter
    lateinit var db: DatabaseHelper
    lateinit var list: List<SendUserData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        db = DatabaseHelper(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val data = db.getProposalList(status = "Y")
        adapter = SendListAdapter(data, { itemClicked(it) })
        binding.recyclerView.adapter = adapter
        binding.confirmButton.setOnClickListener {

            list = adapter.getSelectedRecords()
            if (list.size > 0) {
                val gson = Gson()
                val data = com.google.gson.JsonArray()
                for (user: SendUserData in list) {
                    val animal_detail_list = db.readAnimalDetail(user.proposal_no)
                    val readPremiumDetail = db.readPremiumDetail(user.proposal_no)
                    val array = com.google.gson.JsonArray()

                    for (animal in animal_detail_list) {
                        val animal_images_list =
                            db.readAnimalImages(animal.proposal_no, animal.tag_no!!)


                        val jsonElement = gson.toJsonTree(animal)
                        jsonElement.asJsonObject.add(
                            "animal_images",
                            gson.toJsonTree(animal_images_list)
                        )
                        array.add(jsonElement)


                    }

                    //Log.d("tag", gson.toJson(array))

                    val jsonElement = gson.toJsonTree(user)

                    jsonElement.asJsonObject.add("animal_detail", array)
                    jsonElement.asJsonObject.add(
                        "premium_detail",
                        gson.toJsonTree(readPremiumDetail)
                    )
                    data.add(jsonElement)
                }
                val records = gson.toJson(data)
                saveData(records)
            } else {
                showMsg("No record selected!!")
            }
        }
    }

    private fun itemClicked(user: SendUserData) {
        /* val intent = Intent(this@SendFormActivity, NewAnimalRegistrationActivity::class.java)
         intent.putExtra("proposal_no", user.proposal_no)
         startActivity(intent)*/


    }

    fun saveData(records: String) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.saveRecord(AppPreferences.userid, AppPreferences.usertype, records)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    Log.d("res","res==>"+json_data)
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        val data = obj.getString("data")

                        showMsg("Data saved successfully.")

                        val proposal_nos = list.joinToString { it.proposal_no }

                        db.delete(proposal_nos)
                        val intent = Intent(this@SendFormActivity, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()

                    } else {
                        val data = obj.getString("data")
                        showMsg(data)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}