package com.hiddencoders.cattleinsurance.ui.farmerentry

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hiddencoders.cattleinsurance.data.AuthListner
import com.hiddencoders.cattleinsurance.data.model.Banks
import com.hiddencoders.cattleinsurance.data.model.Branch
import com.hiddencoders.cattleinsurance.data.model.Centers
import com.hiddencoders.cattleinsurance.data.model.Villages
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import com.hiddencoders.cattleinsurance.databinding.ActivityFarmerEntryBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.tagging.TaggingActivity
import com.hiddencoders.cattleinsurance.ui.utilis.Utilities
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class FarmerEntryActivity : BaseClass(), AuthListner {
    private lateinit var binding: ActivityFarmerEntryBinding
    private val viewmodel by viewModels<FarmerEntryViewmodel>()
    private lateinit var filePathImageCamera: File
    private lateinit var filePathImageCamera1: File
    private var getVillagesList: ArrayList<Villages> = ArrayList()
    private var getCenterList:ArrayList<Centers> = ArrayList()
    private var getBanksList:ArrayList<Banks> = ArrayList()
    private var getBranchList:ArrayList<Branch> = ArrayList()
    private var villagesList: java.util.ArrayList<Villages> = arrayListOf()
    var mBitmap: Bitmap? = null

    private lateinit var disposable: Disposable

    @Inject
    lateinit var apiServices: ApiServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmerEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    private fun setData() {
/*        MainScope().launch {
            withContext(Dispatchers.Default) {
                getVillagesData()
            }
        }*/
        getVillagesData()
        getBanksData()
        viewmodel.authListner = this
        viewmodel.villageName = ""
        viewmodel.farmerCode = ""
        viewmodel.farmerName = ""
        viewmodel.fatherName = ""
        viewmodel.contactNumber = ""
        viewmodel.adharNo = ""
        viewmodel.bankAcNo = ""
        viewmodel.bankName = ""
        viewmodel.branch = ""
        viewmodel.uploadBankPassbook = ""
        viewmodel.uploadAdhar = ""
        viewmodel.bcode = userSession.getUserId()
        viewmodel.username = userSession.getUsername()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivImage1.setOnClickListener {
            photoCameraIntent(1)
        }
        binding.ivImage2.setOnClickListener {
            photoCameraIntent(2)
        }
        binding.btSave.setOnClickListener {
            addFormToServer()
        }
    }

    private fun getVillagesData() {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getVillages().observeForever {
                if (it.code == 0) {
                    val list = arrayOfNulls<String>(it.Villages.size)
                    getVillagesList.clear()
                    getVillagesList.addAll(it.Villages)
                    for (i in 0 until it.Villages.size) {
                        list[i] = it.Villages[i].VillName
                    }

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item, list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spVillageName.adapter = adapter

                    binding.spVillageName.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val mandalName = parent?.getItemAtPosition(position).toString()
                                if (getVillagesList.isNotEmpty()){
                                    viewmodel.villageName = getVillagesList[position].VillID.toString()
                                    getCentersData(getVillagesList[position].VillID)
                                }

                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }
    private fun getBanksData() {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getBanks().observeForever {
                if (it.code == "0") {
                    val list = arrayOfNulls<String>(it.Banks.size)
                    getBanksList.clear()
                    getBanksList.addAll(it.Banks)
                    for (i in 0 until it.Banks.size) {
                        list[i] = it.Banks[i].BKNAME
                    }

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item, list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.etBankName.adapter = adapter

                    binding.etBankName.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val mandalName = parent?.getItemAtPosition(position).toString()
                                viewmodel.bankName = mandalName
                                getBranchesData(getBanksList[position].BKNAME)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }
    private fun getBranchesData(code: String) {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getBranches(code).observeForever {
                if (it.code == "0") {
                    val list = arrayOfNulls<String>(it.Branch.size)
                    getBranchList.clear()
                    getBranchList.addAll(it.Branch)
                    for (i in 0 until it.Branch.size) {
                        list[i] = it.Branch[i].CITY
                    }

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item, list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spBranchName.adapter = adapter

                    binding.spBranchName.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val mandalName = parent?.getItemAtPosition(position).toString()
                                viewmodel.branch = getBranchList[position].BKCODE.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }
    private fun getCentersData(code:Int) {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getCenters(code).observeForever {
                if (it.code == "0") {
                    val list = arrayOfNulls<String>(it.Centers.size)
                    getCenterList.clear()
                    getCenterList.addAll(it.Centers)
                    for (i in 0 until it.Centers.size) {
                        list[i] = it.Centers[i].CNAME
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
                                val mandalName = parent?.getItemAtPosition(position).toString()
                                viewmodel.centerName = getCenterList[position].CCODE.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }

    private fun addFormToServer() {
        viewmodel.farmerCode = binding.etFarmerCode.text.toString()
        viewmodel.farmerName = binding.etFarmerName.text.toString()
        viewmodel.fatherName = binding.etFatherName.text.toString()
        viewmodel.contactNumber = binding.etContactNumber.text.toString()
        viewmodel.adharNo = binding.etAdharNumber.text.toString()
        viewmodel.bankAcNo = binding.etBankAccountNo.text.toString()
        viewmodel.addFarmerFormToServer()
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
            startActivityForResult(it, TaggingActivity.IMAGE_CAMERA_REQUEST)
        } else {
            startActivityForResult(it, TaggingActivity.IMAGE_CAMERA_REQUEST2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TaggingActivity.IMAGE_CAMERA_REQUEST) {
            if (filePathImageCamera.exists()) {
                lifecycleScope.launch {
                    val filePath =
                        Compressor.compress(this@FarmerEntryActivity, filePathImageCamera) {
                            resolution(480, 480)
                            quality(50)
                            format(Bitmap.CompressFormat.JPEG)
                            size(209715)
                        }
                    Glide.with(this@FarmerEntryActivity).load(filePath).into(binding.ivImage1)
                    binding.ivImage1.scaleType = ImageView.ScaleType.FIT_XY
                    binding.ivImage1.setPadding(0, 0, 0, 0)
                    viewmodel.uploadAdhar = encode(filePath)
                }
            }
        } else if (requestCode == TaggingActivity.IMAGE_CAMERA_REQUEST2) {
            lifecycleScope.launch {
                val filePath = Compressor.compress(this@FarmerEntryActivity, filePathImageCamera1) {
                    resolution(480, 480)
                    quality(50)
                    format(Bitmap.CompressFormat.JPEG)
                    size(209715)
                }
                Glide.with(this@FarmerEntryActivity).load(filePath).into(binding.ivImage2)
                binding.ivImage2.scaleType = ImageView.ScaleType.FIT_XY
                binding.ivImage2.setPadding(0, 0, 0, 0)
                viewmodel.uploadBankPassbook = encode(filePath  )
            }
        }
    }

    override fun onStarted() {

    }

    override fun onSuccess() {
        Utilities.showAlertDialog(this,"Farmer Entry","Added Successfully")
        Handler().postDelayed({
            onBackPressed()
        },3000)
    }

    override fun onFailure(message: String) {
        showToast(this, message)
    }
}