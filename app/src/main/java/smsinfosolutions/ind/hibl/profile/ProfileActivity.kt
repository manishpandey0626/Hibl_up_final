package smsinfosolutions.ind.hibl.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.*
import smsinfosolutions.ind.hibl.admin.AdminDashboardActivity
import smsinfosolutions.ind.hibl.databinding.ActivityProfileBinding
import smsinfosolutions.ind.hibl.databinding.AnimalEntryDialogBinding
import smsinfosolutions.ind.hibl.databinding.UpdateEmailBinding
import smsinfosolutions.ind.hibl.databinding.UpdateProfileBinding
import smsinfosolutions.ind.hibl.utilities.*

class ProfileActivity : BaseActivity() {
    lateinit var binding: ActivityProfileBinding
    private lateinit var dialog_binding: UpdateProfileBinding
    private lateinit var email_binding: UpdateEmailBinding
    private var input_dialog: Dialog? = null
    private var email_dialog: Dialog? = null
    private lateinit var action:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.name.text = AppPreferences.username
        binding.mobileno.text = AppPreferences.mobile
        binding.email.text = AppPreferences.email
        binding.designation.text = AppPreferences.usertype
        binding.bank.text = AppPreferences.bank
        binding.branch.text = AppPreferences.branch
        binding.accountno.text = AppPreferences.account
        binding.ifsc.text = AppPreferences.ifsc
        binding.hospital.text = AppPreferences.hospital_name
        binding.district.text = AppPreferences.city_name


        binding.language.setOnClickListener{
            val items = arrayOf("हिंदी", "English")

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.choose_language))
                .setItems(items) { dialog, which ->
                   if(which==1)
                   {
                       AppPreferences.applang = "en"

                   }
                    else
                   {
                       AppPreferences.applang = "hi"
                   }
                    startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
                }
                .show()
        }
 /*       binding.hindi.setOnClickListener {
            AppPreferences.applang = "hi"

            //  ContextUtils.updateLocale(this, AppPreferences.applang!!)
          startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
            finish()
        }
        binding.english.setOnClickListener {
            AppPreferences.applang = "en"
            startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
        }*/

      if(AppPreferences.usertype!="ADMIN") {
          binding.insuredLayout.visibility= View.VISIBLE
          getInsDetails()
      }
        else
      {
          binding.insuredLayout.visibility=View.GONE
      }

        initialzeinput_dialog()
        initialzeEmailDialog()
        binding.editMob.setOnClickListener {
             action="MOBILE"
            input_dialog?.show()
        }

        binding.editEmail.setOnClickListener {
            action="EMAIL"
            email_dialog?.show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getInsDetails() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getVoInsDetails(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<InsDetails> {
            override fun onResponse(
                call: Call<InsDetails>,
                response: Response<InsDetails>
            ) {
                if (response.isSuccessful) {
                 binding.noOfAnimalsInsBig.text=response.body()!!.bIG
                 binding.noOfAnimalsInsSmall.text=response.body()!!.sMALL
                 binding.paidAmt.text=response.body()!!.paid
                 binding.paidDt.text=response.body()!!.paidDt
                 binding.balanceAmt.text=response.body()!!.balance
                }
            }

            override fun onFailure(call: Call<InsDetails>, t: Throwable) {
                showMsg("on FAilure ${t.message}")


            }
        })
    }


    private fun initialzeinput_dialog() {

        input_dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
        dialog_binding = UpdateProfileBinding.inflate(layoutInflater)
        input_dialog?.setContentView(dialog_binding.root)
        input_dialog?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        //input_dialog?.setCancelable(false)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(input_dialog?.getWindow()?.getAttributes())
        val dialogWindowWidth = (displayWidth).toInt()
        //val dialogWindowHeight = (displayHeight).toInt()
        layoutParams.width = dialogWindowWidth
        // layoutParams.height = dialogWindowHeight


        input_dialog?.let {
            it.getWindow()?.setAttributes(layoutParams)


            dialog_binding.update.setOnClickListener {



                    if (dialog_binding.otp.isVisible) {
                        if (dialog_binding.mobileno.editText?.text.toString().isNullOrBlank()) {
                            showMsg("Please enter mobile no.")
                            return@setOnClickListener
                        }
                        if (dialog_binding.mobileno.editText?.text.toString().length != 10) {
                            showMsg("Please enter valid mobile no.")
                            return@setOnClickListener
                        }

                        if (dialog_binding.otp.editText?.text.toString().isNullOrBlank()) {
                            showMsg("Please enter OTP")
                            return@setOnClickListener
                        }

                        updateProfile(
                            dialog_binding.otp.editText?.text.toString(),
                            dialog_binding.mobileno.editText?.text.toString()
                        )

                    } else {
                        if (dialog_binding.mobileno.editText?.text.toString().isNullOrBlank()) {
                            showMsg("Please enter mobile no.")
                            return@setOnClickListener
                        }
                        if (dialog_binding.mobileno.editText?.text.toString().length != 10) {
                            showMsg("Please enter valid mobile no.")
                            return@setOnClickListener
                        }

                        getOTP(dialog_binding.mobileno.editText?.text.toString())

                    }




                }
            }
        }

    private fun initialzeEmailDialog() {

        email_dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
        email_binding = UpdateEmailBinding.inflate(layoutInflater)
        email_dialog?.setContentView(email_binding.root)
        email_dialog?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        //email_dialog?.setCancelable(false)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(email_dialog?.getWindow()?.getAttributes())
        val dialogWindowWidth = (displayWidth).toInt()
        //val dialogWindowHeight = (displayHeight).toInt()
        layoutParams.width = dialogWindowWidth
        // layoutParams.height = dialogWindowHeight


        email_dialog?.let {
            it.getWindow()?.setAttributes(layoutParams)


            email_binding.update.setOnClickListener {



                if (email_binding.otp.isVisible) {
                    if (email_binding.email.editText?.text.toString().isNullOrBlank()) {
                        showMsg("Please enter email id")
                        return@setOnClickListener
                    }


                    if (email_binding.otp.editText?.text.toString().isNullOrBlank()) {
                        showMsg("Please enter OTP")
                        return@setOnClickListener
                    }

                    updateProfile(
                        email_binding.otp.editText?.text.toString(),
                        email_binding.email.editText?.text.toString()
                    )

                } else {
                    if (email_binding.email.editText?.text.toString().isNullOrBlank()) {
                        showMsg("Please enter email id")
                        return@setOnClickListener
                    }


                    getOTP(AppPreferences.mobile)

                }




            }
        }
    }



    fun getOTP(mobile:String)
    {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getProfileUdateOTP(AppPreferences.userid,AppPreferences.usertype,mobile)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        if("MOBILE"==action) {
                            TransitionManager.beginDelayedTransition(dialog_binding.transitionLayout)
                            dialog_binding.otp.visibility = View.VISIBLE
                            dialog_binding.update.text = getString(R.string.save)
                        }
                        else
                        {
                            TransitionManager.beginDelayedTransition(email_binding.transitionLayout)
                            email_binding.otp.visibility = View.VISIBLE
                            email_binding.update.text = getString(R.string.save)

                        }
                    } else {
                        val data = obj.getString("data")
                        showMsg(data)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                //

            }
        })



    }


    fun updateProfile(otp:String,txt: String)
    {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.updateProfile(AppPreferences.userid,AppPreferences.usertype,otp,action,txt)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        if("MOBILE"==action) {
                            input_dialog?.dismiss()
                            AppPreferences.mobile = txt
                            binding.mobileno.text = AppPreferences.mobile
                        }
                        else
                        {
                            email_dialog?.dismiss()
                            AppPreferences.email = txt
                            binding.email.text = AppPreferences.email

                        }

                    }
                        val data = obj.getString("data")
                        showMsg(data)

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                //

            }
        })

    }

}