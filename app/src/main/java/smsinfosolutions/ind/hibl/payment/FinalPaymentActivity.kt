package smsinfosolutions.ind.hibl.payment

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.ResponseBody
import smsinfosolutions.ind.hibl.utilities.LovData
import smsinfosolutions.ind.hibl.utilities.LovDataList
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.DashboardActivity
import smsinfosolutions.ind.hibl.databinding.ActivityFinalPaymentBinding
import smsinfosolutions.ind.hibl.utilities.*
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class FinalPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalPaymentBinding
    private var bank_type_code:String?=null
    lateinit var proposal_nos:String
    private lateinit var bank_type_lov:List<LovData>
    private val PICK_RECEIPT_REQUEST = 11
    private var storage_permission = false
    private val STORAGE_PERMISSION_CODE = 123
    private var uri: Uri? = null
    private var receipt_img: String? = null
    var app_permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var imageUri: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val total_bal = intent.getDoubleExtra("total_bal", 0.00)
        val df = DecimalFormat("#.00")
         proposal_nos=intent.getStringExtra("proposal_nos")
         binding.totalAmt.editText?.setText(df.format(total_bal))
        binding.dateLayout.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    binding.bankDate.editText?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                },
                year,
                month,
                day
            )
            dpd.show()

        }

        binding.paymentTypeTxt.setOnItemClickListener { adapterView, view, i, l ->
            bank_type_code = bank_type_lov[i].id



        }
        binding.receiptUpload.setOnClickListener {
            val mimetype = arrayOf("image/*")
            showFileChooser(mimetype, PICK_RECEIPT_REQUEST)
        }

        binding.save.setOnClickListener{
            if(bank_type_code.isNullOrBlank())
            {
                showMsg("Please select payment type.")
                return@setOnClickListener
            }
            if(binding.bankAmt.editText?.text.isNullOrBlank())
            {
                showMsg("Please enter bank amount")
                return@setOnClickListener
            }

            if(binding.bankAmt.editText?.text.toString().toDouble()<total_bal)
            {
                showMsg("Bank amount can't be less than total amount $total_bal")
                return@setOnClickListener
            }
            if(binding.bankDate.editText?.text.isNullOrBlank())
            {
                showMsg("Please select date")
                return@setOnClickListener
            }
            if(receipt_img.isNullOrBlank())
            {
                showMsg("Please upload receipt.")
                return@setOnClickListener
            }
     saveData()

        }
        requestStoragePermission()
        getBankType()

    }

    fun getBankType() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getBankType()
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    bank_type_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@FinalPaymentActivity,
                        android.R.layout.simple_list_item_1,
                        bank_type_lov.map { it.name })
                    (binding.paymentType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<LovDataList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    private fun requestStoragePermission(): Boolean {
        val permission_needed: MutableList<String> = ArrayList()
        for (perm in app_permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permission_needed.add(perm)
            }
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        if (!permission_needed.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permission_needed.toTypedArray(),
                STORAGE_PERMISSION_CODE
            )
            storage_permission = false
            return false
        } else {
            /*   val filepath =
                   Environment.getExternalStorageDirectory().toString() + "/Japps/Media/Temp"
               val folder = File(filepath)
               if (!folder.exists()) {
                   folder.mkdirs()
               }*/
            storage_permission = true
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    storage_permission = false
                } else {
                    storage_permission = true
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun showFileChooser(mime_type: Array<String>, request: Int) {
        if (storage_permission) {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_type)
//            intent.type = "*/*"
//            startActivityForResult(intent, request)
            val photoFile: File? = try {
                Utils.createImageFile(this)

            } catch (ex: IOException) {
                null
            }
            imageUri=photoFile?.absolutePath
            startActivityForResult(Utils.getPickImageChooserIntent(this, true, photoFile), request)
        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_RECEIPT_REQUEST && resultCode == RESULT_OK  ) {
            var bitmap: Bitmap?
            if (data==null) {
                bitmap = imageUri?.let { Utils.decodeUri(it) } ?:null
            } else {
                uri = data.data
                bitmap = Utils.decodeUri(this, uri)
            }

            bitmap?.let {
                binding.receiptUpload.setImageBitmap(bitmap)
                receipt_img = Utils.Bitmap_to_base64(bitmap)

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    fun saveData()
    {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.updatePayment(bank_type_code!!,binding.bankDate.editText?.text.toString(),binding.bankAmt.editText?.text.toString(),proposal_nos,receipt_img!!)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        val data = obj.getString("data")

                        showMsg("Data saved successfully.")
                        val intent = Intent(this@FinalPaymentActivity, DashboardActivity::class.java)
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
}