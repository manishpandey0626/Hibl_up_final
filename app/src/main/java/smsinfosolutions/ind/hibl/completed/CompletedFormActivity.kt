package smsinfosolutions.ind.hibl.completed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import smsinfosolutions.ind.hibl.utilities.CompletedFormDetail
import smsinfosolutions.ind.hibl.utilities.CompletedFormList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityCompletedFormBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg


class CompletedFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompletedFormBinding
    private lateinit var adapter: CompletedFormAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )



        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })



        getCompletedFormList()

    }


    fun getCompletedFormList() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getCompletedList(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<CompletedFormList> {
            override fun onResponse(
                call: Call<CompletedFormList>,
                response: Response<CompletedFormList>
            ) {
                if (response.isSuccessful) {

                    adapter = CompletedFormAdapter(response.body()!!.form_list,{c->itemClicked(c)})
                    binding.recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<CompletedFormList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun itemClicked(data: CompletedFormDetail)
    {
        startActivity(Intent(this@CompletedFormActivity,CompletedFormDetailActivity::class.java).apply {
            putExtra("proposal_no",data.proposal_no)
            putExtra("policy_no",data.policy_no)
            putExtra("certificate_no",data.certificate_no)
            putExtra("beneficiary_name",data.beneficiary_name)
            putExtra("inward_dt",data.inward_dt)
            putExtra("use_dt",data.use_dt)
            putExtra("insurance_from",data.insurance_from)
            putExtra("total_amt",data.total_amt)
            putExtra("no_of_animal",data.no_of_animal)
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}