package com.hiddencoders.cattleinsurance.ui.tagging

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.AuthListner
import com.hiddencoders.cattleinsurance.data.model.Centers
import com.hiddencoders.cattleinsurance.data.model.Farmers
import com.hiddencoders.cattleinsurance.databinding.ActivityTaggingBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.farmerentry.FarmerEntryActivity
import com.hiddencoders.cattleinsurance.ui.farmers.FarmersViewmodel
import com.hiddencoders.cattleinsurance.ui.utilis.Utilities
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EditTaggingActivity : BaseClass(), AuthListner, AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityTaggingBinding
    var mSelectedMonth: String = ""
    var mSelectedDay: String = ""
    var mSelectedYear: String = ""
    var mDOB: String = ""
    private var mYear: Int = 0
    private var mMOnth_val: Int = 0
    private lateinit var filePathImageCamera: File
    private lateinit var filePathImageCamera1: File
    private lateinit var rbBuffaloeType: RadioButton
    private lateinit var rbBreedType: RadioButton
    private lateinit var rbLocalGraded: RadioButton
    private lateinit var rbAnimalStatus: RadioButton
    private val farmersViewmodel by viewModels<FarmersViewmodel>()
    private var farmerId: Int = 0
    private var centerId: Int = 0
    private var farmerlist: kotlin.collections.ArrayList<Farmers> = ArrayList()
    private var centerList: kotlin.collections.ArrayList<Centers> = ArrayList()
    private val viewmodel by viewModels<TaggingViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaggingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListners()
    }

    private fun setClickListners() {
        viewmodel.categoryId = ""

        val ide = intent.getStringExtra("id")!!
        viewmodel.getTagById(ide).observeForever {
            if (it.code.isNotEmpty()) {
                if (it.Tags.isNotEmpty()) {
                    val data = it.Tags[0]
                    binding.etDate.setText(data.TDATE.split("T")[0])
                    binding.etMilkYeild.setText(data.MILK_YIELD)
                    binding.etPremium.setText("${data.PREMIUM} INR")
                    binding.etRctNo.setText(data.RCTNO)
                    viewmodel.categoryId = data.CATID.toString()
                    binding.etTagNo.setText(data.TAGNO)
                    farmerId = data.FARMERID
                    centerId = data.ccode
                    if (data.IMAGE1_IMAGE != null && data.IMAGE2_IMAGE != null) {
                        viewmodel.uploadImage1 = data.IMAGE1_IMAGE
                        viewmodel.uploadImage2 = data.IMAGE2_IMAGE
                        lifecycleScope.launch {
                            Glide.with(this@EditTaggingActivity).load(decode(data.IMAGE1_IMAGE))
                                .into(binding.ivImage1)
                            Glide.with(this@EditTaggingActivity).load(decode(data.IMAGE2_IMAGE))
                                .into(binding.ivImage2)

                            binding.ivImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                            binding.ivImage1.setPadding(0, 0, 0, 0)
                            binding.ivImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                            binding.ivImage2.setPadding(0, 0, 0, 0)
                        }

                    }
                    viewmodel.selectFarmer = data.FARMERID.toString()
                    if (data.BREED == "1") {
                        binding.radioLocal.isChecked = true
                    } else if (data.BREED == "2") {
                        binding.radioCross.isChecked = true
                    } else {
                        binding.radioPure.isChecked = true
                    }
                    if (data.ANIMAL_STATUS == 1) {
                        binding.radioMilk.isChecked = true
                    } else {
                        binding.radioPregnent.isChecked = true
                    }
                    if (data.BUFF_COW == "1") {
                        binding.radioBuff.isChecked = true
                    } else {
                        binding.radioCow.isChecked = true
                    }
//                    getFarmersData(0)
                    getCentersData()

                }
            }
        }

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formattedDate: String = df.format(c)
        viewmodel.authListener = this
        viewmodel.animalStatus = ""
        viewmodel.breedType = ""
        viewmodel.buffaloType = ""
        viewmodel.rctNo = ""
        viewmodel.tagNo = ""
        viewmodel.localGraded = ""
        viewmodel.milkFeild = ""
        viewmodel.selectFarmer = ""
        viewmodel.selectedDate = formattedDate
        viewmodel.uploadImage1 = ""
        viewmodel.uploadImage2 = ""
        viewmodel.bcode = userSession.getUserId()
        viewmodel.username = userSession.getUsername()
        binding.btSave.text = "Edit Tag"
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.etMilkYeild.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s!!.isNotEmpty()) {
                    val count = s.toString().toInt()
                    if (count > 50) {
                        binding.etMilkYeild.error = "Should not exceed 50 Ltrs"
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etDate.setOnClickListener {
            openDateDialog()
        }
        binding.ivImage1.setOnClickListener {
            photoCameraIntent(1)
        }
        binding.ivImage2.setOnClickListener {
            photoCameraIntent(2)
        }
        binding.btAddFarmer.setOnClickListener {
            startActivity(Intent(this, FarmerEntryActivity::class.java))
        }
        viewmodel.localGraded = "300"

        binding.radioGroup4.setOnCheckedChangeListener { group, checkedId ->
            val rg = group.checkedRadioButtonId
            val rbBreedType = findViewById<RadioButton>(rg)
            if (rbBreedType.text.toString() == "Milking") {
                binding.etMilkYeild.visibility = View.VISIBLE
            } else if (rbBreedType.text.toString() == "Pregnent") {
                binding.etMilkYeild.visibility = View.GONE
            }
        }
        binding.radioGroup2.setOnCheckedChangeListener { radioGroup, i ->
            val rg = radioGroup.checkedRadioButtonId
            val rbBreedType = findViewById<RadioButton>(rg)
            if (rbBreedType.text.toString() == "Local breed") {
                viewmodel.breedType = "1"
                binding.etPremium.setText("300 INR")
                viewmodel.localGraded = "300"
            } else if (rbBreedType.text.toString() == "Cross breed  (Graded Murrah, Jersey, Gir)") {
                viewmodel.breedType = "2"
                viewmodel.localGraded = "400"
                binding.etPremium.setText("400 INR")
            } else if (rbBreedType.text.toString() == "Pure breed   (Pure Murrah, HF)") {
                viewmodel.breedType = "3"
                viewmodel.localGraded = "500"
                binding.etPremium.setText("500 INR")
            }
        }
        binding.btSave.setOnClickListener {
            val intBuffaloType: Int = binding.radioGroup1.checkedRadioButtonId
            rbBuffaloeType = findViewById<RadioButton>(intBuffaloType)

            if (rbBuffaloeType.text.toString() == "Buffalo") {
                viewmodel.buffaloType = "1"
            } else {
                viewmodel.buffaloType = "2"
            }

            val intBreedType: Int = binding.radioGroup2.checkedRadioButtonId
            rbBreedType = findViewById<RadioButton>(intBreedType)
            if (rbBreedType.text.toString() == "Local breed") {
                viewmodel.breedType = "1"
            } else if (rbBreedType.text.toString() == "Cross breed") {
                viewmodel.breedType = "2"
            } else if (rbBreedType.text.toString() == "Pure breed") {
                viewmodel.breedType = "3"
            }
            /*      val intLocalGrade: Int = binding.radioGroup3.checkedRadioButtonId
                  rbLocalGraded = findViewById<RadioButton>(intLocalGrade)
                  if (rbLocalGraded.text.toString() == "Local") {
                      viewmodel.localGraded = "1"
                  } else {
                      viewmodel.localGraded = "2"
                  }*/
            val intAnimalStatus: Int = binding.radioGroup4.checkedRadioButtonId
            rbAnimalStatus = findViewById<RadioButton>(intAnimalStatus)

            if (rbAnimalStatus.text.toString() == "Milking") {
                viewmodel.animalStatus = "1"
            } else {
                viewmodel.animalStatus = "2"
            }
            viewmodel.milkFeild = binding.etMilkYeild.text.toString().trim()
            viewmodel.tagNo = binding.etTagNo.text.toString()
            viewmodel.rctNo = binding.etRctNo.text.toString()
            viewmodel.editTagFormOnServer()
        }

    }


    private fun getFarmersData(code: Int) {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getFarmers(code).observe(this, androidx.lifecycle.Observer {
                val list = arrayOfNulls<String>(it.Farmers.size)
                farmerlist.clear()

                for (i in 0 until it.Farmers.size) {
                    list[i] = it.Farmers[i].FARMERNAME
                    farmerlist.add(it.Farmers[i])
                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, list
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.spFarmerName.adapter = adapter
                binding.spFarmerName.onItemSelectedListener = this
                if (farmerId != 0) {
                    for (i in list.indices) {
                        val group = farmerlist[i].FARMERID.toString()
                        if (group == farmerId.toString()) {
                            binding.spFarmerName.setSelection(i, true)
                        }
                    }

                }
            })

        }
    }

    private fun getCentersData() {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getCenters().observe(this, androidx.lifecycle.Observer {
                val list = arrayOfNulls<String>(it.Centers.size)
                centerList.clear()

                for (i in 0 until it.Centers.size) {
                    list[i] = it.Centers[i].CNAME
                    centerList.add(it.Centers[i])
                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spCenterName.adapter = adapter

                binding.spCenterName.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            getFarmersData(centerList[position].CCODE)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                    }
                if (centerId != 0) {
                    for (i in list.indices) {
                        val group = centerList[i].CCODE.toString()
                        if (group == centerId.toString()) {
                            binding.spCenterName.setSelection(i, true)
                        }
                    }

                }

            })

        }
    }

    private fun photoCameraIntent(type: Int) {
        try {
            if (type == 1) {
                filePathImageCamera = createImageFile()
            } else {
                filePathImageCamera1 = createImageFile()
            }
        } catch (ex: IOException) {
            // Error occurred while creating the File
        }
        val it = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI: Uri = if (type == 1) FileProvider.getUriForFile(
            this, "com.hiddencoders.cattleinsurance" + ".provider",
            filePathImageCamera
        ) else {
            FileProvider.getUriForFile(
                this, "com.hiddencoders.cattleinsurance" + ".provider",
                filePathImageCamera1
            )
        }
        //        Uri photoURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        if (type == 1) {
            startActivityForResult(it, IMAGE_CAMERA_REQUEST)
        } else {
            startActivityForResult(it, IMAGE_CAMERA_REQUEST2)
        }
    }


    companion object {
        const val IMAGE_CAMERA_REQUEST = 2
        const val IMAGE_CAMERA_REQUEST2 = 3
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (filePathImageCamera.exists()) {
                lifecycleScope.launch {
                    val filePath =
                        Compressor.compress(this@EditTaggingActivity, filePathImageCamera) {
                            resolution(480, 480)
                            quality(80)
                            format(Bitmap.CompressFormat.JPEG)
                            size(209715)
                        }
                    Glide.with(this@EditTaggingActivity).load(filePath).into(binding.ivImage1)
                    binding.ivImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.ivImage1.setPadding(0, 0, 0, 0)
                    viewmodel.uploadImage1 = encode(filePath)
                }
            }
        } else if (requestCode == IMAGE_CAMERA_REQUEST2 && resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val filePath = Compressor.compress(this@EditTaggingActivity, filePathImageCamera1) {
                    resolution(480, 480)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(209715)
                }
                Glide.with(this@EditTaggingActivity).load(filePath).into(binding.ivImage2)
                binding.ivImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.ivImage2.setPadding(0, 0, 0, 0)
                viewmodel.uploadImage2 = encode(filePath)
            }
        }
    }

    private fun openDateDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.datedialog)
        val picker_dailog = dialog.findViewById(R.id.picker_dicker) as DatePicker
        val body = dialog.findViewById(R.id.tvTitle) as TextView
        body.text = "Enter From Date"
        val ok_dialog = dialog.findViewById(R.id.tvOk) as TextView
        val cancel_dialog = dialog.findViewById(R.id.tvCancel) as TextView
        val c = Calendar.getInstance()
        c[2003, 8] = 1
        val ca = Calendar.getInstance()
        ca.add(Calendar.YEAR, -19)
        picker_dailog.maxDate = ca.timeInMillis
        val calender = Calendar.getInstance()
        mYear = calender[Calendar.YEAR]
        val mMonth = calender[Calendar.MONTH]
        var selectedYear: String = ""
        val mDay = calender[Calendar.DAY_OF_MONTH]
        picker_dailog.init(mYear, mMonth, mDay,
            DatePicker.OnDateChangedListener { view, year, month, day ->

                mSelectedYear = year.toString()

                if (month < 9) {
                    val mMonth = month + 1
                    mSelectedMonth = "0$mMonth"
                }
                if (month >= 9) {
                    val mMonth = month + 1
                    mSelectedMonth = mMonth.toString()
                }

                if (day < 10) {
                    mSelectedDay = "0$day"
                }
                if (day >= 10) {
                    mSelectedDay = day.toString()
                }
                mDOB = "$mSelectedYear-$mSelectedMonth-$mSelectedDay"
                viewmodel.selectedDate = mDOB
                binding.etDate.setText(mDOB)

            })

        if (binding.etDate.text!!.isNotEmpty()) {
            val mYearEdit = binding.etDate.text.toString().trim()
            val items1: List<String> = mYearEdit.split("-")
            val date1 = items1[0]
            mMOnth_val = items1[1].toInt()
            val year = items1[2]
            val mMonth_selected = mMOnth_val - 1

            picker_dailog.updateDate(
                Integer.parseInt(date1),
                mMonth_selected,
                Integer.parseInt(year)
            );
        }

        ok_dialog.setOnClickListener {

            dialog.dismiss()

        }
        cancel_dialog.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onStarted() {

    }

    override fun onSuccess() {
        Utilities.showAlertDialog(this, "Tagging", "Updated Successfully")
        Handler().postDelayed({
            onBackPressed()
        }, 3000)

    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val farmerName = farmerlist[position].FARMERID.toString()
        viewmodel.selectFarmer = farmerName

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        viewmodel.selectFarmer = ""
    }
}