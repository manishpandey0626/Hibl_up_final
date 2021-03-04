package smsinfosolutions.ind.hibl.registration

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.healthymantra.piousvision.utilities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.R
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityAnimalRegistraionScreen2Binding
import smsinfosolutions.ind.hibl.databinding.AnimalEntryDialogBinding
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg


class AnimalRegistraionScreen2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimalRegistraionScreen2Binding
    private lateinit var dialog_binding: AnimalEntryDialogBinding
    private lateinit var animal_list: MutableList<AnimalDetail>
    private var input_dialog: Dialog? = null
    private var p_no: String? = null
    private var no_of_animal: Int? = null
    private var area_code: String? = null
    private var category_code: String? = null
    private var duration_code: String? = null
    private var wptd: String? = null
    private var db: DatabaseHelper? = null
    private lateinit var animal_type_lov: List<AnimalTypeLov>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalRegistraionScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        animal_list = arrayListOf<AnimalDetail>()
        db = DatabaseHelper(this)
        p_no = intent.getStringExtra("proposal_no")
        no_of_animal = intent.getIntExtra("no_of_animal", 0)
        area_code = intent.getStringExtra("area_code")
        category_code = intent.getStringExtra("category_code")
        duration_code = intent.getStringExtra("duration_code")
        wptd = intent.getStringExtra("wptd")
        db?.let {

            p_no?.let {
                animal_list = db!!.readAnimalDetail(it)
            }
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(getDrawable(R.drawable.divider)!!)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerView.adapter =
            AnimalListAdapter(animal_list, { c, type -> itemClicked(c, type) })
        initialzeinput_dialog()
        binding.save.setOnClickListener {

            if (binding.recyclerView.adapter?.itemCount != no_of_animal) {
                showMsg("No of animals must be equal to $no_of_animal")
                return@setOnClickListener
            }

            for (data in animal_list) {

                val cnt = db?.imageCount(data.proposal_no, data.tag_no!!)
                if (cnt == 0) {
                    showMsg("Please upload atlest one image for tag no ${data.tag_no}")
                    return@setOnClickListener
                }
            }
            getPremium()


        }
        getAnimalType()
    }

    private fun initialzeinput_dialog() {

        input_dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
        dialog_binding = AnimalEntryDialogBinding.inflate(layoutInflater)
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


            // animal_type = it.findViewById<AutoCompleteTextView>(R.id.animal_type_txt)
            // val sum_insured = it.findViewById<TextInputLayout>(R.id.sum_insured)
            var animal_type_code: String? = null
            var limit:Double=0.0
            dialog_binding.animalTypeTxt.setOnItemClickListener { adapterView, view, i, l ->
                animal_type_code = animal_type_lov[i].id
                limit=animal_type_lov[i].limit.toDouble()
            }
            dialog_binding.save.setOnClickListener {


                if (dialog_binding.tagNo.editText?.text.toString().isNullOrBlank()) {
                    showMsg("Please enter tag no.")
                    return@setOnClickListener
                }
                if (dialog_binding.tagNo.editText?.text.toString().length !=12) {
                    showMsg("Tag no must be 12 digits.")
                    return@setOnClickListener
                }

                if (dialog_binding.animalTypeTxt.toString().isNullOrBlank()) {
                    showMsg("Please select animal type")
                    return@setOnClickListener
                }

                if (dialog_binding.sumInsured.editText?.text.toString().isNullOrBlank()) {
                    showMsg("Please enter sum insured")
                    return@setOnClickListener
                }
                if(dialog_binding.sumInsured.editText?.text.toString().toDouble()>limit)
                {
                    showMsg("Sum insured amount can not be greater than $limit")
                    return@setOnClickListener
                }

                val animal = AnimalDetail(
                    p_no!!,
                    dialog_binding.tagNo.editText?.text.toString(),
                    dialog_binding.animalTypeTxt.text.toString(),
                    animal_type_code!!,
                    dialog_binding.sumInsured.editText?.text.toString()
                )
                val result = db?.insertAnimalDetail(animal)
                if (result != -1.toLong()) {
                    p_no?.let {
                        animal_list.clear()
                        animal_list.addAll(db!!.readAnimalDetail(it))
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }

                    dialog_binding.animalTypeTxt.setText("")
                    dialog_binding.sumInsured.editText?.setText("")
                    dialog_binding.tagNo.editText?.setText("")
                    animal_type_code = ""
                    // showMsg("success $result")
                } else {
                    // showMsg("failed: $result")
                }

                input_dialog!!.dismiss()
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.animal_activity_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_record) {
            if (binding.recyclerView.adapter?.itemCount == no_of_animal) {
                showMsg("No of animals must be equal to $no_of_animal")
                return true
            }
            input_dialog!!.show()
            return true
        }
        if (id == android.R.id.home) {
            onBackPressed()
            return true

        }
        return super.onOptionsItemSelected(item)
    }


    fun getAnimalType() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getAnimalType()
        call.enqueue(object : Callback<AnimalTypeLovList> {
            override fun onResponse(call: Call<AnimalTypeLovList>, response: Response<AnimalTypeLovList>) {
                if (response.isSuccessful) {
                    animal_type_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@AnimalRegistraionScreen2Activity,
                        android.R.layout.simple_list_item_1,
                        animal_type_lov.map { it.name })
                    dialog_binding.animalTypeTxt.setAdapter(adapter)

                    /*  data?.let {
                         val code=it.area
                        val area_name= area_lov.filter { it.id.equals(code) }
                        binding.areaTxt.setText(area_name[0].name)
                    }*/

                }
            }

            override fun onFailure(call: Call<AnimalTypeLovList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun itemClicked(animal: AnimalDetail, type: String) {
        if (type == "upload") {
            val intent = Intent(
                this@AnimalRegistraionScreen2Activity,
                UploadAnimalImageActivity::class.java
            ).apply {
                putExtra("proposal_no", animal.proposal_no)
                putExtra("tag_no", animal.tag_no)
            }

            startActivity(intent)
        } else if (type == "delete") {
            val result = db?.deleteAnimal(animal.proposal_no, animal.tag_no!!)
            if (result != -1) {
                p_no?.let {
                    animal_list.clear()
                    animal_list.addAll(db!!.readAnimalDetail(it))
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
    }


    fun getPremium() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val sum = db?.sumInsured(p_no!!).toString()
        val call =
            request.getPremium(p_no!!, sum, duration_code!!, wptd!!, area_code!!, category_code!!)
        call.enqueue(object : Callback<PremiumDetail> {
            override fun onResponse(call: Call<PremiumDetail>, response: Response<PremiumDetail>) {
                if (response.isSuccessful) {

                    db?.let {
                        val premiumDetail = response.body()!!
                        val result = it.insertPremiumDetail(premiumDetail)

                        if (result != -1.toLong()) {
                            val intent = Intent(
                                this@AnimalRegistraionScreen2Activity,
                                AnimalRegistrationFinalActivity::class.java
                            )
                            intent.putExtra("proposal_no", p_no!!)
                            startActivity(intent)
                        } else {
                            showMsg("Something went wrong!!")
                        }
                    }


                }
            }

            override fun onFailure(call: Call<PremiumDetail>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }
}

