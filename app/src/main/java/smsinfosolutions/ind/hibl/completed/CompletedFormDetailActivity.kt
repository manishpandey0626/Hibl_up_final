package smsinfosolutions.ind.hibl.completed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import smsinfosolutions.ind.hibl.utilities.ShowAnimalDetailList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityCompletedFormDetailBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg

class CompletedFormDetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityCompletedFormDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCompletedFormDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)



        val proposal_no = intent.getStringExtra("proposal_no")

        binding.proposalNo.text=intent.getStringExtra("proposal_no")
        binding.policyNo.text=intent.getStringExtra("policy_no")
        binding.certificate.text=intent.getStringExtra("certificate_no")
        binding.beneficiaryName.text=intent.getStringExtra("beneficiary_name")
        binding.inwardDt.text=intent.getStringExtra("inward_dt")
        binding.useDt.text=intent.getStringExtra("use_dt")
        binding.insuranceForm.text=intent.getStringExtra("insurance_from")
        binding.totalAmt.text=intent.getStringExtra("total_amt")
        binding.noOfAnimals.text=intent.getStringExtra("no_of_animal")

        binding.recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        getCompletedFormDetailList(proposal_no)
    }


    fun getCompletedFormDetailList(proposal_no:String) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getCompletedListDetail(AppPreferences.userid, AppPreferences.usertype,proposal_no)
        call.enqueue(object : Callback<ShowAnimalDetailList> {
            override fun onResponse(
                call: Call<ShowAnimalDetailList>,
                response: Response<ShowAnimalDetailList>
            ) {
                if (response.isSuccessful) {

                    binding.recyclerView.adapter = AnimalDetailAdapter(response.body()!!.animal_list)
                }
            }

            override fun onFailure(call: Call<ShowAnimalDetailList>, t: Throwable) {
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