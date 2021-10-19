package smsinfosolutions.ind.hibl.registration

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import smsinfosolutions.ind.hibl.DashboardActivity
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityAnimalRegistrationFinalBinding
import smsinfosolutions.ind.hibl.utilities.showMsg


class AnimalRegistrationFinalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimalRegistrationFinalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalRegistrationFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val proposal_no = intent.getStringExtra("proposal_no").toString()
        val db = DatabaseHelper(this)
        val premium_detail = db?.readPremiumDetail(proposal_no)

        premium_detail?.let {
            binding.beneficiaryShare.editText?.setText(premium_detail.beneficiary_share)
            binding.centralShare.editText?.setText(premium_detail.central_share)
            binding.stateShare.editText?.setText(premium_detail.state_share)
            binding.totalPremium.editText?.setText(premium_detail.total_premius)
        }

        binding.confirmButton.setOnClickListener {
            val result = db?.updateStatus(proposal_no, "Y")
            if (result > 0) {
                showMsg("Data saved successfully.")
                val intent = Intent(this, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                showMsg("Something went wrong!!")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}